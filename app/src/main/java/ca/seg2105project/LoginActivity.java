package ca.seg2105project;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

import ca.seg2105project.model.UserRepository;
import ca.seg2105project.model.userClasses.User;

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

        final TextView loginTextView = findViewById(R.id.logintextview);
        List<User> allRegisteredUsers = UserRepository.getAllRegisteredUsers();
        String testText = allRegisteredUsers.get(0).getFirstName() + " " +
                allRegisteredUsers.get(2).getPassword();
        loginTextView.setText(testText);

        /*
        //Additional test cases I added before I approve of PR (Rachel)
        //It works!

        StringBuilder s = new StringBuilder(" ");
        User cur = null;
        for(int i = 0; i<allRegisteredUsers.size(); i++) {
            cur = allRegisteredUsers.get(i);
            s.append(cur.toString(true));
        }

        loginTextView.setText(s.toString());


         */
    }
}