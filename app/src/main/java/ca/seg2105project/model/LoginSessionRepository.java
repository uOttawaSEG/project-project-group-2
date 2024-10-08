package ca.seg2105project.model;

import android.content.Context;
import android.content.SharedPreferences;

import ca.seg2105project.model.UserRepository;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ca.seg2105project.R;
import ca.seg2105project.model.userClasses.User;

public class LoginSessionRepository {

    public static boolean isLoggedIn(String username, Context applicationContext){
        SharedPreferences sharedPref = applicationContext.getSharedPreferences("session", Context.MODE_PRIVATE);
        if(sharedPref.getString(username, null) == null){
            return false;
        }
        return true;
    }

    public static void loggingIn(String email, Context applicationContext){
        SharedPreferences sharedPref = applicationContext.getSharedPreferences("session", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("email", email);
        editor.apply();
    }


}



