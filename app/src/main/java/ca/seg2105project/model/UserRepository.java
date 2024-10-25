package ca.seg2105project.model;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import ca.seg2105project.model.registrationRequestClasses.AccountRegistrationRequest;
import ca.seg2105project.model.registrationRequestClasses.AccountRegistrationRequestStatus;
import ca.seg2105project.model.userClasses.Administrator;
import ca.seg2105project.model.userClasses.Attendee;
import ca.seg2105project.model.userClasses.User;
import ca.seg2105project.model.userClasses.Organizer;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A class for accessing any information on any registered user.
 * In the next iteration of this class in this sprint, this class will hold a 'real'
 * list of registered users that will actually be modifiable.
 */
public class UserRepository {

	private final ArrayList<User> registeredUsers;
	//firebase database reference
	private DatabaseReference mDatabase;

	public UserRepository() {
		registeredUsers = new ArrayList<>();

		//Add admin
		registeredUsers.add(new Administrator("admin@gmail.com", "adminpwd"));
	}

	public ArrayList<User> readUsers() {
		mDatabase.addValueEventListener(new ValueEventListener() {

			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				registeredUsers.clear();
				for (DataSnapshot requestSnapshot : dataSnapshot.getChildren()) {
					User user = requestSnapshot.getValue(User.class);
					registeredUsers.add(user);
				}
			}

			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {
				//Toast.makeText(RegisterActivity.this, "Failed to read request: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
				//the toast here was being activated too early. Need to figure out why and fix it. @TODO
			}
		});
		return registeredUsers;
	}

	/**
	 * Goes through firebase and checks for accepted requests to turn them into users and add back to firebase
	 * Deletes accepted requests
	 * Might move to another class
	 */
	public void updateToUser() {
		mDatabase.addValueEventListener(new ValueEventListener() {

			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				for (DataSnapshot requestSnapshot : dataSnapshot.getChildren()) {
					AccountRegistrationRequest request = requestSnapshot.getValue(AccountRegistrationRequest.class);

					if(request.getStatus()== AccountRegistrationRequestStatus.APPROVED) {
						User user;
						if(request.getOrganizationName()==null) {
							user = new Attendee(request.getFirstName(), request.getLastName(), request.getEmail(), request.getPassword(), request.getAddress(), request.getPhoneNumber());
						}else {
							user = new Organizer(request.getFirstName(), request.getLastName(), request.getEmail(), request.getPassword(), request.getAddress(), request.getPhoneNumber(), request.getOrganizationName());
						}
						// generating a unique key for the request
						String userID = mDatabase.push().getKey();

						// setting the userID key's value to the user
						mDatabase.child(userID).setValue(user);

						//TODO: delete accepted request
					}
				}
			}

			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {
				//Toast.makeText(RegisterActivity.this, "Failed to read request: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
				//the toast here was being activated too early. Need to figure out why and fix it. @TODO
			}
		});
	}

    /**
     * In later implementations of this class this will actually return a List of Users that
     * have been added in the course of the app running.
     * In an even later implementation likely in deliverable 2 this will access the database.
     * @return a full list of all registered users
     */
    public ArrayList<User> getAllRegisteredUsers() {
		return registeredUsers;
    }

	/**
	 * Always run isEmailRegistered on the new users email before running this method.
	 * This method will do nothing if the provided user email is already in use.
	 * This method adds a provided User to the list of registered users for our application.
	 * In future implementations this will make a DB call instead of using a list in memory.
	 * @param user the user to be added
	 */
	public void registerUser(User user) {
		if (!isEmailRegistered(user.getEmail())) {
			registeredUsers.add(user);
		}
	}

    /**
     * A method to see if an email-password pair exists in the list of all registered users.
     * @param email the email to be checked
     * @param password the password attatched to the email to be checked
     * @return true if the email-password pair was found in the list of registered users, false if not found
     */
    public boolean authenticate(String email, String password) { //O(n), where n = # of registered users
        ArrayList<User> users = getAllRegisteredUsers();
        int n = users.size();
        for (int x = 0; x < n; x++) {
            User u = users.get(x);
            if (u.getEmail().equals(email) && u.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

	/**
     * A method to see if an email exists in the list of all registered users.
     * @param email the email to be checked
     * @return true if the email was found in the list of users, false if not found
     */
	public boolean isEmailRegistered(String email) { //O(n), where n = # of registered users
		ArrayList<User> users = getAllRegisteredUsers();
		int n = users.size();
		for (int x = 0; x < n; x++) {
			User u = users.get(x);
			if (u.getEmail().equals(email)) {
				return true;
			}
		}
		return false;
	}

	/**
     * A method to return the type of user of the email provided (Administrator, Attendee, or Organizer).
     * @param email the email whose user type is to be returned
     * @return a string representation of the user type of the email provided, null if the email is not found
     */
	public String getUserTypeByEmail(String email) {
		ArrayList<User> users = getAllRegisteredUsers();
		int n = users.size();
		Administrator adm = new Administrator(null, null);
		Attendee att = new Attendee(null, null, null, null, null, null);
		for (int x = 0; x < n; x++) {
			User u = users.get(x);
			if (u.getEmail().equals(email)) {
				if (u.getClass().equals(adm.getClass())) {
					return "Administrator";
				} else if (u.getClass().equals(att.getClass())) {
					return "Attendee";
				} else { //if u is not Attendee, Administrator, u must be Organizer since User is abstract and thus uninstantiable
					return "Organizer";
				}
			}
		}
		//have not found email in the list of registered users
		return null;
	}
}