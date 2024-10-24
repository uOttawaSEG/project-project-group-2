package ca.seg2105project.model.repositories;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;

public class LoginSessionRepository {

    private final Context applicationContext;

    public LoginSessionRepository(Context applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * Checks for existing shared preference extra "email" which contains the email of a user
     * that has already successfully logged in
     * @return true if a user has logged in in a previous app session and has not yet logged out,
     *  false otherwise
     */
    public boolean hasActiveLoginSession(){
        SharedPreferences sharedPref = applicationContext.getSharedPreferences("session",
                Context.MODE_PRIVATE);
        return sharedPref.contains("email");
    }

    /**
     * Method to store successfully authenticated (see UserRepository.authenticateEmailAndPassowrd)
     * user email. This is used to maintain user login session across all app instances until the
     * user specifically logs out
     * @param email the email of the successfully authenticated user
     */
    public void startLoginSession(String email) {
        SharedPreferences sharedPref = applicationContext.getSharedPreferences("session",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("email", email);
        editor.apply();
    }

    /**
     * @return the user email of the active login session. Will be null if there is no active login
     * session
     */
    @Nullable
    public String getActiveLoginSessionEmail() {
        SharedPreferences sharedPref = applicationContext.getSharedPreferences("session",
                Context.MODE_PRIVATE);
        return sharedPref.getString("email", null);
    }

    /**
     * Used to end a login session for a user. Make sure that there is an active login session
     * before calling this method using hasActiveLoginSession()
     */
    public void endLoginSession() {
        SharedPreferences sharedPref = applicationContext.getSharedPreferences("session",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove("email");
        editor.apply();
    }
}