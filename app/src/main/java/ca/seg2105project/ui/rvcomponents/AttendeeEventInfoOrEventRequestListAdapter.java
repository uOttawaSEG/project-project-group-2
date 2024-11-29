package ca.seg2105project.ui.rvcomponents;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;



import java.util.ArrayList;

import ca.seg2105project.R;
import ca.seg2105project.model.eventClasses.Event;

public class AttendeeEventInfoOrEventRequestListAdapter extends
        RecyclerView.Adapter<AttendeeEventInfoOrEventRequestViewHolder> {

    public enum UseCase {
        ATTENDEE_ERR_LIST,
        ATTENDEE_EVENT_SEARCH_LIST
    }

    private final UseCase useCase;

    private final String attendeeEmail;

    private ArrayList<Event> events;

    /**
     *
     * @param useCase the use case for this adapter, either attendee ERR list or attendee event search list
     * @param attendeeEmail the email for the attendee that is viewing event information listed in this adapter
     * @param events When useCase is ATTENDEE_ERR_LIST, this list should be a list of events that the attendee
     *               with email attendeeEmail has requested registration for. When useCase is ATTENDEE_EVENT_SEARCH_LIST,
     *               this list should be a list of events that the attendee with email attendeeEmail has just searched for
     */
    public AttendeeEventInfoOrEventRequestListAdapter(UseCase useCase, String attendeeEmail, ArrayList<Event> events) {
        this.useCase = useCase;
        this.attendeeEmail = attendeeEmail;
        this.events = events;
    }

    @NonNull
    @Override
    public AttendeeEventInfoOrEventRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AttendeeEventInfoOrEventRequestViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.rvitem_attendeeeventinfooreventrequest,
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull AttendeeEventInfoOrEventRequestViewHolder holder, int position) {
        holder.eventTitleTV.setText(events.get(position).getTitle());
        String eventStartDateTime = events.get(position).getLocalDate().toString() + events.get(position).getLocalStartTime().toString();
        holder.eventStartDateTimeTV.setText(eventStartDateTime);
        String eventEndDateTime = events.get(position).getLocalDate().toString() + events.get(position).getLocalEndTime().toString();
        holder.eventEndDateTimeTV.setText(eventEndDateTime);
        holder.eventLocationTV.setText(events.get(position).getEventAddress());

        if (useCase == UseCase.ATTENDEE_ERR_LIST) {
            Event event = events.get(position);
            if (event.getPendingRequests() != null && event.getPendingRequests().containsValue(attendeeEmail)) {
                holder.eventRequestStatusTV.setText("registration: pending");
                holder.requestOrCancelBtn.setText("cancel registration");
                // TODO: Set on click listener here for cancellation with appropriate checks for ability to cancel
            } else if (event.getRejectedRequests() != null && event.getRejectedRequests().containsValue(attendeeEmail)) {
                holder.eventRequestStatusTV.setText("registration: rejected");
                holder.requestOrCancelBtn.setVisibility(View.INVISIBLE);
            } else {
                holder.eventRequestStatusTV.setText("registration: approved");
                holder.requestOrCancelBtn.setText("cancel registration");
                // TODO: Set on click listener here for cancellation with appropriate checks for ability to cancel
            }
        } else if (useCase == UseCase.ATTENDEE_EVENT_SEARCH_LIST) {
            holder.eventRequestStatusTV.setVisibility(View.INVISIBLE);
            holder.requestOrCancelBtn.setText("register");
            // TODO: Set on click listener here for registration with appropriate for ability to register
            // as well as automatic approval if automatic approval is set for this event
        }
        holder.viewEventDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                builder.setTitle("Event Details")
                        .setMessage(
                                "Title: " +
                                        "Start: "  +
                                        "Description: " +
                                        "Date:  "   +
                                        "startTime: "  +
                                        "EndTime: "  +
                                        "eventAddress: "

                        )
                        .setPositiveButton("Close", null);

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    /**
     * Method used in search use case to modify the search results
     * @param newEvents the new set of events
     */
    public void updateEvents(ArrayList<Event> newEvents) {
        events = newEvents;
        notifyDataSetChanged();
    }
}
