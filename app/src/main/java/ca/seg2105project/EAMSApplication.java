package ca.seg2105project;

import android.app.Application;

import ca.seg2105project.model.repositories.AccountRegistrationRequestRepository;
import ca.seg2105project.model.repositories.LoginSessionRepository;
import ca.seg2105project.model.repositories.UserRepository;

/**
 * Custom application class to be able to access single instances of repository classes
 * for the app. This ensures more reliable behaviour across the app and we can more easily track
 * how the repositories are interacted with and what happens with their data.
 */
public class EAMSApplication extends Application {

    private UserRepository userRepository;
    private LoginSessionRepository loginSessionRepository;
    private AccountRegistrationRequestRepository accountRegistrationRequestRepository;

    @Override
    public void onCreate() {
        super.onCreate();

        userRepository = new UserRepository();
        loginSessionRepository = new LoginSessionRepository(getApplicationContext());
        accountRegistrationRequestRepository = new AccountRegistrationRequestRepository();
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public LoginSessionRepository getLoginSessionRepository() {
        return loginSessionRepository;
    }

    public AccountRegistrationRequestRepository getAccountRegistrationRequestRepository() {
        return accountRegistrationRequestRepository;
    }
}
