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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!LoginSessionRepository.hasActiveLoginSession(getApplicationContext())){
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_login);
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });

            setLoginViewLogic();
            setRegistrationLinkLogic();
        } else {
            launchWelcomeActivity();
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

            // TODO: check for empty email or password and check email format here

            if (UserRepository.authenticate(email, password)) {
                LoginSessionRepository.startLoginSession(email, getApplicationContext());
                Toast.makeText(getApplicationContext(), "Logging In", Toast.LENGTH_LONG).show();
                launchWelcomeActivity();
            }
            else{
                Toast.makeText(getApplicationContext(), "No account exists for that email or password is incorrect", Toast.LENGTH_LONG).show();
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

    private void launchWelcomeActivity() {
        Intent launchWelcomeActivityIntent = new Intent(this, WelcomeActivity.class);
        // TODO: make sure that the user can't get back to the login screen after going to welcome screen
        startActivity(launchWelcomeActivityIntent);
    }
}