package ca.seg2105project.ui.rvcomponents;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ca.seg2105project.R;

public class AttendeeEventInfoOrEventRequestViewHolder extends RecyclerView.ViewHolder{

    TextView eventTitleTV, eventStartTimeTV, eventEndTimeTV, eventLocationTV, eventRequestStatusTV;

    Button requestOrCancelBtn;
    Button viewEventDetailsBtn;

    /**
     * Constructor for EventViewHolder
     * @param attendeeEventInfoOrEventRequestItemView the view inflated from rvitem_event xml layout passed by EventListAdapter
     */
    public AttendeeEventInfoOrEventRequestViewHolder(@NonNull View attendeeEventInfoOrEventRequestItemView) {
        super(attendeeEventInfoOrEventRequestItemView);
        eventTitleTV = attendeeEventInfoOrEventRequestItemView.findViewById(R.id.event_title_tv);
        eventStartTimeTV = attendeeEventInfoOrEventRequestItemView.findViewById(R.id.start_time_tv);
        eventEndTimeTV = attendeeEventInfoOrEventRequestItemView.findViewById(R.id.end_time_tv);
        eventLocationTV = attendeeEventInfoOrEventRequestItemView.findViewById(R.id.event_location_tv);
        eventRequestStatusTV = attendeeEventInfoOrEventRequestItemView.findViewById(R.id.request_status_tv);

        requestOrCancelBtn = attendeeEventInfoOrEventRequestItemView.findViewById(R.id.request_or_cancel_btn);
        viewEventDetailsBtn = attendeeEventInfoOrEventRequestItemView.findViewById(R.id.view_event_details_btn);
    }
}
