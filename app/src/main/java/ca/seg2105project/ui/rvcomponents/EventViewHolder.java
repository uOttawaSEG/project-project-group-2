package ca.seg2105project.ui.rvcomponents;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ca.seg2105project.R;
import ca.seg2105project.ui.activities.EventAttendeePendingRequest;

public class EventViewHolder extends RecyclerView.ViewHolder{

    TextView eventTitleTV, eventDescriptionTV, eventDateTV, eventStartTimeTV, eventEndTimeTV, eventAddressTV;
    String eventId;

    /**
     * Constructor for EventViewHolder
     * @param eventItemView the view inflated from rvitem_event xml layout passed by EventListAdapter
     */
    public EventViewHolder(@NonNull View eventItemView) {
        super(eventItemView);
        eventTitleTV = eventItemView.findViewById(R.id.event_title);
        eventDescriptionTV = eventItemView.findViewById(R.id.event_description);
        eventDateTV = eventItemView.findViewById(R.id.date);
        eventStartTimeTV = eventItemView.findViewById(R.id.startTime);
        eventEndTimeTV = eventItemView.findViewById(R.id.endTime);
        eventAddressTV = eventItemView.findViewById(R.id.eventAddress);

        // Send user to see list of pending event registration requests
        eventItemView.setOnClickListener(v -> {
            Intent launchEventAttendeePendingRequestActivityIntent = new Intent(eventItemView.getContext(),
                    EventAttendeePendingRequest.class);
            launchEventAttendeePendingRequestActivityIntent.putExtra("event_id", eventId);

            eventItemView.getContext().startActivity(launchEventAttendeePendingRequestActivityIntent);
        });
    }
}
