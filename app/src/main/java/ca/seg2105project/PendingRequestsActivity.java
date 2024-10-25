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

public class PendingRequestsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_adminpendingrequests);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button seeRejectedRequestsBtn = findViewById(R.id.see_rejected_requests_btn);
        seeRejectedRequestsBtn.setOnClickListener(v -> {
            Intent launchRejectedRequestsActivityIntent = new Intent(this, RejectedRequestsActivity.class);

            // Make sure to re-use previously opened instance of rejected requests activity
            launchRejectedRequestsActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(launchRejectedRequestsActivityIntent);
        });
    }
}