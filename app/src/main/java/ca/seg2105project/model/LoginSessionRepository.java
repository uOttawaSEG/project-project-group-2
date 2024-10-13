package ca.seg2105project.model;

import android.content.Context;
import android.content.SharedPreferences;

public class LoginSessionRepository {

    /**
     * Checks for existing shared preference extra "email" which contains the email of a user
     * that has already successfully logged in
     * @param applicationContext Android application context
     * @return true if a user has logged in in a previous app session and has not yet logged out,
     *  false otherwise
     */
    public static boolean hasActiveLoginSession(Context applicationContext){
        SharedPreferences sharedPref = applicationContext.getSharedPreferences("session",
                Context.MODE_PRIVATE);
        return sharedPref.contains("email");
    }

    /**
     * Method to store successfully authenticated (see UserRepository.authenticateEmailAndPassowrd)
     * user email. This is used to maintain user login session across all app instances until the
     * user specifically logs out
     * @param email the email of the successfully authenticated user
     * @param applicationContext Android application context
     */
    public static void startLoginSession(String email, Context applicationContext){
        SharedPreferences sharedPref = applicationContext.getSharedPreferences("session",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("email", email);
        editor.apply();
    }
}