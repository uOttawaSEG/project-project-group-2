package ca.seg2105project.ui.rvcomponents;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ca.seg2105project.R;

/**
 * A class that is responsible for holding references to the UI components of each item of the RecyclerView
 * This particular view holder class is used for holding information about a user that is making
 * a request for something. In one case it could be a request for account registration, in another
 * case it could be event registration.
 */
public class UserRequestViewHolder extends RecyclerView.ViewHolder{

    TextView firstName, lastName, email, phoneNumber,address, organizationName;
    Button approveButton, rejectButton;

    /**
     * A parameterized constructor for RequestViewHolder.
     * @param userRequestItemView represents the a single item in the RecyclerView
     */
    public UserRequestViewHolder(@NonNull View userRequestItemView) {
        super(userRequestItemView);
        firstName = userRequestItemView.findViewById(R.id.firstName);
        lastName = userRequestItemView.findViewById(R.id.lastName);
        email = userRequestItemView.findViewById(R.id.end_time_tv);
        phoneNumber = userRequestItemView.findViewById(R.id.event_location_tv);
        address = userRequestItemView.findViewById(R.id.request_status_tv);
        organizationName = userRequestItemView.findViewById(R.id.organizationName);
        approveButton = userRequestItemView.findViewById(R.id.view_event_details_btn);
        rejectButton = userRequestItemView.findViewById(R.id.request_to_join_btn);
    }
}
