package ca.seg2105project.model.repositories;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ca.seg2105project.model.registrationRequestClasses.AccountRegistrationRequest;

public class AccountRegistrationRequestRepository {

    // Firebase database reference
    private final DatabaseReference mDatabase;

    public AccountRegistrationRequestRepository() {
        // Initializing Firebase database reference
        mDatabase = FirebaseDatabase.getInstance().getReference("requests");
    }

    /**
     * Note that none of the requests in the returned list will have status Approved because
     * the user that corresponds to the approved request should be immediately registered as a proper
     * user in the user database
     * @return The full current list of pending or rejected requests from Firebase.
     */
    public List<AccountRegistrationRequest> readRequests() {
        List<AccountRegistrationRequest> requestList = new ArrayList<>();
        mDatabase.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot requestSnapshot : dataSnapshot.getChildren()) {
                    AccountRegistrationRequest request = requestSnapshot.getValue(AccountRegistrationRequest.class);
                    requestList.add(request);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase Error", "msg: " + databaseError.getMessage() + "details: " + databaseError.getDetails());
            }
        });
        return requestList;
    }

    /**
     * Adds new AccountRegistrationRequest to requests section in Firebase DB
     * @param newRequest the new request--either for an Attendee or for an Organizer--to be added to DB
     * @return whether or not the new request was successfully added to the DB
     */
    public boolean addNewAccountRegistrationRequest(AccountRegistrationRequest newRequest) {
        // generating a unique key for the request
        String requestID = mDatabase.push().getKey();

        // We failed to create a reference to a new child in the account registration requests section of Firebase
        if (requestID == null) {
            return false;
        }

        // Add the request to the requests section
        mDatabase.child(requestID).setValue(newRequest);
        return true;
    }
}
