package ca.seg2105project.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

import ca.seg2105project.ui.rvcomponents.ARRListAdapter;
import ca.seg2105project.EAMSApplication;
import ca.seg2105project.R;
import ca.seg2105project.model.registrationRequestClasses.AccountRegistrationRequest;
import ca.seg2105project.model.registrationRequestClasses.RegistrationRequestStatus;
import ca.seg2105project.model.repositories.AccountRegistrationRequestRepository;
import ca.seg2105project.model.repositories.LoginSessionRepository;

public class PendingAccountRequestsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pendingaccountrequests);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EAMSApplication eamsApplication = (EAMSApplication) getApplication();
        LoginSessionRepository loginSessionRepository = eamsApplication.getLoginSessionRepository();
        AccountRegistrationRequestRepository accountRegistrationRequestRepository = eamsApplication.getAccountRegistrationRequestRepository();

        Button seeRejectedRequestsBtn = findViewById(R.id.see_rejected_requests_btn);
        seeRejectedRequestsBtn.setOnClickListener(v -> {
            Intent launchRejectedRequestsActivityIntent = new Intent(this, RejectedAccountRequestsActivity.class);
            startActivity(launchRejectedRequestsActivityIntent);

            // Close this instance of PendingRequestsActivity in case user logs off. They shouldn't be able to back-navigate
            // to this activity
            finish();
        });

        Button logoffBtn = findViewById(R.id.logout_btn);
        logoffBtn.setOnClickListener(v -> {
            // Removes email from our shared preferences
            loginSessionRepository.endLoginSession();

            // Let user know they are logged out
            Toast.makeText(getApplicationContext(), "Logged out successfully",
                    Toast.LENGTH_LONG).show();

            // Sends user back to login screen
            Intent launchLoginActivityIntent = new Intent(this, LoginActivity.class);
            startActivity(launchLoginActivityIntent);

            // User shouldn't be able to return to this instance of PendingRequestsActivity
            finish();
        });

        RecyclerView recyclerView = findViewById(R.id.pendingRV);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<AccountRegistrationRequest> accountRegistrationRequests = accountRegistrationRequestRepository.readRequests();

        // This delayed runnable is for making sure that Firebase has enough time to fill the
        // pendingAccountRegistrationRequests list before we render the list in the RV
        // That's why we delay the running of setting the adapter by 1000 ms
        Runnable setPendingRvList = new Runnable() {
            @Override
            public void run() {
                List<AccountRegistrationRequest> pendingAccountRegistrationRequests = new ArrayList<>();
                for (int i = 0; i < accountRegistrationRequests.size(); i++) {
                    if (accountRegistrationRequests.get(i).getStatus() == RegistrationRequestStatus.PENDING) {
                        pendingAccountRegistrationRequests.add(accountRegistrationRequests.get(i));
                    }
                }

                recyclerView.setAdapter(new ARRListAdapter(
                        pendingAccountRegistrationRequests,
                        accountRegistrationRequestRepository));
            }
        };
        Handler h = new Handler();
        h.postDelayed(setPendingRvList, 1000);
    }
}