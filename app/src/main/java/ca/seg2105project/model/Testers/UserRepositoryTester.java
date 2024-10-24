package ca.seg2105project.model.Testers;

import ca.seg2105project.model.repositories.UserRepository;
import ca.seg2105project.model.userClasses.Administrator;
import ca.seg2105project.model.userClasses.Attendee;
import ca.seg2105project.model.userClasses.Organizer;

/**
* A class to test the methods in UserRepository.java
*/
public class UserRepositoryTester {

	/**
	* A method to test the implementation of the UserRepository methods authenticate, isEmailRegistered, and getUserTypeByEmail. 
	* Prints out a string containing lines of format "value=returned_value_from_method". As long as the two values on either sides of the = sign are the same, the method is working as intended.
	* @param args the command line arguments, will not be used
	*/
    public static void main(String[] args) {
        UserRepository userRepository = new UserRepository();

        userRepository.registerUser(new Attendee("Isaac", "Jensen-Large",
                "jensenlarge.isaac@gmail.com", "awesomepassword",
                "54 Awesome St.", "6139835504"));

        userRepository.registerUser(new Organizer("Roni", "Nartatez",
                "ronisemail@gmail.com", "epicpassword",
                "57 Awesome St.", "6131234567", "Awesome Org."));

        userRepository.registerUser(new Attendee("Rachel", "Luo",
                "rluo123@gmail.com", "walkingIsOverrated",
                "39 Mann", "6471234567"));

        userRepository.registerUser(new Organizer("Kunala", "Deotare",
                "kdeotare@gmail.com", "the_best_pass",
                "29 Mann", "4161234567", "Best Org."));

        userRepository.registerUser(new Administrator("shawn@gmail.com",
                "secure_pass"));
		
        //Tests added for UserRepository.authenticate
        String s = "";
        boolean answer;
		String temp = "";
        answer = userRepository.authenticate("", ""); //false
        s+= "false=" + answer + "\n";

        //Test for existing accounts (and added mispellings for it to return false)
        answer = userRepository.authenticate("jensenlarge.isaac@gmail.com", "awesomepassword"); //true
        s+= "true=" + answer + "\n";
        answer = userRepository.authenticate("isaac@gmail.com", "awesomepassword"); //false
        s+= "false=" + answer + "\n";
        answer = userRepository.authenticate("jensenlarge.isaac@gmail.com", "THE BEST PASSWORD EVER"); //false
        s+= "false=" + answer + "\n";

        answer = userRepository.authenticate("ronisemail@gmail.com", "epicpassword"); //true
        s+="true=" +  answer + "\n";
        answer = userRepository.authenticate("roniemail@gmail.com", "epicpassword"); //false
        s+="false=" +  answer + "\n";
        answer = userRepository.authenticate("ronisemail@gmail.com", "coolpassword"); //false
        s+="false=" +  answer + "\n";

        answer = userRepository.authenticate("admin@gmail.com", "adminpwd"); //true
        s+="true=" +  answer + "\n";
        answer = userRepository.authenticate("administrator@gmail.com", "adminpwd"); //false
        s+="false=" +  answer + "\n";
        answer = userRepository.authenticate("admin@gmail.com", "adminpassword"); //false
        s+="false=" +  answer + "\n";

        answer = userRepository.authenticate("rluo123@gmail.com", "walkingIsOverrated"); //true
        s+="true=" +  answer + "\n";
        answer = userRepository.authenticate("rluo123@gmail.com", "walkingIsNotOverrated"); //false
        s+="false=" +  answer + "\n";
        answer = userRepository.authenticate("rluo12@gmail.com", "walkingIsOverrated"); //false
        s+="false=" +  answer + "\n";

        answer = userRepository.authenticate("kdeotare@gmail.com", "the_best_pass"); //true
        s+="true=" +  answer + "\n";
        answer = userRepository.authenticate("kdeotare@gmail.com", "THE_best_pass"); //false
        s+="false=" +  answer + "\n";
        answer = userRepository.authenticate("kunaladeotare@gmail.com", "the_best_pass"); //false
        s+="false=" +  answer + "\n";

        answer = userRepository.authenticate("shawn@gmail.com", "secure_pass"); //true
        s+="true=" +  answer + "\n";
        answer = userRepository.authenticate("shawn@gmail.com", "not_secure_pass"); //false
        s+="false=" +  answer + "\n";
        answer = userRepository.authenticate("shawnH@gmail.com", "secure_pass"); //false
        s+="false=" +  answer + "\n";


        //Test for non-existing accounts (should always be false)
        answer = userRepository.authenticate("JohnDoe@gmail.com", "anonPassword0"); //false
        s+="false=" +  answer + "\n";
        answer = userRepository.authenticate("BobRoss@gmail.com", "proAt Painting"); //false
        s+="false=" +  answer + "\n";
        answer = userRepository.authenticate("hussein.alosman@uottawa.ca", "the most amazing password ever"); //false
        s+="false=" +  answer + "\n";

        s+="\n";
		
		//Tests added for userRepository.isEmailRegistered
		
		//Test for existing accounts (should always be true)
		answer = userRepository.isEmailRegistered("jensenlarge.isaac@gmail.com"); //true
        s+="true=" +  answer + "\n";
		answer = userRepository.isEmailRegistered("ronisemail@gmail.com"); //true
        s+="true=" +  answer + "\n";
		answer = userRepository.isEmailRegistered("admin@gmail.com"); //true
        s+="true=" +  answer + "\n";
		answer = userRepository.isEmailRegistered("rluo123@gmail.com"); //true
        s+="true=" +  answer + "\n";
		answer = userRepository.isEmailRegistered("kdeotare@gmail.com"); //true
        s+="true=" +  answer + "\n";
		answer = userRepository.isEmailRegistered("shawn@gmail.com"); //true
        s+="true=" +  answer + "\n";
		
		//Test for non-existing accounts (should always be false)
		answer = userRepository.isEmailRegistered("JohnDoe@gmail.com"); //false
        s+="false=" +  answer + "\n";
        answer = userRepository.isEmailRegistered("BobRoss@gmail.com"); //false
        s+="false=" +  answer + "\n";
        answer = userRepository.isEmailRegistered("hussein.alosman@uottawa.ca"); //false
        s+="false=" +  answer + "\n";
		
		s+="\n";
		
		//Tests added for userRepository.getUserTypeByEmail
		
		//Test for Attendee accounts (should always be "Attendee")
        temp = userRepository.getUserTypeByEmail("jensenlarge.isaac@gmail.com"); //Attendee
        s+="Attendee=" +  temp + "\n";
        temp = userRepository.getUserTypeByEmail("rluo123@gmail.com"); //Attendee
        s+="Attendee=" +  temp + "\n";
		
		//Test for Organizer accounts (should always be "Organizer")
        temp = userRepository.getUserTypeByEmail("ronisemail@gmail.com"); //Organizer
        s+="Organizer=" +  temp + "\n";
        temp = userRepository.getUserTypeByEmail("kdeotare@gmail.com"); //Organizer
        s+="Organizer=" +  temp + "\n";
		
		//Test for Administrator accounts (should always be "Administrator")
        temp = userRepository.getUserTypeByEmail("shawn@gmail.com"); //Administrator
        s+="Administrator=" +  temp + "\n";
        temp = userRepository.getUserTypeByEmail("admin@gmail.com"); //Administrator
        s+="Administrator=" +  temp + "\n";
		
		//Test for non-existing accounts (should always be null)
		temp = userRepository.getUserTypeByEmail("JohnDoe@gmail.com"); //null
        s+="null=" +  temp + "\n";
        temp = userRepository.getUserTypeByEmail("BobRoss@gmail.com"); //null
        s+="null=" +  temp + "\n";
        temp = userRepository.getUserTypeByEmail("hussein.alosman@uottawa.ca"); //null
        s+="null=" +  temp + "\n";
		
		System.out.println(s);
    }
}
