package ca.seg2105project.ui.rvcomponents;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ca.seg2105project.R;
import ca.seg2105project.ui.activities.EventAttendeePendingRequests;

public class OrganizerEventViewHolder extends RecyclerView.ViewHolder{

    TextView eventTitleTV, eventDescriptionTV, eventDateTV, eventStartTimeTV, eventEndTimeTV, eventAddressTV;

    // The eventId will be set by the EventListAdapter
    String eventId;

    Button deleteEventBtn;

    /**
     * Constructor for EventViewHolder
     * @param eventItemView the view inflated from rvitem_event xml layout passed by EventListAdapter
     */

    public OrganizerEventViewHolder(@NonNull View eventItemView) {
        super(eventItemView);
        eventTitleTV = eventItemView.findViewById(R.id.event_title);
        eventDescriptionTV = eventItemView.findViewById(R.id.event_description);
        eventDateTV = eventItemView.findViewById(R.id.date);
        eventStartTimeTV = eventItemView.findViewById(R.id.startTime);
        eventEndTimeTV = eventItemView.findViewById(R.id.endTime);
        eventAddressTV = eventItemView.findViewById(R.id.eventAddress);
        deleteEventBtn = eventItemView.findViewById(R.id.delete_event_btn);

        // Send user to see list of pending event registration requests
        eventItemView.setOnClickListener(v -> {
            Intent launchEventAttendeePendingRequestActivityIntent = new Intent(eventItemView.getContext(),
                    EventAttendeePendingRequests.class);
            launchEventAttendeePendingRequestActivityIntent.putExtra("event_id", eventId);

            eventItemView.getContext().startActivity(launchEventAttendeePendingRequestActivityIntent);
        });
    }
}
