package ca.seg2105project.model;

import java.util.ArrayList;
import java.util.List;

import ca.seg2105project.model.userClasses.Administrator;
import ca.seg2105project.model.userClasses.Attendee;
import ca.seg2105project.model.userClasses.Organizer;
import ca.seg2105project.model.userClasses.User;

/**
 * A class for accessing any information on any registered user.
 * In the next iteration of this class in this sprint, this class will hold a 'real'
 * list of registered users that will actually be modifiable.
 */
public class UserRepository {

    /**
     * In later implementations of this class this will actually return a List of Users that
     * have been added in the course of the app running.
     * In an even later implementation likely in deliverable 2 this will access the database.
     * @return a full list of all registered users
     */
    public static List<User> getAllRegisteredUsers() {
        ArrayList<User> allRegisteredUsers = new ArrayList<>();
        allRegisteredUsers.add(new Attendee("Isaac", "Jensen-Large",
                "jensenlarge.isaac@gmail.com", "awesomepassword",
                "54 Awesome St.", "6139835504"));

        allRegisteredUsers.add(new Organizer("Roni", "Nartatez",
                "ronisemail@gmail.com", "epicpassword",
        "57 Awesome St.", "6131234567", "Awesome Org."));

        allRegisteredUsers.add(new Administrator("admin@gmail.com",
                "adminpwd"));

        allRegisteredUsers.add(new Attendee("Rachel", "Luo",
                "rluo123@gmail.com", "walkingIsOverrated",
                "39 Mann", "6471234567"));

        allRegisteredUsers.add(new Organizer("Kunala", "Deotare",
                "kdeotare@gmail.com", "the_best_pass",
                "29 Mann", "4161234567", "Best Org."));

        allRegisteredUsers.add(new Administrator("shawn@gmail.com",
                "secure_pass"));

        return allRegisteredUsers;
    }

    /**
     * A method to see if an email-password pair exist in the list of all registered users.
     * @param email the email to be checked
     * @param password the password attatched to the email to be checked
     * @return true if the email-password pair was found in the list of registered users, false if not found
     */
    public static boolean authenticateEmailAndPassword(String email, String password) { //O(n), where n = # of registered users
        List<User> users = getAllRegisteredUsers();
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
	public static boolean authenticateEmail(String email) { //O(n), where n = # of registered users
		List<User> users = getAllRegisteredUsers();
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
	public static String getUserTypeByEmail(String email) {
		List<User> users = getAllRegisteredUsers();
		int n = users.size();
		Administrator adm = new Administrator(null, null);
		Attendee att = new Attendee(null, null, null, null, null, null);
		Attendee att = new Attendee(null, null, null, null, null, null, null);
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