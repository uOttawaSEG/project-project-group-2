package ca.seg2105project;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RequestViewHolder extends RecyclerView.ViewHolder{

    ImageView IVrequestBox;
    TextView firstName, lastName, email, phoneNumber,address, organizationName;

    //Constructor for RequestViewHolder
    public RequestViewHolder(@NonNull View requestItemView) {
        super(requestItemView);
        IVrequestBox = requestItemView.findViewById(R.id.IVrequestBox);
        firstName = requestItemView.findViewById(R.id.firstName);
        lastName = requestItemView.findViewById(R.id.lastName);
        email = requestItemView.findViewById(R.id.email);
        phoneNumber = requestItemView.findViewById(R.id.phoneNumber);
        address = requestItemView.findViewById(R.id.address);
        organizationName = requestItemView.findViewById(R.id.organizationName);
    }
}
