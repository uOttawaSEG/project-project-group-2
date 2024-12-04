package ca.seg2105project.ui.rvcomponents;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ca.seg2105project.R;
import ca.seg2105project.model.eventClasses.Event;
import ca.seg2105project.model.repositories.EventRepository;

public class OrganizerEventListAdapter extends RecyclerView.Adapter<OrganizerEventViewHolder> {

    private final ArrayList<Event> events;
    private final EventRepository eventRepository;

    /**
     * @param events the list of events to be adapted to the viewholders
     */
    public OrganizerEventListAdapter(ArrayList<Event> events, EventRepository eventRepository) {
        this.events = events;
        this.eventRepository = eventRepository;
    }

    @NonNull
    @Override
    public OrganizerEventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrganizerEventViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.rvitem_event,
                parent,
                false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrganizerEventViewHolder holder, int position) {
        holder.eventTitleTV.setText(events.get(position).getTitle());
        holder.eventDescriptionTV.setText(events.get(position).getDescription());
        holder.eventDateTV.setText(events.get(position).getLocalDate().toString());
        holder.eventStartTimeTV.setText(events.get(position).getLocalStartTime().toString());
        holder.eventEndTimeTV.setText(events.get(position).getLocalEndTime().toString());
        holder.eventAddressTV.setText(events.get(position).getEventAddress());
        holder.eventId = events.get(position).getEventID();

        holder.deleteEventBtn.setOnClickListener(v -> {
            // We use the event ID to check whether the event can be deleted because
            // the Event object in our events list in this adapter may not reflect the current
            // state of that event in fb. When using the eventID though we will always be checking
            // against what is in fb.
            if (!eventRepository.canDeleteEvent(events.get(position).getEventID())) {
                Toast.makeText(holder.itemView.getContext(),
                        "Can't delete an event with one or more approved registrations", Toast.LENGTH_LONG).show();
            } else {
                // Remove event from fb
                eventRepository.deleteEvent(events.get(position).getEventID());

                // Remove event from recycler view
                events.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }
}