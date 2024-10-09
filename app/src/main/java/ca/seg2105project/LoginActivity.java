package ca.seg2105project;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if(!LoginSessionRepository.isLoggedIn("email", getApplicationContext())){
            EditText editEmail = findViewById(R.id.email);
            EditText editPassword = findViewById(R.id.password);
            Button loginButton = findViewById(R.id.loginBTN);

            loginButton.setOnClickListener(v -> {
                String email, password;
                email = String.valueOf(editEmail.getText());
                password = String.valueOf(editPassword.getText());

                if (UserRepository.authenticate(email, password)) {
                    LoginSessionRepository.login(email, getApplicationContext());
                    Toast.makeText(getApplicationContext(), "Logging In", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "No account exists for that email or password is incorrect", Toast.LENGTH_LONG).show();
                }
            });
        }
        else{
            Toast.makeText(this, "Logged in as ", Toast.LENGTH_LONG).show();
        }
    }
}