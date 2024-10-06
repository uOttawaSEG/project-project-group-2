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
     * A method to see if an email-password pair exist in set of all registered users.
     * @param email the email to be checked
     * @param password the password attatched to the email to be checked
     * @return true if the email-password pair was found in user repository, false if not found
     */
    public static boolean authenticate(String email, String password) { //O(n)
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
}