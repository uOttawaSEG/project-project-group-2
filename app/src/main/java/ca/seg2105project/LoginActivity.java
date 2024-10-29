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

import java.util.List;

import ca.seg2105project.model.registrationRequestClasses.AccountRegistrationRequest;
import ca.seg2105project.model.registrationRequestClasses.AccountRegistrationRequestStatus;
import ca.seg2105project.model.repositories.AccountRegistrationRequestRepository;
import ca.seg2105project.model.repositories.LoginSessionRepository;
import ca.seg2105project.model.repositories.UserRepository;

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

        UserRepository userRepository = eamsApplication.getUserRepository();
        AccountRegistrationRequestRepository accountRegistrationRequestRepository = eamsApplication.getAccountRegistrationRequestRepository();
        List<AccountRegistrationRequest> allAccountRegistrationRequests = accountRegistrationRequestRepository.readRequests();

        loginButton.setOnClickListener(v -> {
            String enteredEmail, enteredPassword;
            enteredEmail = String.valueOf(editEmail.getText());
            enteredPassword = String.valueOf(editPassword.getText());

            // TODO: check for empty email or password and check email format here

            if (userRepository.authenticate(enteredEmail, enteredPassword)) {
                loginSessionRepository.startLoginSession(enteredEmail);
                Toast.makeText(getApplicationContext(), "Logging in", Toast.LENGTH_LONG).show();
                launchWelcomeActivityAndFinish();
            } else {

                // We check to see if the email the user entered has an existing account
                // registration request associated with it
                boolean hasAccountRegistrationRequestWithEmail = false;
                int i = 0;
                while (!hasAccountRegistrationRequestWithEmail && i < allAccountRegistrationRequests.size()) {
                    if (allAccountRegistrationRequests.get(i).getEmail().equals(enteredEmail)) {
                        hasAccountRegistrationRequestWithEmail = true;
                    } else {
                        i++;
                    }
                }
                if (hasAccountRegistrationRequestWithEmail) {
                    // We know we found an existing account registration request

                    if (allAccountRegistrationRequests.get(i).getStatus().equals(AccountRegistrationRequestStatus.PENDING)) {
                        // User has a pending account registration request
                        Toast.makeText(this, "Account registration approval pending, please try logging in again soon for updates", Toast.LENGTH_LONG).show();
                    } else {
                        // User has a rejected account registration request
                        Toast.makeText(this, "Your account registration request has unfortunately been rejected, please contact our administrator at 6133333333 for more info", Toast.LENGTH_LONG).show();
                    }
                } else {
                    // The entered credentials failed to authenticate and we couldn't find an
                    // existing account registration request so either they got their password wrong
                    // or they haven't made a registration request
                    Toast.makeText(getApplicationContext(), "No account exists for that email or " +
                            "password is incorrect", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void setRegistrationLinkLogic() {
        TextView registrationTextView = findViewById(R.id.create_accountBTN);

        registrationTextView.setOnClickListener(v -> {
            Intent launchRegisterActivityIntent = new Intent(this, RegisterActivity.class);
            startActivity(launchRegisterActivityIntent);

            // Don't finish the activity here because the user will want to return to this LoginActivity instance
        });
    }

    private void launchWelcomeActivityAndFinish() {
        Intent launchWelcomeActivityIntent = new Intent(this, WelcomeActivity.class);
        startActivity(launchWelcomeActivityIntent);

        // Login is done, so finish this instance of LoginActivity
        finish();
    }
}