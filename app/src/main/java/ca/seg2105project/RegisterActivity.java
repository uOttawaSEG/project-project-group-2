package ca.seg2105project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.util.PatternsCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import ca.seg2105project.model.UserRepository;
import ca.seg2105project.model.userClasses.Attendee;
import ca.seg2105project.model.userClasses.Organizer;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText firstNameEditText = findViewById(R.id.firstNameET);
        EditText lastNameEditText = findViewById(R.id.lastNameET);
        EditText emailEditText = findViewById(R.id.emailET);
        EditText createPasswordEditText = findViewById(R.id.createPasswordET);
        EditText confirmPAsswordEditText = findViewById(R.id.confirmPasswordET);
        EditText phoneNumberEditText = findViewById(R.id.phoneNumberET);
        EditText addressEditText = findViewById(R.id.addressET);
        EditText organizationEditText = findViewById(R.id.OrganizationET);
        CheckBox isOrganizerCheckBox = findViewById(R.id.isOrganizationCB);

        EAMSApplication eamsApplication = (EAMSApplication) getApplication();
        UserRepository userRepository = eamsApplication.getUserRepository();

        Button createAccountButton = findViewById(R.id.createAccountBTN);
        createAccountButton.setOnClickListener(v -> {

            String enteredEmail = emailEditText.getText().toString();
            if (userRepository.isEmailRegistered(enteredEmail)) {
                Toast.makeText(this, "The email you entered is already used for " +
                        "an existing account, please enter a different email", Toast.LENGTH_LONG).show();
            } else {
                // Now we know the email entered by the user is available to register
                String enteredPassword = createPasswordEditText.getText().toString();
                String enteredConfirmPassword = confirmPAsswordEditText.getText().toString();

                if (!enteredPassword.equals(enteredConfirmPassword)) {
                    Toast.makeText(this, "The passwords you entered don't match, " +
                            "please re-enter your passwords", Toast.LENGTH_LONG).show();
                } else {
                    // Now we know that the email entered by the user is available to register,
                    // and the entered passwords match.
                    String enteredFirstName = firstNameEditText.getText().toString();
                    String enteredLastName = lastNameEditText.getText().toString();
                    String enteredPhoneNumber = phoneNumberEditText.getText().toString();
                    String enteredAddress = addressEditText.getText().toString();

                    if (enteredFirstName.isEmpty() || enteredLastName.isEmpty() || enteredEmail.isEmpty() ||
                            enteredPassword.isEmpty() || enteredAddress.isEmpty() || enteredPhoneNumber.isEmpty()) {
                        Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_LONG).show();
                    } else {
                        // Now we know that the email entered by the user is available to register,
                        // the entered passwords match, and the user has entered text for every field

                        if (!PatternsCompat.EMAIL_ADDRESS.matcher(enteredEmail).matches()) {
                            Toast.makeText(this, "Provided email isn't a valid email address", Toast.LENGTH_LONG).show();
                        } else {
                            // Now we know that the email entered by the user is available to register,
                            // the entered passwords match, the user has entered text for every field
                            // and the email is formatted properly

                            if (!Patterns.PHONE.matcher(enteredPhoneNumber).matches()) {
                                Toast.makeText(this, "Provided phone number isn't a valid phone number", Toast.LENGTH_LONG).show();
                            } else {
                                // Now we know that the email entered by the user is available to register,
                                // the entered passwords match, the user has entered text for every field
                                // the email is formatted properly, and the phone number is formatted properly

                                if (isOrganizerCheckBox.isChecked()) {
                                    String enteredOrganizationName = organizationEditText.getText().toString();

                                    if (enteredOrganizationName.isEmpty()) {
                                        Toast.makeText(this, "If you're an event organizer, " +
                                                "we need an organization name", Toast.LENGTH_LONG).show();
                                    } else {
                                        // User is registered as Organizer
                                        Organizer newOrganizer = new Organizer(enteredFirstName, enteredLastName,
                                                enteredEmail, enteredPassword, enteredAddress, enteredPhoneNumber,
                                                enteredOrganizationName);
                                        userRepository.registerUser(newOrganizer);

                                        // Successful registration, send user back to login screen
                                        launchLoginActivityAndFinishRegistration();
                                    }
                                } else {
                                    // User is registered as Attendee
                                    Attendee newAttendee = new Attendee(enteredFirstName, enteredLastName,
                                            enteredEmail, enteredPassword, enteredAddress, enteredPhoneNumber);
                                    userRepository.registerUser(newAttendee);

                                    // Successful registration, send user back to login screen
                                    launchLoginActivityAndFinishRegistration();
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    private void launchLoginActivityAndFinishRegistration() {
        Toast.makeText(this, "Successfully registered, please login",
                Toast.LENGTH_LONG).show();
        Intent launchLoginActivityIntent = new Intent(this, LoginActivity.class);
        startActivity(launchLoginActivityIntent);
        finish();
    }
}