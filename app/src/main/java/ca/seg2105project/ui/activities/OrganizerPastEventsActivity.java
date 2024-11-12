package ca.seg2105project.ui.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
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
import java.util.List;

import ca.seg2105project.EAMSApplication;
import ca.seg2105project.R;
import ca.seg2105project.model.eventClasses.Event;
import ca.seg2105project.model.registrationRequestClasses.AccountRegistrationRequest;
import ca.seg2105project.model.registrationRequestClasses.AccountRegistrationRequestStatus;
import ca.seg2105project.model.repositories.EventRepository;
import ca.seg2105project.model.repositories.LoginSessionRepository;
import ca.seg2105project.ui.rvcomponents.AccountRegistrationRequestListAdapter;
import ca.seg2105project.ui.rvcomponents.EventListAdapter;

public class OrganizerPastEventsActivity extends AppCompatActivity {

    private EAMSApplication eamsApplication;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_organiserpastevents);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        eamsApplication = (EAMSApplication) getApplication();
        EventRepository eventRepository = new EventRepository();

        RecyclerView pastEventsRV = findViewById(R.id.past_events_rv);
        pastEventsRV.setLayoutManager(new LinearLayoutManager(this));
        ArrayList<Event> events = eventRepository.getAllPastEvents();

        setSeeUpcomingEventsButtonLogic();
        setLogoutButtonLogic();
        Runnable setPendingRvList = new Runnable() {
            @Override
            public void run() {
                pastEventsRV.setAdapter(new EventListAdapter(events, eventRepository));
            }
        };
        Handler h = new Handler();
        h.postDelayed(setPendingRvList, 1000);
    }

    private void setSeeUpcomingEventsButtonLogic() {
        Button seeOrganizerPastEventsBTN = findViewById(R.id. see_upcoming_btn);
        seeOrganizerPastEventsBTN.setOnClickListener(v -> {
            Intent launchUpcomingEventsActivityIntent = new Intent(this, OrganizerUpcomingEventsActivity.class);
            startActivity(launchUpcomingEventsActivityIntent);

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
