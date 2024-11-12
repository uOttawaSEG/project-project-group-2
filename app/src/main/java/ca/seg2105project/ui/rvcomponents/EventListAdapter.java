package ca.seg2105project.ui.rvcomponents;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ca.seg2105project.R;
import ca.seg2105project.model.eventClasses.Event;
import ca.seg2105project.model.repositories.EventRepository;

public class EventListAdapter extends RecyclerView.Adapter<EventViewHolder> {

    private final ArrayList<Event> events;
    private final EventRepository eventRepository;

    /**
     * @param events the list of events to be adapted to the viewholders
     */
    public EventListAdapter(ArrayList<Event> events, EventRepository eventRepository) {
        this.events = events;
        this.eventRepository = eventRepository;
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
        holder.eventDateTV.setText(events.get(position).getLocalDate().toString());
        holder.eventStartTimeTV.setText(events.get(position).getLocalStartTime().toString());
        holder.eventEndTimeTV.setText(events.get(position).getLocalEndTime().toString());
        holder.eventAddressTV.setText(events.get(position).getEventAddress());
        holder.eventId = events.get(position).getEventId();

        holder.deleteEventBtn.setOnClickListener(v -> {
            // Remove event from fb
            eventRepository.deleteEvent(events.get(position).getEventId());

            // Remove event from recycler view
            events.remove(position);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }
}