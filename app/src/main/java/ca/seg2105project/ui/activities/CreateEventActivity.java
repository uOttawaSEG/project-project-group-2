package ca.seg2105project.ui.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

import java.time.LocalDate;
import java.util.Objects;

import ca.seg2105project.EAMSApplication;
import ca.seg2105project.R;
import ca.seg2105project.model.eventClasses.Event;
import ca.seg2105project.model.repositories.EventRepository;
import ca.seg2105project.model.repositories.LoginSessionRepository;
import ca.seg2105project.model.repositories.UserRepository;

public class CreateEventActivity extends AppCompatActivity {

    private EAMSApplication eamsApplication;

    private TextInputEditText eventTitleEditText;
    private TextInputEditText eventDescriptionEditText;
    private TextInputEditText dateEditText;
    private EditText startTimeEditText;
    private EditText endTimeEditText;
    private EditText eventAddrressEditText;
    private CheckBox autoApproveCheckBox;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_createevent);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        eamsApplication = (EAMSApplication) getApplication();

        eventTitleEditText = findViewById(R.id.event_title);
        eventDescriptionEditText = findViewById(R.id.event_description);
        dateEditText = findViewById(R.id.date);
        startTimeEditText = findViewById(R.id.startTime);
        endTimeEditText = findViewById(R.id.endTime);
        eventAddrressEditText = findViewById(R.id.eventAddress);
        autoApproveCheckBox = findViewById(R.id.autoApproveCheckBox);

        setCreateEventButtonBehaviour();
    }

    private void setCreateEventButtonBehaviour() {
        Button createEventButton = findViewById(R.id.createEventAccountBTN);
        createEventButton.setOnClickListener(v -> {
            try {
                String enteredEventTitle = Objects.requireNonNull(eventTitleEditText.getText()).toString();
                String enteredEventDescription = Objects.requireNonNull(eventDescriptionEditText.getText()).toString();

                String[] date = Objects.requireNonNull(dateEditText.getText()).toString().split("-");
                int enteredYear = Integer.parseInt(date[0]);
                int enteredMonth = Integer.parseInt(date[1]);
                int enteredDay = Integer.parseInt(date[2]);

                LocalDate eventDate = LocalDate.of(enteredYear, enteredMonth, enteredDay);
                LocalDate today = LocalDate.now();
                if (eventDate.isBefore(today)) {
                    Toast.makeText(this, "Cannot create event for a past date", Toast.LENGTH_LONG).show();
                    return;
                }

                String[] enteredStartTime = startTimeEditText.getText().toString().split(":");
                int enteredStartHour = Integer.parseInt(enteredStartTime[0]);
                int enteredStartMinute = Integer.parseInt(enteredStartTime[1]);

                String[] enteredEndTime = endTimeEditText.getText().toString().split(":");
                int enteredEndHour = Integer.parseInt(enteredEndTime[0]);
                int enteredEndMinute = Integer.parseInt(enteredEndTime[1]);

                if (enteredStartMinute % 30 != 0) {
                    Toast.makeText(this, "Start time must be in 30-minute increments", Toast.LENGTH_LONG).show();
                    return;
                }

                if (enteredEndMinute % 30 != 0) {
                    Toast.makeText(this, "End time must be in 30-minute increments", Toast.LENGTH_LONG).show();
                    return;
                }

                String enteredEventAddress = eventAddrressEditText.getText().toString();
                boolean enteredAutoApproveCheckBox = autoApproveCheckBox.isChecked();

                LoginSessionRepository loginSessionRepository = eamsApplication.getLoginSessionRepository();
                UserRepository userRepository = eamsApplication.getUserRepository();
                String organizerEmail = loginSessionRepository.getActiveLoginSessionEmail();

                EventRepository eventRepository = new EventRepository();

                Event newEvent = new Event(
                        String.valueOf(System.currentTimeMillis()),
                        enteredEventTitle,
                        enteredEventDescription,
                        enteredYear, enteredMonth, enteredDay,
                        enteredStartHour, enteredStartMinute,
                        enteredEndHour, enteredEndMinute,
                        enteredEventAddress,
                        organizerEmail,
                        enteredAutoApproveCheckBox);
                eventRepository.addEvent(newEvent);

                finish();
            } catch (Exception e) {
                Toast.makeText(this, "Please fill all fields correctly", Toast.LENGTH_LONG).show();
            }
        });
    }
}
