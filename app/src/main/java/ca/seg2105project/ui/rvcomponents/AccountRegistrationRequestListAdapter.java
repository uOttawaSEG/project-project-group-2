package ca.seg2105project.ui.rvcomponents;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ca.seg2105project.R;
import ca.seg2105project.model.registrationRequestClasses.AccountRegistrationRequest;
import ca.seg2105project.model.registrationRequestClasses.RegistrationRequestStatus;
import ca.seg2105project.model.repositories.AccountRegistrationRequestRepository;


/**
 * A class that displays the list of registration requests in a RecyclerView
 */
public class AccountRegistrationRequestListAdapter extends RecyclerView.Adapter<UserRequestViewHolder> {

    List<AccountRegistrationRequest> registrationRequests;
    AccountRegistrationRequestRepository accountRegistrationRequestRepository;

    /**
     * A parameterized constructor for AdminAdapter.
     * @param registrationRequests a list of registration requests
     * @param
     */
    public AccountRegistrationRequestListAdapter(List<AccountRegistrationRequest> registrationRequests, AccountRegistrationRequestRepository accountRegistrationRequestRepository) {
        this.registrationRequests = registrationRequests;
        this.accountRegistrationRequestRepository = accountRegistrationRequestRepository;
    }


    /**
     * Creates a new RequestViewHolder for a new registrationRequest added to the list
     * @return a Viewholder for the new registration request created
     */
    @NonNull
    @Override
    public UserRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserRequestViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.rvitem_userrequest,
                parent,
                false));
    }

    /**
     * Adds all necessary data for the registration request, and displays it in it's own RequestViewHolder
     */
    @Override
    public void onBindViewHolder(@NonNull UserRequestViewHolder holder, int position) {
        holder.firstName.setText(registrationRequests.get(position).getFirstName());
        holder.lastName.setText(registrationRequests.get(position).getLastName());
        holder.email.setText(registrationRequests.get(position).getEmail());
        holder.address.setText(registrationRequests.get(position).getAddress());
        holder.phoneNumber.setText(registrationRequests.get(position).getPhoneNumber());
        holder.organizationName.setText(registrationRequests.get(position).getOrganizationName());

        // Make reject button invisible if we are on rejected requests screen
        if (registrationRequests.get(position).getStatus() == RegistrationRequestStatus.REJECTED) {
            holder.rejectButton.setVisibility(View.INVISIBLE);
        }

        holder.approveButton.setOnClickListener(v -> {
                accountRegistrationRequestRepository.updateRequestStatus(registrationRequests.get(position).getEmail(), RegistrationRequestStatus.APPROVED);
                registrationRequests.remove(position);
                notifyDataSetChanged();
        });
        holder.rejectButton.setOnClickListener(v -> {
            // Only change status if it hasn't already been changed
            if (registrationRequests.get(position).getStatus() != RegistrationRequestStatus.REJECTED) {
                accountRegistrationRequestRepository.updateRequestStatus(registrationRequests.get(position).getEmail(), RegistrationRequestStatus.REJECTED);
                registrationRequests.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    /**
     * Keeps track of the size registrationRequests
     * @return the number of requests in the registration request list
     */
    @Override
    public int getItemCount() {
        return registrationRequests.size();
    }
}