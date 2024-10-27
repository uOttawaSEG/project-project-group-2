package ca.seg2105project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ca.seg2105project.model.registrationRequestClasses.AccountRegistrationRequest;
import ca.seg2105project.model.repositories.LoginSessionRepository;

public class RejectedRequestsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_adminrejectedrequests);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button seePendingRequestsBtn = findViewById(R.id.see_pending_requests_btn);
        seePendingRequestsBtn.setOnClickListener(v -> {
            Intent launchPendingRequestsActivity = new Intent(this, PendingRequestsActivity.class);
            startActivity(launchPendingRequestsActivity);

            // Close this instance of RejectedRequestsActivity in case user logs off. They shouldn't be able to back-navigate
            // to this activity
            finish();
        });

        EAMSApplication eamsApplication = (EAMSApplication) getApplication();
        LoginSessionRepository loginSessionRepository = eamsApplication.getLoginSessionRepository();

        Button logoffBtn = findViewById(R.id.Logout_BTN);
        logoffBtn.setOnClickListener(v -> {
            // Removes email from our shared preferences
            loginSessionRepository.endLoginSession();

            // Let user know they are logged out
            Toast.makeText(getApplicationContext(), "Logged out successfully",
                    Toast.LENGTH_LONG).show();

            // Sends user back to login screen
            Intent launchLoginActivityIntent = new Intent(this, LoginActivity.class);
            startActivity(launchLoginActivityIntent);

            // User shouldn't be able to return to this instance of RejectedRequestsActivity
            finish();
        });

        RecyclerView recyclerView = findViewById(R.id.rejectedRV);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<AccountRegistrationRequest> requestList = new ArrayList<>();
        requestList.add(new AccountRegistrationRequest("Veronica", "Nartatez","veronicanartatez@gmail.com","poggers", "123 cool street", "1234567890", "Test"));
        requestList.add(new AccountRegistrationRequest("Shane", "Topp","shanetopp@gmail.com","poggers", "122 cool street", "1234567891", null));
        requestList.add(new AccountRegistrationRequest("Courtney", "Topp","counrtneytopp@gmail.com","poggers", "122 cool street", "1234567891", null));
        requestList.add(new AccountRegistrationRequest("Freddy", "Fazbear","freddyfazzbear@gmail.com","poggers", "121 cool street", "1234567892", null));
        requestList.add(new AccountRegistrationRequest("Michael", "Reeves","michalreeves@gmail.com","poggers", "120 cool street", "1234567893", null));
        requestList.add(new AccountRegistrationRequest("Michael", "Reeves","michalreeves@gmail.com","poggers", "120 cool street", "1234567893", null));
        requestList.add(new AccountRegistrationRequest("Michael", "Reeves","michalreeves@gmail.com","poggers", "120 cool street", "1234567893", null));
        requestList.add(new AccountRegistrationRequest("Michael", "Reeves","michalreeves@gmail.com","poggers", "120 cool street", "1234567893", null));
        requestList.add(new AccountRegistrationRequest("Michael", "Reeves","michalreeves@gmail.com","poggers", "120 cool street", "1234567893", null));
        requestList.add(new AccountRegistrationRequest("Michael", "Reeves","michalreeves@gmail.com","poggers", "120 cool street", "1234567893", null));
        requestList.add(new AccountRegistrationRequest("Michael", "Reeves","michalreeves@gmail.com","poggers", "120 cool street", "1234567893", null));

        recyclerView.setAdapter(new AdminAdapter(requestList, getApplicationContext()));
    }
}
