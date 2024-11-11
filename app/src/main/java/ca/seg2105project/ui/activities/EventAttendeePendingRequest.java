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

public class EventAttendeePendingRequest extends AppCompatActivity {

    private EAMSApplication eamsApplication;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_eventattendeependingrequest);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        eamsApplication = (EAMSApplication) getApplication();

        Toast.makeText(this, getIntent().getStringExtra("event_id"), Toast.LENGTH_LONG).show();

        setLogoutButtonLogic();
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
