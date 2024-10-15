package ca.seg2105project;

import android.app.Application;

import ca.seg2105project.model.LoginSessionRepository;
import ca.seg2105project.model.UserRepository;

/**
 * Custom application class to be able to access single instances of repository classes
 * for the app. This ensures more reliable behaviour across the app and we can more easily track
 * how the repositories are interacted with and what happens with their data.
 */
public class EAMSApplication extends Application {

    private UserRepository userRepository;
    private LoginSessionRepository loginSessionRepository;

    @Override
    public void onCreate() {
        super.onCreate();

        userRepository = new UserRepository();
        loginSessionRepository = new LoginSessionRepository(getApplicationContext());

        // Invalidate potentially outdated login session
        if (loginSessionRepository.hasActiveLoginSession()) {
            if (!userRepository.isEmailRegistered(loginSessionRepository.getActiveLoginSessionEmail())) {
                loginSessionRepository.endLoginSession();
            }
        }
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public LoginSessionRepository getLoginSessionRepository() {
        return loginSessionRepository;
    }
}
