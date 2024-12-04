package ca.seg2105project.ui.rvcomponents;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ca.seg2105project.R;
import ca.seg2105project.model.registrationRequestClasses.RegistrationRequestStatus;
import ca.seg2105project.model.repositories.EventRepository;
import ca.seg2105project.model.repositories.UserRepository;
import ca.seg2105project.model.userClasses.User;

public class EventRegistrationRequestListAdapter extends RecyclerView.Adapter<UserRequestViewHolder> {

    // The eventID of the event that has the provided list of event registration request statuses
    private final String eventID;

    // This enum stores which type of event registration requests we are displaying in this adapter.
    // It's one of PENDING, REJECTED, APPROVED
    private final RegistrationRequestStatus registrationRequestStatus;

    // The list of event registration request emails
    private final ArrayList<String> eventRegistrationRequestEmails;

    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public EventRegistrationRequestListAdapter(String eventID, RegistrationRequestStatus registrationRequestStatus,
                                               ArrayList<String> eventRegistrationRequestEmails, EventRepository eventRepository,
                                               UserRepository userRepository, Button approveAllEventRegistrationRequestsButton) {
        this.eventID = eventID;
        this.registrationRequestStatus = registrationRequestStatus;
        this.eventRegistrationRequestEmails = eventRegistrationRequestEmails;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;

        if (approveAllEventRegistrationRequestsButton != null) {
            approveAllEventRegistrationRequestsButton.setOnClickListener(v -> {
                for (String email : eventRegistrationRequestEmails) {
                    eventRepository.changeEventRegistrationRequestStatus(eventID, email,
                            registrationRequestStatus, RegistrationRequestStatus.APPROVED);
                }
                eventRegistrationRequestEmails.clear();

                notifyDataSetChanged();
            });
        }
    }

    @NonNull
    @Override
    public UserRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserRequestViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.rvitem_userrequest,
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull UserRequestViewHolder holder, int position) {
        // First get the User associated with the event registration request from FB
        User user = userRepository.getUser(eventRegistrationRequestEmails.get(position));

        // Now set the view holder data
        holder.firstName.setText(user.getFirstName());
        holder.lastName.setText(user.getLastName());
        holder.email.setText(user.getEmail());
        holder.address.setText(user.getAddress());
        holder.phoneNumber.setText(user.getPhoneNumber());

        // We set the organization name to be invisible because we know that all of these event
        // registration requests are coming from attendees
        holder.organizationName.setVisibility(View.INVISIBLE);

        // If we are looking at a list of approved event registration requests, we can no longer
        // approve or reject a request, set both buttons to be invisible
        if (registrationRequestStatus.equals(RegistrationRequestStatus.APPROVED)) {
            holder.approveButton.setVisibility(View.INVISIBLE);
            holder.rejectButton.setVisibility(View.INVISIBLE);

        // If we are looking at a list of rejected event registration requests, we can no longer
        // reject a request
        } else if (registrationRequestStatus.equals(RegistrationRequestStatus.REJECTED)) {
            holder.rejectButton.setVisibility(View.INVISIBLE);
        }

        // Set the behaviour for the approve and reject buttons
        holder.approveButton.setOnClickListener(v -> {
            // Modify the status in fb
            eventRepository.changeEventRegistrationRequestStatus(eventID, user.getEmail(),
                    registrationRequestStatus, RegistrationRequestStatus.APPROVED);

            eventRegistrationRequestEmails.remove(position);
            notifyDataSetChanged();
        });
        holder.rejectButton.setOnClickListener(v -> {
            // Modify the status in fb
            eventRepository.changeEventRegistrationRequestStatus(eventID, user.getEmail(),
                    registrationRequestStatus, RegistrationRequestStatus.REJECTED);

            eventRegistrationRequestEmails.remove(position);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return eventRegistrationRequestEmails.size();
    }
}
