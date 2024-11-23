package ca.seg2105project.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

import java.util.ArrayList;

import ca.seg2105project.EAMSApplication;
import ca.seg2105project.R;
import ca.seg2105project.model.registrationRequestClasses.RegistrationRequestStatus;
import ca.seg2105project.model.repositories.EventRepository;
import ca.seg2105project.model.repositories.LoginSessionRepository;
import ca.seg2105project.ui.rvcomponents.EventRegistrationRequestListAdapter;

public class EventAttendeeApprovedRequests extends AppCompatActivity {

    private EAMSApplication eamsApplication;
    private String eventID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_eventattendeeapprovedrequests);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        eamsApplication = (EAMSApplication) getApplication();

        eventID = getIntent().getStringExtra("event_id");

        setPendingRequestsButtonLogic();
        setLogoutButtonLogic();
        setUpApprovedRequestsRv();
    }

    private void setPendingRequestsButtonLogic() {
        Button seePendingEventRequestsButton = findViewById(R.id.see_pending_event_requests_btn);

        seePendingEventRequestsButton.setOnClickListener(v -> {
            Intent eventAttendeePendingRequestsActivityIntent = new Intent(this, EventAttendeePendingRequests.class);

            // Pass along the event id to the pending attendee requests activity
            eventAttendeePendingRequestsActivityIntent.putExtra("event_id", eventID);

            startActivity(eventAttendeePendingRequestsActivityIntent);

            finish();
        });
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

    private void setUpApprovedRequestsRv() {
        EventRepository eventRepository = eamsApplication.getEventRepository();

        RecyclerView approvedRequestsRv = findViewById(R.id.approved_attendee_requests_rv);
        approvedRequestsRv.setLayoutManager(new LinearLayoutManager(this));

        // This is delayed so that the constructor for EventRepository from EAMSApplication has time
        // to run and pull the list of events from fb before we set the list of approved requests
        Runnable setApprovedRvList = new Runnable() {
            @Override
            public void run() {
                approvedRequestsRv.setAdapter(new EventRegistrationRequestListAdapter(eventID,
                        RegistrationRequestStatus.APPROVED, new ArrayList<>(eventRepository.getApprovedEventRequests(eventID)),
                        eventRepository, eamsApplication.getUserRepository(), null));
            }
        };
        Handler h = new Handler();
        h.postDelayed(setApprovedRvList, 1000);
    }
}
