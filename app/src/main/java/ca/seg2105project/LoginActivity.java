package ca.seg2105project;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import ca.seg2105project.model.LoginSessionRepository;

public class LoginActivity extends AppCompatActivity {

    EditText editEmail, editPassword;
    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPref = getSharedPreferences("session", Context.MODE_PRIVATE);

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        if((sharedPref.getString("email", null) == null)){
            setContentView(R.layout.activity_login);

            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });

            editEmail = findViewById(R.id.email);
            editPassword = findViewById(R.id.password);
            loginButton = findViewById(R.id.loginBTN);

            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String email, password;
                    email = String.valueOf(editEmail.getText());
                    password = String.valueOf(editPassword.getText());
                    SharedPreferences.Editor editor = sharedPref.edit();

                    if (LoginSessionRepository.loginSession(email, password, getApplicationContext())) {
                        editor.putString("email", email);
                        editor.apply();
                        setContentView(R.layout.activity_register);
                    }
                }
            });
        }
        else{
            setContentView(R.layout.activity_register);
        }
    }
}