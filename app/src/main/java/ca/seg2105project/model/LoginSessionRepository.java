package ca.seg2105project.model;

import android.content.Context;
import android.content.SharedPreferences;

public class LoginSessionRepository {

    public static boolean isLoggedIn(String username, Context applicationContext){
        SharedPreferences sharedPref = applicationContext.getSharedPreferences("session", Context.MODE_PRIVATE);
        if(sharedPref.getString(username, null) == null){
            return false;
        }
        return true;
    }

    public static void login(String email, Context applicationContext){
        SharedPreferences sharedPref = applicationContext.getSharedPreferences("session", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("email", email);
        editor.apply();
    }
}