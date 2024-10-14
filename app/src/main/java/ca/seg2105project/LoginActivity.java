package ca.seg2105project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import ca.seg2105project.model.LoginSessionRepository;
import ca.seg2105project.model.UserRepository;

public class LoginActivity extends AppCompatActivity {

    private LoginSessionRepository loginSessionRepository;
    private EAMSApplication eamsApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        eamsApplication = (EAMSApplication) getApplication();
        loginSessionRepository = eamsApplication.getLoginSessionRepository();

        if (!loginSessionRepository.hasActiveLoginSession()) {
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_login);
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });

            // Set up view logic
            setLoginViewLogic();
            setRegistrationLinkLogic();
        } else {
            launchWelcomeActivityAndFinish();
        }
    }

    // below is view logic setup

    private void setLoginViewLogic() {
        EditText editEmail = findViewById(R.id.email);
        EditText editPassword = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.loginBTN);

        loginButton.setOnClickListener(v -> {
            String email, password;
            email = String.valueOf(editEmail.getText());
            password = String.valueOf(editPassword.getText());

            // TODO: add email format check here
            UserRepository userRepository = eamsApplication.getUserRepository();

            if (userRepository.authenticate(email, password)) {
                loginSessionRepository.startLoginSession(email);
                Toast.makeText(getApplicationContext(), "Logged in successfully", Toast.LENGTH_LONG).show();
                launchWelcomeActivityAndFinish();
            }
            else {
                Toast.makeText(getApplicationContext(), "Either there isn't an account " +
                        "associated with that email or the password is incorrect", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setRegistrationLinkLogic() {
        TextView registrationTextView = findViewById(R.id.create_accountBTN);

        registrationTextView.setOnClickListener(v -> {
            Intent launchRegisterActivityIntent = new Intent(this, RegisterActivity.class);
            startActivity(launchRegisterActivityIntent);
        });
    }

    private void launchWelcomeActivityAndFinish() {
        Intent launchWelcomeActivityIntent = new Intent(this, WelcomeActivity.class);
        // TODO: make sure that the user can't get back to the login screen after going to welcome screen
        startActivity(launchWelcomeActivityIntent);
        finish();
    }
}