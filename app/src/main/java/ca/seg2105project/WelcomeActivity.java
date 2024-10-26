package ca.seg2105project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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

        TextView welcomeMessageTV = findViewById(R.id.welcome_message_tv);
        String welcomeMessage = "Welcome! You are logged in as " +
                userRepository.getUserTypeByEmail(loginSessionRepository.getActiveLoginSessionEmail());
        welcomeMessageTV.setText(welcomeMessage);

        // Set up 'go to account request inbox' button if the user is admin
        if (userRepository.getUserTypeByEmail(loginSessionRepository.getActiveLoginSessionEmail()).equals("Administrator")) {
            Button goToRequestInboxBtn = findViewById(R.id.admin_pending_requests_inbox_btn);
            goToRequestInboxBtn.setVisibility(View.VISIBLE);
            goToRequestInboxBtn.setOnClickListener(v -> {
                Intent launchPendingRequestsActivityIntent = new Intent(this, PendingRequestsActivity.class);
                startActivity(launchPendingRequestsActivityIntent);
            });
        }

        Button logoutButton = findViewById(R.id.Logout_BTN);
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
    }}
