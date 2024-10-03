package ca.seg2105project.model.Testers;

import ca.seg2105project.R;
import ca.seg2105project.model.Authenticator;

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

public class AuthenTester extends AppCompatActivity  {

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
        String s = "";
        boolean answer;

        answer = Authenticator.authenticate("", ""); //true
        s+= answer;

        //Test for existing accounts (and added mispellings for it to return false)
        answer = Authenticator.authenticate("jensenlarge.isaac@gmail.com", "awesomepassword"); //true
        s+= answer+"\n";
        answer = Authenticator.authenticate("isaac@gmail.com", "awesomepassword"); //false
        s+= answer+"\n";
        answer = Authenticator.authenticate("jensenlarge.isaac@gmail.com", "THE BEST PASSWORD EVER"); //false
        s+= answer+"\n";

        answer = Authenticator.authenticate("ronisemail@gmail.com", "epicpassword"); //true
        s+= answer+"\n";
        answer = Authenticator.authenticate("roniemail@gmail.com", "epicpassword"); //false
        s+= answer+"\n";
        answer = Authenticator.authenticate("ronisemail@gmail.com", "coolpassword"); //false
        s+= answer+"\n";

        answer = Authenticator.authenticate("admin@gmail.com", "adminpwd"); //true
        s+= answer+"\n";
        answer = Authenticator.authenticate("administrator@gmail.com", "adminpwd"); //false
        s+= answer+"\n";
        answer = Authenticator.authenticate("admin@gmail.com", "adminpassword"); //false
        s+= answer+"\n";

        answer = Authenticator.authenticate("rluo123@gmail.com", "walkingIsOverrated"); //true
        s+= answer+"\n";
        answer = Authenticator.authenticate("rluo123@gmail.com", "walkingIsNotOverrated"); //false
        s+= answer+"\n";
        answer = Authenticator.authenticate("rluo12@gmail.com", "walkingIsOverrated"); //false
        s+= answer+"\n";

        answer = Authenticator.authenticate("kdeotare@gmail.com", "the_best_pass"); //true
        s+= answer+"\n";
        answer = Authenticator.authenticate("kdeotare@gmail.com", "THE_best_pass"); //false
        s+= answer+"\n";
        answer = Authenticator.authenticate("kunaladeotare@gmail.com", "the_best_pass"); //false
        s+= answer+"\n";

        answer = Authenticator.authenticate("shawn@gmail.com", "secure_pass"); //true
        s+= answer+"\n";
        answer = Authenticator.authenticate("shawn@gmail.com", "not_secure_pass"); //false
        s+= answer+"\n";
        answer = Authenticator.authenticate("shawnH@gmail.com", "secure_pass"); //false
        s+= answer+"\n";

        //Test for non-existing accounts (should always be false)
        answer = Authenticator.authenticate("JohnDoe@gmail.com", "anonPassword0"); //false
        s+= answer+"\n";
        answer = Authenticator.authenticate("BobRoss@gmail.com", "proAt Painting"); //false
        s+= answer+"\n";
        answer = Authenticator.authenticate("hussein.alosman@uottawa.ca", "the most amazing password ever"); //false
        s+= answer+"\n";

        loginTextView.setText(s);
    }


}
