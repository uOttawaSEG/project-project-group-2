package ca.seg2105project.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ca.seg2105project.EAMSApplication;
import ca.seg2105project.R;
import ca.seg2105project.model.repositories.EventRepository;
import ca.seg2105project.model.repositories.LoginSessionRepository;
import ca.seg2105project.ui.rvcomponents.AttendeeEventInfoOrEventRequestListAdapter;

public class AttendeeEventSearchActivity extends AppCompatActivity {

    private EAMSApplication eamsApplication;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_attendeesearchevents);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        eamsApplication = (EAMSApplication) getApplication();

        setUpEventSearchResultsRv();
    }

    private void setUpEventSearchResultsRv() {
        LoginSessionRepository loginSessionRepository = eamsApplication.getLoginSessionRepository();
        EventRepository eventRepository = eamsApplication.getEventRepository();

        RecyclerView rv = findViewById(R.id.event_search_results_rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(new AttendeeEventInfoOrEventRequestListAdapter(
                AttendeeEventInfoOrEventRequestListAdapter.UseCase.ATTENDEE_EVENT_SEARCH_LIST,
                loginSessionRepository.getActiveLoginSessionEmail(),
                eventRepository.mockGetListOfEventsWithERRsFromAttendee(loginSessionRepository.getActiveLoginSessionEmail())));
    }
}
