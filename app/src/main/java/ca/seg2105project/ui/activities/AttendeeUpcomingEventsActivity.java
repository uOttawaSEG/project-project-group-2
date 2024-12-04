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

public class AttendeeUpcomingEventsActivity extends AppCompatActivity {

    private EAMSApplication eamsApplication;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_attendeeupcomingevents);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        eamsApplication = (EAMSApplication) getApplication();

        setLogoutButtonLogic();
        setSearchForEventsButtonLogic();
        setUpUpcomingEventsRv();
    }

    private void setUpUpcomingEventsRv() {
        LoginSessionRepository loginSessionRepository = eamsApplication.getLoginSessionRepository();
        EventRepository eventRepository = eamsApplication.getEventRepository();

        RecyclerView rv = findViewById(R.id.attendee_request_rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(new AttendeeEventInfoOrEventRequestListAdapter(
                AttendeeEventInfoOrEventRequestListAdapter.UseCase.ATTENDEE_ERR_LIST,
                loginSessionRepository.getActiveLoginSessionEmail(),
                eventRepository.getEventRegistrationRequests(loginSessionRepository.getActiveLoginSessionEmail())));
    }

    private void setSearchForEventsButtonLogic() {
        Button searchForEventsButton = findViewById(R.id.search_for_events_btn);
        searchForEventsButton.setOnClickListener(v -> {
            Intent launchEventSearchActivityIntent = new Intent(this, AttendeeEventSearchActivity.class);
            startActivityForResult(launchEventSearchActivityIntent, 1);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            setUpUpcomingEventsRv();
        }
    }

    private void setLogoutButtonLogic() {
        Button logoutButton = findViewById(R.id.logout_btn);
        logoutButton.setOnClickListener(v -> {
            LoginSessionRepository loginSessionRepository = eamsApplication.getLoginSessionRepository();

            // Removes email from our shared preferences
            loginSessionRepository.endLoginSession();

            // Let user know they are logged out
            Toast.makeText(getApplicationContext(), "Logged out successfully",
                    Toast.LENGTH_LONG).show();

            // Sends user back to login screen
            Intent launchLoginActivityIntent = new Intent(this, LoginActivity.class);
            startActivity(launchLoginActivityIntent);

            // User shouldn't be able to return to this activity
            finish();
        });
    }
}
