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

import ca.seg2105project.EAMSApplication;
import ca.seg2105project.R;
import ca.seg2105project.model.repositories.LoginSessionRepository;

public class EventAttendeeApprovedRequests extends AppCompatActivity {

    private EAMSApplication eamsApplication;

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

        Toast.makeText(this, getIntent().getStringExtra("event_id"), Toast.LENGTH_LONG).show();

        setPendingRequestsButtonLogic();
        setLogoutButtonLogic();
    }

    private void setPendingRequestsButtonLogic() {
        Button seePendingEventRequestsButton = findViewById(R.id.see_pending_event_requests_btn);

        seePendingEventRequestsButton.setOnClickListener(v -> {
            Intent eventAttendeePendingRequestsActivityIntent = new Intent(this, EventAttendeePendingRequests.class);

            // Pass along the event id to the pending attendee requests activity
            eventAttendeePendingRequestsActivityIntent.putExtra("event_id", getIntent().getStringExtra("event_id"));

            startActivity(eventAttendeePendingRequestsActivityIntent);

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
