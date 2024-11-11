package ca.seg2105project.ui.rvcomponents;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ca.seg2105project.R;

public class EventViewHolder extends RecyclerView.ViewHolder{

    TextView eventTitleTV, eventDescriptionTV, eventDateTV, eventStartTimeTV, eventEndTimeTV, eventAddressTV;

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
    }
}
