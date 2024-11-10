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

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;

import ca.seg2105project.EAMSApplication;
import ca.seg2105project.R;
import ca.seg2105project.model.eventClasses.Event;
import ca.seg2105project.model.repositories.LoginSessionRepository;
import ca.seg2105project.ui.rvcomponents.EventListAdapter;

public class OrganizerUpcomingEventsActivity extends AppCompatActivity {

    private EAMSApplication eamsApplication;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_organiserupcomingevents);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        eamsApplication = (EAMSApplication) getApplication();

        RecyclerView upcomingEventsRV = findViewById(R.id.upcoming_events_rv);
        upcomingEventsRV.setLayoutManager(new LinearLayoutManager(this));
        ArrayList<Event> events = new ArrayList<>();
        events.add(new Event(
                "id1",
                "Event 1",
                "First Event",
                LocalDate.of(2024, Month.JANUARY, 1),
                LocalTime.NOON,
                LocalTime.MIDNIGHT,
                "Event 1 Address",
                "org1@gmail.com",
                false));

        events.add(new Event(
                "id2",
                "Event 2",
                "Second Event",
                LocalDate.of(2024, Month.JANUARY, 1),
                LocalTime.NOON,
                LocalTime.MIDNIGHT,
                "Event 2 Address",
                "org2@gmail.com",
                false));

        events.add(new Event(
                "id3",
                "Event 3",
                "Third Event",
                LocalDate.of(2024, Month.JANUARY, 1),
                LocalTime.NOON,
                LocalTime.MIDNIGHT,
                "Event 3 Address",
                "org3@gmail.com",
                false));

        events.add(new Event(
                "id4",
                "Event 4",
                "Fourth Event",
                LocalDate.of(2024, Month.JANUARY, 1),
                LocalTime.NOON,
                LocalTime.MIDNIGHT,
                "Event 4 Address",
                "org4@gmail.com",
                false));

        events.add(new Event(
                "id5",
                "Event 5",
                "Fifth Event",
                LocalDate.of(2024, Month.JANUARY, 1),
                LocalTime.NOON,
                LocalTime.MIDNIGHT,
                "Event 5 Address",
                "org5@gmail.com",
                false));

        events.add(new Event(
                "id6",
                "Event 6",
                "Sixth Event",
                LocalDate.of(2024, Month.JANUARY, 1),
                LocalTime.NOON,
                LocalTime.MIDNIGHT,
                "Event 6 Address",
                "org6@gmail.com",
                false));

        // Shawn once you remove the hardcoded events above and set events variable to just be the result
        // of a call to EventRepository, move this .setAdapter line below to being inside a 1sec delayed runnable
        // similar to how it's done in PendingAccountRequestsActivity
        upcomingEventsRV.setAdapter(new EventListAdapter(events));

        setSeePastEventsButtonLogic();
        setLogoutButtonLogic();
    }

    private void setSeePastEventsButtonLogic() {
        Button seeOrganizerPastEventsBTN = findViewById(R.id. see_past_events_btn);
        seeOrganizerPastEventsBTN.setOnClickListener(v -> {
            Intent launchPastEventsActivityIntent = new Intent(this, OrganizerPastEventsActivity.class);
            startActivity(launchPastEventsActivityIntent);

            // Close this instance of OrganizerUpcomingEventsActivity in case user logs off. They shouldn't be able to back-navigate
            // to this activity
            finish();
        });
    }

    private void setLogoutButtonLogic() {
        Button logoutButton = findViewById(R.id.Logout_BTN);
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