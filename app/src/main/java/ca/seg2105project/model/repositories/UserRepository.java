package ca.seg2105project.model.repositories;

import androidx.annotation.NonNull;

import java.util.ArrayList;

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
	//firebase database references
	private final DatabaseReference usersDatabase;

    public UserRepository() {
		// Initializing Firebase database references
		usersDatabase = FirebaseDatabase.getInstance().getReference("users");

        //initialize the list of users, then update from fb
		registeredUsers = new ArrayList<>();
		pullUsers();
	}

	/**
	 * A method to update the final list of users "registeredUsers." Does so 'in-place.' Takes its updated data from the firebase database. Furthermore, returns the updated list of users.
	 */
	private void pullUsers() {
		usersDatabase.addValueEventListener(new ValueEventListener() {

			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				registeredUsers.clear();
				for (DataSnapshot requestSnapshot : dataSnapshot.getChildren()) {
					//So the most intuitive thing in order to read Users from fb would be to getValue(User.class) but that won't work since that does two steps:
					//step1: instantiates an empty User and then step2: assigns the previously instantiated user to the one being read from fb
					//This way won't work since User is abstract and thus uninstantiable. So I though of a work around:
					//Do step1 and step2 but to get the user from fb as an Organizer object (since Organizer has all the fields that a user can ever have).
					//When we get this Organizer object, its fields will be either a valid string or null.
					//If the firstName is null, clearly the User acquired from fb is an Administrator. If the organizationName is null, then User is an Attendee. Otherwise, the User can be kept as an Organizer object.
					//Once we determine what type of User has been stored in that Organizer object, we can create the respective type of User and call the getter methdos on that object to fill in the user's fields.
					//This is obviously a very messy work around but the alternative is to make User not abstract, which goes against our previously agreed upon conventions.
					//Kunala Deotare takes ownership of this work around, dated 27 October 2024. Please contact me at kdeot090@uottawa.ca to ask any questions.

					Organizer user = requestSnapshot.getValue(Organizer.class);
					if (user.getFirstName() == null) { //user must really be an Administrator
						Administrator adm = new Administrator(user.getEmail(), user.getPassword());
						registeredUsers.add(adm);
					} else if (user.getOrganizationName() == null) { //user must really be an Attendee
						Attendee att = new Attendee(user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword(), user.getAddress(), user.getPhoneNumber());
						registeredUsers.add(att);
					} else { //user must actually be an Organizer
						registeredUsers.add(user);
					}
				}
				usersDatabase.removeEventListener(this);
			}

			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {
				usersDatabase.removeEventListener(this);
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
		pullUsers();
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
			// generating a unique key for the request
			String userID = usersDatabase.push().getKey();

			// setting the requestID key's value to the request
			usersDatabase.child(userID).setValue(user);
		}
	}

    /**
     * A method to see if an email-password pair exists in the list of all registered users.
     * @param email the email to be checked
     * @param password the password attatched to the email to be checked
     * @return true if the email-password pair was found in the list of registered users, false if not found
     */
    public boolean authenticate(String email, String password) { //O(n), where n = # of registered users
        int n = registeredUsers.size();
        for (int x = 0; x < n; x++) {
            User u = registeredUsers.get(x);
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
		int n = registeredUsers.size();
		for (int x = 0; x < n; x++) {
			User u = registeredUsers.get(x);
			if (u.getEmail().equals(email)) {
				return true;
			}
		}
		// Update user list in case user has been added since the app started
		pullUsers();
		return false;
	}

	/**
     * A method to return the type of user of the email provided (Administrator, Attendee, or Organizer).
     * @param email the email whose user type is to be returned
     * @return a string representation of the user type of the email provided, null if the email is not found
     */
	public String getUserTypeByEmail(String email) {
		int n = registeredUsers.size();
		Administrator adm = new Administrator(null, null);
		Attendee att = new Attendee(null, null, null, null, null, null);
		for (int x = 0; x < n; x++) {
			User u = registeredUsers.get(x);
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