package ca.seg2105project;

import android.accounts.Account;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;
import java.util.ArrayList;
import androidx.annotation.NonNull;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.util.PatternsCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputLayout;

import ca.seg2105project.model.UserRepository;
import ca.seg2105project.model.registrationRequestClasses.AccountRegistrationRequest;
import ca.seg2105project.model.userClasses.Attendee;
import ca.seg2105project.model.userClasses.Organizer;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {

    private UserRepository userRepository;

    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText emailEditText;
    private EditText createPasswordEditText;
    private EditText confirmPAsswordEditText;
    private EditText phoneNumberEditText;
    private EditText addressEditText;
    private CheckBox isOrganizationCheckBox;
    private TextInputLayout organizationEditTextLayout;
    private EditText organizationEditText;

    // List to hold requests
    private List<AccountRegistrationRequest> requestList;
	
	//firebase database reference
	private DatabaseReference mDatabase;

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

        firstNameEditText = findViewById(R.id.firstNameET);
        lastNameEditText = findViewById(R.id.lastNameET);
        emailEditText = findViewById(R.id.emailET);
        createPasswordEditText = findViewById(R.id.createPasswordET);
        confirmPAsswordEditText = findViewById(R.id.confirmPasswordET);
        phoneNumberEditText = findViewById(R.id.phoneNumberET);
        addressEditText = findViewById(R.id.addressET);
        organizationEditText = findViewById(R.id.OrganizationET);
        organizationEditTextLayout = findViewById(R.id.organization_ET_layout);
        isOrganizationCheckBox = findViewById(R.id.isOrganizationCB);

        EAMSApplication eamsApplication = (EAMSApplication) getApplication();
        userRepository = eamsApplication.getUserRepository();

        setCreateAccountButtonBehaviour();
        setIsOrganizationCheckBoxBehaviour();
		
		// Initializing Firebase database reference
        mDatabase = FirebaseDatabase.getInstance().getReference("requests");

        //intialize request list
        requestList = new ArrayList<>();
        readRequests();
    }


    // Method to read requests from Firebase
    private void readRequests() {
        mDatabase.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                requestList.clear();
                for (DataSnapshot requestSnapshot : dataSnapshot.getChildren()) {
                    Object request = requestSnapshot.getValue(AccountRegistrationRequest.class);

                    requestList.add(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Toast.makeText(RegisterActivity.this, "Failed to read request: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                //the toast here was being activated too early. Need to figure out why and fix it. @TODO
            }
        });
    }

    private void setCreateAccountButtonBehaviour() {
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
                        // the entered passwords match, and the user has entered text for every attendee related field

                        if (!PatternsCompat.EMAIL_ADDRESS.matcher(enteredEmail).matches()) {
                            Toast.makeText(this, "Provided email isn't a valid email address", Toast.LENGTH_LONG).show();
                        } else {
                            // Now we know that the email entered by the user is available to register,
                            // the entered passwords match, the user has entered text for every attendee related field
                            // and the email is formatted properly

                            if (!Patterns.PHONE.matcher(enteredPhoneNumber).matches()) {
                                Toast.makeText(this, "Provided phone number isn't a valid phone number", Toast.LENGTH_LONG).show();
                            } else {
                                // Now we know that the email entered by the user is available to register,
                                // the entered passwords match, the user has entered text for every attendee related field
                                // the email is formatted properly, and the phone number is formatted properly

                                if (isOrganizationCheckBox.isChecked()) {
                                    String enteredOrganizationName = organizationEditText.getText().toString();

                                    if (enteredOrganizationName.isEmpty()) {
                                        Toast.makeText(this, "If you're an event organizer, " +
                                                "we need an organization name", Toast.LENGTH_LONG).show();
                                    } else {
                                        // creating a new request
                                        AccountRegistrationRequest newOrganizerRequest = new AccountRegistrationRequest(enteredFirstName, enteredLastName, enteredEmail, enteredPassword, enteredAddress, enteredPhoneNumber, enteredOrganizationName);

                                        // generating a unique key for the request
                                        String requestID = mDatabase.push().getKey();

                                        // setting the requestID key's value to the request
                                        mDatabase.child(requestID).setValue(newOrganizerRequest);

                                        //TODO make a toast to announce to the user that their request has been processed

                                        // Successful registration, send user back to login screen
                                        launchLoginActivityAndFinishRegistration();
                                    }
                                } else {
                                    // User is requesting to be an Attendee
                                    AccountRegistrationRequest newAttendeeRequest = new AccountRegistrationRequest(enteredFirstName, enteredLastName, enteredEmail, enteredPassword, enteredAddress, enteredPhoneNumber, null);

                                    // generating a unique key for the request
                                    String requestID = mDatabase.push().getKey();

                                    // setting the requestID key's value to the request
                                    mDatabase.child(requestID).setValue(newAttendeeRequest);

                                    //TODO make a toast to announce to the user that their request has been processed

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

    private void setIsOrganizationCheckBoxBehaviour() {
        isOrganizationCheckBox.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                organizationEditTextLayout.setVisibility(View.VISIBLE);
            } else {
                organizationEditTextLayout.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void launchLoginActivityAndFinishRegistration() {
        Toast.makeText(this, "Successfully registered, please login",
                Toast.LENGTH_LONG).show();
        Intent launchLoginActivityIntent = new Intent(this, LoginActivity.class);

        // Make sure we re-use the instance of LoginActivity that brought the user to this register
        // activity
        launchLoginActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(launchLoginActivityIntent);
        finish();
    }
}