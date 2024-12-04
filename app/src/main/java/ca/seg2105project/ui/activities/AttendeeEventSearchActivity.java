package ca.seg2105project.ui.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

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

        setUpRvAndSearchButtonBehaviour();
    }

    private void setUpRvAndSearchButtonBehaviour() {
        LoginSessionRepository loginSessionRepository = eamsApplication.getLoginSessionRepository();
        EventRepository eventRepository = eamsApplication.getEventRepository();

        RecyclerView rv = findViewById(R.id.event_search_results_rv);
        rv.setLayoutManager(new LinearLayoutManager(this));

        AttendeeEventInfoOrEventRequestListAdapter adapter = new AttendeeEventInfoOrEventRequestListAdapter(
                AttendeeEventInfoOrEventRequestListAdapter.UseCase.ATTENDEE_EVENT_SEARCH_LIST,
                loginSessionRepository.getActiveLoginSessionEmail(),
                new ArrayList<>(),
                eventRepository);

        rv.setAdapter(adapter);

        Button searchButton = findViewById(R.id.search_btn);
        searchButton.setOnClickListener(v -> {
            EditText searchEditText = findViewById(R.id.search_et);
            String enteredKeyword = searchEditText.getText().toString();

            if (enteredKeyword.isEmpty()) {
                Toast.makeText(this, "Please enter a keyword to search for", Toast.LENGTH_LONG).show();
            } else {
                adapter.updateEvents(eventRepository.getEventsByKeyword(enteredKeyword, loginSessionRepository.getActiveLoginSessionEmail()));
            }
        });
    }
}
