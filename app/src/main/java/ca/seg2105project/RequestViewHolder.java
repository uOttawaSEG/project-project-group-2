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
    public RequestViewHolder(@NonNull View itemView) {
        super(itemView);
        IVrequestBox = itemView.findViewById(R.id.IVrequestBox);
        firstName = itemView.findViewById(R.id.firstName);
        lastName = itemView.findViewById(R.id.lastName);
        email = itemView.findViewById(R.id.email);
        phoneNumber = itemView.findViewById(R.id.phoneNumber);
        address = itemView.findViewById(R.id.address);
        organizationName = itemView.findViewById(R.id.organizationName);
    }
}
