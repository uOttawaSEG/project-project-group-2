package ca.seg2105project.ui.rvcomponents;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ca.seg2105project.R;
import ca.seg2105project.model.eventClasses.Event;

public class EventListAdapter extends RecyclerView.Adapter<EventViewHolder> {

    private ArrayList<Event> events;

    /**
     * @param events the list of events to be adapted to the viewholders
     */
    public EventListAdapter(ArrayList<Event> events) {
        this.events = events;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EventViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.rvitem_event,
                parent,
                false));
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        holder.eventTitleTV.setText(events.get(position).getTitle());
        holder.eventDescriptionTV.setText(events.get(position).getDescription());
        holder.eventDateTV.setText(events.get(position).getDate().toString());
        holder.eventStartTimeTV.setText(events.get(position).getStartTime().toString());
        holder.eventEndTimeTV.setText(events.get(position).getEndTime().toString());
        holder.eventAddressTV.setText(events.get(position).getEventAddress());
    }

    @Override
    public int getItemCount() {
        return events.size();
    }
}
