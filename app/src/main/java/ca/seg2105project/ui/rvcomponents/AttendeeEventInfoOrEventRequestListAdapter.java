package ca.seg2105project.ui.rvcomponents;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ca.seg2105project.R;
import ca.seg2105project.model.repositories.EventRepository;


public class AttendeeEventInfoOrEventRequestListAdapter extends
        RecyclerView.Adapter<AttendeeEventInfoOrEventRequestViewHolder> {

    public enum UseCase {
        ATTENDEE_ERR_LIST,
        ATTENDEE_EVENT_SEARCH_LIST
    }

    private final UseCase useCase;

    private final String attendeeEmail;

    private final EventRepository eventRepository;

    /**
     *
     * @param useCase the use case for this adapter, either attendee ERR list or attendee event search list
     * @param attendeeEmail the email for the attendee that is viewing event information listed in this adapter
     * @param eventRepository repository for all event info
     */
    public AttendeeEventInfoOrEventRequestListAdapter(UseCase useCase, String attendeeEmail, EventRepository eventRepository) {
        this.useCase = useCase;
        this.attendeeEmail = attendeeEmail;
        this.eventRepository = eventRepository;
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

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
