package ca.seg2105project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
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
            // TODO: do all field validation and format checking here

            // Check if email entered by user is already registered for another account
            String enteredEmail = emailEditText.getText().toString();
            if (userRepository.isEmailRegistered(enteredEmail)) {
                Toast.makeText(this, "The email you entered is already used for " +
                        "an existing account, please enter a different email", Toast.LENGTH_LONG).show();
            } else {
                // Now we know the email entered by the user is available to register, next check
                // if the password and confirm password strings match
                String enteredPassword = createPasswordEditText.getText().toString();
                String enteredConfirmPassword = confirmPAsswordEditText.getText().toString();

                if (!enteredPassword.equals(enteredConfirmPassword)) {
                    Toast.makeText(this, "The passwords you entered don't match, " +
                            "please re-enter your passwords", Toast.LENGTH_LONG).show();
                } else {
                    // Now we know that the email entered by the user is available to register,
                    // and the entered passwords match.
                    // Now we register the user either as an Organizer or Attendee based on
                    // if they've checked the 'Are you an Organization?' checkbox
                    String enteredFirstName = firstNameEditText.getText().toString();
                    String enteredLastName = lastNameEditText.getText().toString();
                    String enteredPhoneNumber = phoneNumberEditText.getText().toString();
                    String enteredAddress = addressEditText.getText().toString();

                    if (isOrganizerCheckBox.isChecked()) {
                        // User is registered as Organizer
                        String enteredOrganizationName = organizationEditText.getText().toString();

                        Organizer newOrganizer = new Organizer(enteredFirstName, enteredLastName,
                                enteredEmail, enteredPassword, enteredAddress, enteredPhoneNumber,
                                enteredOrganizationName);
                        userRepository.registerUser(newOrganizer);
                    } else {
                        // User is registered as Attendee
                        Attendee newAttendee = new Attendee(enteredFirstName, enteredLastName,
                                enteredEmail, enteredPassword, enteredAddress, enteredPhoneNumber);
                        userRepository.registerUser(newAttendee);
                    }

                    Toast.makeText(this, "Successfully registered, please login",
                            Toast.LENGTH_LONG).show();
                    launchLoginActivityAndFinishRegistration();
                }
            }
        });
    }

    private void launchLoginActivityAndFinishRegistration() {
        Intent launchLoginActivityIntent = new Intent(this, LoginActivity.class);
        startActivity(launchLoginActivityIntent);
        finish();
    }
}