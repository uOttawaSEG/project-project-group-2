package ca.seg2105project.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import ca.seg2105project.EAMSApplication;
import ca.seg2105project.R;
import ca.seg2105project.model.repositories.LoginSessionRepository;
import ca.seg2105project.model.repositories.UserRepository;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_welcome);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EAMSApplication eamsApplication = (EAMSApplication) getApplication();
        LoginSessionRepository loginSessionRepository = eamsApplication.getLoginSessionRepository();
        UserRepository userRepository = eamsApplication.getUserRepository(); 

        // All of this needs to be on a delayed thread because we have to wait for the user repository to load the list of users
        Runnable setWelcomePageUserSpecificContent = new Runnable() {
            @Override
            public void run() {
                TextView welcomeMessageTV = findViewById(R.id.welcome_message_tv);
                String welcomeMessage = "Welcome! You are logged in as " +
                        userRepository.getUserTypeByEmail(loginSessionRepository.getActiveLoginSessionEmail());
                welcomeMessageTV.setText(welcomeMessage);

                // Set up 'go to account request inbox' button if the user is admin
                if (userRepository.getUserTypeByEmail(loginSessionRepository.getActiveLoginSessionEmail()).equals("Administrator")) {
                    Button goToRequestInboxBtn = findViewById(R.id.launch_user_specific_activity_btn);
                    goToRequestInboxBtn.setText(R.string.admin_welcome_screen_launch_account_request_inbox_button_text);
                    goToRequestInboxBtn.setVisibility(View.VISIBLE);
                    goToRequestInboxBtn.setOnClickListener(v -> {
                        Intent launchPendingRequestsActivityIntent = new Intent(WelcomeActivity.this, PendingAccountRequestsActivity.class);
                        startActivity(launchPendingRequestsActivityIntent);

                        finish();
                    });
                // Set up 'Go to upcoming events' button if the user is organizer
                } else if (userRepository.getUserTypeByEmail(loginSessionRepository.getActiveLoginSessionEmail()).equals("Organizer")) {
                    Button goToRequestInboxBtn = findViewById(R.id.launch_user_specific_activity_btn);
                    goToRequestInboxBtn.setText(R.string.organizer_welcome_screen_launch_account_request_inbox_button_text);
                    goToRequestInboxBtn.setVisibility(View.VISIBLE);
                    goToRequestInboxBtn.setOnClickListener(v -> {
                        Intent launchOrganizerUpcomingEventsActivityIntent = new Intent(
                                WelcomeActivity.this, OrganizerUpcomingEventsActivity.class);
                        startActivity(launchOrganizerUpcomingEventsActivityIntent);

                        finish();
                    });
                }
                //Setup to go the AttendeeUpcomingEvents page if user is an Attendee
                else if (userRepository.getUserTypeByEmail(loginSessionRepository.getActiveLoginSessionEmail()).equals("Attendee")) {
                    Button goToRequestInboxBtn = findViewById(R.id.launch_user_specific_activity_btn);
                    goToRequestInboxBtn.setText(R.string.organizer_welcome_screen_launch_account_request_inbox_button_text);
                    goToRequestInboxBtn.setVisibility(View.VISIBLE);
                    goToRequestInboxBtn.setOnClickListener(v -> {
                        Intent launchAttendeeUpcomingEventsActivityIntent = new Intent(
                                WelcomeActivity.this, AttendeeUpcomingEventsActivity.class);
                        startActivity(launchAttendeeUpcomingEventsActivityIntent);

                        finish();
                    });
                }

            }
        };

        Handler h = new Handler();
        h.postDelayed(setWelcomePageUserSpecificContent, 1500);

        Button logoutButton = findViewById(R.id.logout_btn);
        logoutButton.setOnClickListener(v -> {
            // Removes email from our shared preferences
            loginSessionRepository.endLoginSession();

            // Let user know they are logged out
            Toast.makeText(getApplicationContext(), "Logged out successfully",
                    Toast.LENGTH_LONG).show();

            // Sends user back to login screen
            Intent launchLoginActivityIntent = new Intent(this, LoginActivity.class);
            startActivity(launchLoginActivityIntent);

            // User shouldn't be able to return to this instance of WelcomeActivity
            finish();
        });
    }
}
