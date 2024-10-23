package ca.seg2105project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ca.seg2105project.model.registrationRequestClasses.AccountRegistrationRequest;


/**
 * A class that displays the list of registration requests in a RecyclerView
 */
public class AdminAdapter extends RecyclerView.Adapter<RequestViewHolder> {

    Context registrationRequestContext;
    List<AccountRegistrationRequest> registrationRequests;

    /**
     * A parameterized constructor for AdminAdapter.
     * @param registrationRequests a list of registration requests
     * @param registrationRequestContext provides the adapter a proper environment to display the registration requests in the RecyclerView
     */
    public AdminAdapter(List<AccountRegistrationRequest> registrationRequests, Context registrationRequestContext) {
        this.registrationRequests = registrationRequests;
        this.registrationRequestContext = registrationRequestContext;
    }


    /**
     * Creates a new RequestViewHolder for a new registrationRequest added to the list
     * @return a Viewholder for the new registration request created
     */
    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RequestViewHolder(LayoutInflater.from(registrationRequestContext).inflate(R.layout.item_requestbox,parent,false));
    }

    /**
     * Adds all necessary data for the registration request, and displays it in it's own RequestViewHolder
     */
    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        holder.firstName.setText(registrationRequests.get(position).getFirstName());
        holder.lastName.setText(registrationRequests.get(position).getLastName());
        holder.email.setText(registrationRequests.get(position).getEmail());
        holder.address.setText(registrationRequests.get(position).getAddress());
        holder.phoneNumber.setText(registrationRequests.get(position).getPhoneNumber());
        holder.organizationName.setText(registrationRequests.get(position).getOrganizationName());
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
