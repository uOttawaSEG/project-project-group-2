package ca.seg2105project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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

            // make sure to re-use any existing instance of pending requests activity
            launchPendingRequestsActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(launchPendingRequestsActivity);
        });
    }
}
