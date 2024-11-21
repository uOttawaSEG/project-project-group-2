package ca.seg2105project.ui.rvcomponents;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ca.seg2105project.R;
import ca.seg2105project.ui.activities.EventAttendeePendingRequests;

public class EventRequestViewHolder extends RecyclerView.ViewHolder{

    TextView eventTitleTV, eventStartTimeTV, eventEndTimeTV, eventLocationTV, eventRequestStatusTV;

    // The eventId will be set by the EventListAdapter
    String eventId;
    
    Button requestOrCancelBtn;
    Button viewEventDetailsBtn;

    /**
     * Constructor for EventViewHolder
     * @param eventItemView the view inflated from rvitem_event xml layout passed by EventListAdapter
     */

    public EventRequestViewHolder(@NonNull View eventItemView) {
        super(eventItemView);
        eventTitleTV = eventItemView.findViewById(R.id.event_title_tv);
        eventStartTimeTV = eventItemView.findViewById(R.id.start_time_tv);
        eventEndTimeTV = eventItemView.findViewById(R.id.end_time_tv);
        eventLocationTV = eventItemView.findViewById(R.id.event_location_tv);

        requestOrCancelBtn = eventItemView.findViewById(R.id.request_or_cancel_btn);
        viewEventDetailsBtn = eventItemView.findViewById(R.id.view_event_details_btn);

        //not sure about implementation here
        eventItemView.setOnClickListener(v -> {
            Intent launchEventAttendeePendingRequestActivityIntent = new Intent(eventItemView.getContext(),
                    EventAttendeePendingRequests.class);
            launchEventAttendeePendingRequestActivityIntent.putExtra("event_id", eventId);

            eventItemView.getContext().startActivity(launchEventAttendeePendingRequestActivityIntent);
        });
    }
}
