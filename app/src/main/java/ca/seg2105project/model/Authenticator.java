package ca.seg2105project.model;

import java.util.List;

import ca.seg2105project.model.userClasses.User;

/**
 * A class to see if a given email and password match any of the email-password pairs in the user repository.
 */
public class Authenticator {
	/**
	 * A method to see if an email-password pair exist in user repository.
	 * @param email the email to be checked
	 * @param password the password attatched to the email to be checked
	 * @return true if the email-password pair was found in user repository, false if not found
	 */
	public boolean authenticate(String email, String password) { //O(n)
		UserRepository userrep = new UserRepository();
		List<User> users = userrep.getAllRegisteredUsers();
		int n = users.size();
		for (int x = 0; x < n; x++) {
			User u = users.get(x);
			if (u.getEmail().equals(email) && u.getPassword().equals(password)) {
				return true;
			}
			//u.getEmail() != email || u.getPassword() != password
		}
		return false;
	}
}