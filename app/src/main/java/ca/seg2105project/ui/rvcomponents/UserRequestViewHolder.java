package ca.seg2105project.ui.rvcomponents;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

    ImageView IVrequestBox;
    TextView firstName, lastName, email, phoneNumber,address, organizationName;
    Button approveButton, rejectButton;

    /**
     * A parameterized constructor for RequestViewHolder.
     * @param requestItemView represents the a single item in the RecyclerView
     */
    public UserRequestViewHolder(@NonNull View requestItemView) {
        super(requestItemView);
        IVrequestBox = requestItemView.findViewById(R.id.IVrequestBox);
        firstName = requestItemView.findViewById(R.id.firstName);
        lastName = requestItemView.findViewById(R.id.lastName);
        email = requestItemView.findViewById(R.id.email);
        phoneNumber = requestItemView.findViewById(R.id.phoneNumber);
        address = requestItemView.findViewById(R.id.address);
        organizationName = requestItemView.findViewById(R.id.organizationName);
        approveButton = requestItemView.findViewById(R.id.approve_button);
        rejectButton = requestItemView.findViewById(R.id.reject_button);
    }
}
