package ca.seg2105project.model.repositories;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ca.seg2105project.model.registrationRequestClasses.AccountRegistrationRequest;
import ca.seg2105project.model.registrationRequestClasses.AccountRegistrationRequestStatus;
import ca.seg2105project.model.userClasses.Attendee;
import ca.seg2105project.model.userClasses.Organizer;
import ca.seg2105project.model.userClasses.User;

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
                mDatabase.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase Error", "msg: " + databaseError.getMessage() + "details: " + databaseError.getDetails());
                mDatabase.removeEventListener(this);
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

    /**
     * Check if new status is approved, if so then retrieve information from the request and remove from firebase and create
     * new user with the information retrieved. If not approved change status to whatever new status is.
     */
    public void updateRequestStatus(String email, AccountRegistrationRequestStatus newStatus){
        Query emailQuery = mDatabase.orderByChild("email").equalTo(email);
        emailQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    DataSnapshot snapshot = dataSnapshot.getChildren().iterator().next();
                    AccountRegistrationRequest request = snapshot.getValue(AccountRegistrationRequest.class);
                    if(newStatus == AccountRegistrationRequestStatus.APPROVED) {
                        String userID = FirebaseDatabase.getInstance().getReference("users").push().getKey();
                        User user;
                        if(request.getOrganizationName() == null) {
                            user = new Attendee(request.getFirstName(), request.getLastName(), request.getEmail(), request.getPassword(), request.getAddress(), request.getPhoneNumber());
                        }
                        else {
                            user = new Organizer(request.getFirstName(), request.getLastName(), request.getEmail(), request.getPassword(), request.getAddress(), request.getPhoneNumber(), request.getOrganizationName());
                        }
                        FirebaseDatabase.getInstance().getReference("users").child(userID).setValue(user);
                        deleteRequest(email);
                    } else {
                        String key = snapshot.getKey();
                        mDatabase.child(key).child("status").setValue(newStatus.toString());
                    }
                }
                emailQuery.removeEventListener(this);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase Error", "msg: " + databaseError.getMessage() + "details: " + databaseError.getDetails());
                emailQuery.removeEventListener(this);
            }
        });
    }

    /**
     * Removes the request associated with a specific email from firebase
     */
    public void deleteRequest(String email){
        Query emailQuery = mDatabase.orderByChild("email").equalTo(email);
        emailQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        String key = snapshot.getKey();
                        mDatabase.child(key).removeValue()
                                .addOnSuccessListener(v -> Log.d("Firebase", "Succesfully deleted request from email: " + email))
                                .addOnFailureListener(e -> Log.e("Firebase", "Error trying to delete request from email: " + email));
                    }
                }
                emailQuery.removeEventListener(this);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase Error", "msg: " + databaseError.getMessage() + "details: " + databaseError.getDetails());
                emailQuery.removeEventListener(this);
            }
        });
    }
}
