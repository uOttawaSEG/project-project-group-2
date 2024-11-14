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

import ca.seg2105project.ui.rvcomponents.AccountRegistrationRequestListAdapter;
import ca.seg2105project.EAMSApplication;
import ca.seg2105project.R;
import ca.seg2105project.model.registrationRequestClasses.AccountRegistrationRequest;
import ca.seg2105project.model.registrationRequestClasses.RegistrationRequestStatus;
import ca.seg2105project.model.repositories.AccountRegistrationRequestRepository;
import ca.seg2105project.model.repositories.LoginSessionRepository;

public class RejectedAccountRequestsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_rejectedaccountrequests);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button seePendingRequestsBtn = findViewById(R.id.see_pending_requests_btn);
        seePendingRequestsBtn.setOnClickListener(v -> {
            Intent launchPendingRequestsActivity = new Intent(this, PendingAccountRequestsActivity.class);
            startActivity(launchPendingRequestsActivity);

            // Close this instance of RejectedRequestsActivity in case user logs off. They shouldn't be able to back-navigate
            // to this activity
            finish();
        });

        EAMSApplication eamsApplication = (EAMSApplication) getApplication();
        LoginSessionRepository loginSessionRepository = eamsApplication.getLoginSessionRepository();
        AccountRegistrationRequestRepository accountRegistrationRequestRepository = eamsApplication.getAccountRegistrationRequestRepository();

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
        List<AccountRegistrationRequest> accountRegistrationRequests = accountRegistrationRequestRepository.readRequests();

        // This delayed runnable is for making sure that Firebase has enough time to fill the
        // pendingAccountRegistrationRequests list before we render the list in the RV
        // That's why we delay the running of setting the adapter by 1000 ms
        Runnable setRejectedRvList = new Runnable() {
            @Override
            public void run() {
                List<AccountRegistrationRequest> rejectedAccountRegistrationRequests = new ArrayList<>();
                for (int i = 0; i < accountRegistrationRequests.size(); i++) {
                    if (accountRegistrationRequests.get(i).getStatus() == RegistrationRequestStatus.REJECTED) {
                        rejectedAccountRegistrationRequests.add(accountRegistrationRequests.get(i));
                    }
                }

                recyclerView.setAdapter(new AccountRegistrationRequestListAdapter(
                        rejectedAccountRegistrationRequests,
                        accountRegistrationRequestRepository));
            }
        };
        Handler h = new Handler();
        h.postDelayed(setRejectedRvList, 1000);
    }
}
