package ca.seg2105project.ui.rvcomponents;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ca.seg2105project.EAMSApplication;
import ca.seg2105project.R;
import ca.seg2105project.model.eventClasses.Event;
import androidx.annotation.RequiresApi;
import ca.seg2105project.model.repositories.EventRepository;
import ca.seg2105project.model.repositories.LoginSessionRepository;
import ca.seg2105project.model.repositories.UserRepository;

@RequiresApi(api = Build.VERSION_CODES.O)
public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ViewHolder> {

    private ArrayList<Event> events;
    private EventRepository eventRepository;

    /**
     * @param events the list of events to be adapted to the viewholders
     */
    public EventListAdapter(ArrayList<Event> events) {
        this.events = events;
        this.eventRepository = new EventRepository();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.rvitem_event,
                parent,
                false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.eventTitleTV.setText(events.get(position).getTitle());
        holder.eventDescriptionTV.setText(events.get(position).getDescription());
        holder.eventDateTV.setText(events.get(position).getLocalDate().toString());
        holder.eventStartTimeTV.setText(events.get(position).getLocalStartTime().toString());
        holder.eventEndTimeTV.setText(events.get(position).getLocalEndTime().toString());
        holder.eventAddressTV.setText(events.get(position).getEventAddress());
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView eventTitleTV;
        TextView eventDescriptionTV;
        TextView eventDateTV;
        TextView eventStartTimeTV;
        TextView eventEndTimeTV;
        TextView eventAddressTV;
        Button deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            eventTitleTV = itemView.findViewById(R.id.event_title);
            eventDescriptionTV = itemView.findViewById(R.id.event_description);
            eventDateTV = itemView.findViewById(R.id.date);
            eventStartTimeTV = itemView.findViewById(R.id.startTime);
            eventEndTimeTV = itemView.findViewById(R.id.endTime);
            eventAddressTV = itemView.findViewById(R.id.eventAddress);
            deleteButton = itemView.findViewById(R.id.delete_event_btn);

            EAMSApplication eamsApplication = (EAMSApplication) itemView.getContext().getApplicationContext();
            LoginSessionRepository loginSessionRepository = eamsApplication.getLoginSessionRepository();
            UserRepository userRepository = eamsApplication.getUserRepository();
            String organizer = loginSessionRepository.getActiveLoginSessionEmail();

            deleteButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Event eventToDelete = events.get(position);
                    if(organizer.equals(eventToDelete.getOrganizerEmail())) {
                        eventRepository.deleteEvent(eventToDelete.getEventID());
                        events.remove(position);
                        notifyItemRemoved(position);
                    }
                    else{
                        Toast.makeText(eamsApplication, "Cannot Delete Another Organizer's Event",
                                Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}
