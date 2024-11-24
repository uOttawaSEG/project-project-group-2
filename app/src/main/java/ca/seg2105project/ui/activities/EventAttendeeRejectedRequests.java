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
import ca.seg2105project.ui.rvcomponents.OrganizerERRListAdapter;

public class EventAttendeeRejectedRequests extends AppCompatActivity {

    private EAMSApplication eamsApplication;
    private String eventID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_eventattendeerejectedrequest);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        eamsApplication = (EAMSApplication) getApplication();

        eventID = getIntent().getStringExtra("event_id");

        setApprovedRequestsButtonLogic();
        setLogoutButtonLogic();
        setUpRejectedRequestsRv();
    }

    private void setApprovedRequestsButtonLogic() {
        Button seeApprovedEventRequestsButton = findViewById(R.id.see_approved_event_requests_btn);

        seeApprovedEventRequestsButton.setOnClickListener(v -> {
            Intent eventAttendeeApprovedRequestsActivityIntent = new Intent(this, EventAttendeeApprovedRequests.class);

            // Pass along the event id to the rejected attendee requests activity
            eventAttendeeApprovedRequestsActivityIntent.putExtra("event_id", eventID);

            startActivity(eventAttendeeApprovedRequestsActivityIntent);

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

    private void setUpRejectedRequestsRv() {
        EventRepository eventRepository = eamsApplication.getEventRepository();

        RecyclerView rejectedRequestsRv = findViewById(R.id.rejected_attendee_requests_rv);
        rejectedRequestsRv.setLayoutManager(new LinearLayoutManager(this));

        // This is delayed so that the constructor for EventRepository from EAMSApplication has time
        // to run and pull the list of events from fb before we set the list of rejected requests
        Runnable setRejectedRvList = new Runnable() {
            @Override
            public void run() {
                rejectedRequestsRv.setAdapter(new OrganizerERRListAdapter(eventID,
                        RegistrationRequestStatus.REJECTED, new ArrayList<>(eventRepository.getRejectedEventRequests(eventID)),
                        eventRepository, eamsApplication.getUserRepository(), findViewById(R.id.approve_all_request_btn)));
            }
        };
        Handler h = new Handler();
        h.postDelayed(setRejectedRvList, 1000);
    }
}
