package ca.seg2105project.model.Testers;

import ca.seg2105project.model.UserRepository;
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

        UserRepository.init();


        UserRepository.registerUser(new Attendee("Isaac", "Jensen-Large",
                "jensenlarge.isaac@gmail.com", "awesomepassword",
                "54 Awesome St.", "6139835504"));

        UserRepository.registerUser(new Organizer("Roni", "Nartatez",
                "ronisemail@gmail.com", "epicpassword",
                "57 Awesome St.", "6131234567", "Awesome Org."));

        UserRepository.registerUser(new Attendee("Rachel", "Luo",
                "rluo123@gmail.com", "walkingIsOverrated",
                "39 Mann", "6471234567"));

        UserRepository.registerUser(new Organizer("Kunala", "Deotare",
                "kdeotare@gmail.com", "the_best_pass",
                "29 Mann", "4161234567", "Best Org."));

        UserRepository.registerUser(new Administrator("shawn@gmail.com",
                "secure_pass"));
		
        //Tests added for UserRepository.authenticate
        String s = "";
        boolean answer;
		String temp = "";
        answer = UserRepository.authenticate("", ""); //false
        s+= "false=" + answer + "\n";

        //Test for existing accounts (and added mispellings for it to return false)
        answer = UserRepository.authenticate("jensenlarge.isaac@gmail.com", "awesomepassword"); //true
        s+= "true=" + answer + "\n";
        answer = UserRepository.authenticate("isaac@gmail.com", "awesomepassword"); //false
        s+= "false=" + answer + "\n";
        answer = UserRepository.authenticate("jensenlarge.isaac@gmail.com", "THE BEST PASSWORD EVER"); //false
        s+= "false=" + answer + "\n";

        answer = UserRepository.authenticate("ronisemail@gmail.com", "epicpassword"); //true
        s+="true=" +  answer + "\n";
        answer = UserRepository.authenticate("roniemail@gmail.com", "epicpassword"); //false
        s+="false=" +  answer + "\n";
        answer = UserRepository.authenticate("ronisemail@gmail.com", "coolpassword"); //false
        s+="false=" +  answer + "\n";

        answer = UserRepository.authenticate("admin@gmail.com", "adminpwd"); //true
        s+="true=" +  answer + "\n";
        answer = UserRepository.authenticate("administrator@gmail.com", "adminpwd"); //false
        s+="false=" +  answer + "\n";
        answer = UserRepository.authenticate("admin@gmail.com", "adminpassword"); //false
        s+="false=" +  answer + "\n";

        answer = UserRepository.authenticate("rluo123@gmail.com", "walkingIsOverrated"); //true
        s+="true=" +  answer + "\n";
        answer = UserRepository.authenticate("rluo123@gmail.com", "walkingIsNotOverrated"); //false
        s+="false=" +  answer + "\n";
        answer = UserRepository.authenticate("rluo12@gmail.com", "walkingIsOverrated"); //false
        s+="false=" +  answer + "\n";

        answer = UserRepository.authenticate("kdeotare@gmail.com", "the_best_pass"); //true
        s+="true=" +  answer + "\n";
        answer = UserRepository.authenticate("kdeotare@gmail.com", "THE_best_pass"); //false
        s+="false=" +  answer + "\n";
        answer = UserRepository.authenticate("kunaladeotare@gmail.com", "the_best_pass"); //false
        s+="false=" +  answer + "\n";

        answer = UserRepository.authenticate("shawn@gmail.com", "secure_pass"); //true
        s+="true=" +  answer + "\n";
        answer = UserRepository.authenticate("shawn@gmail.com", "not_secure_pass"); //false
        s+="false=" +  answer + "\n";
        answer = UserRepository.authenticate("shawnH@gmail.com", "secure_pass"); //false
        s+="false=" +  answer + "\n";


        //Test for non-existing accounts (should always be false)
        answer = UserRepository.authenticate("JohnDoe@gmail.com", "anonPassword0"); //false
        s+="false=" +  answer + "\n";
        answer = UserRepository.authenticate("BobRoss@gmail.com", "proAt Painting"); //false
        s+="false=" +  answer + "\n";
        answer = UserRepository.authenticate("hussein.alosman@uottawa.ca", "the most amazing password ever"); //false
        s+="false=" +  answer + "\n";

        s+="\n";
		
		//Tests added for UserRepository.isEmailRegistered
		
		//Test for existing accounts (should always be true)
		answer = UserRepository.isEmailRegistered("jensenlarge.isaac@gmail.com"); //true
        s+="true=" +  answer + "\n";
		answer = UserRepository.isEmailRegistered("ronisemail@gmail.com"); //true
        s+="true=" +  answer + "\n";
		answer = UserRepository.isEmailRegistered("admin@gmail.com"); //true
        s+="true=" +  answer + "\n";
		answer = UserRepository.isEmailRegistered("rluo123@gmail.com"); //true
        s+="true=" +  answer + "\n";
		answer = UserRepository.isEmailRegistered("kdeotare@gmail.com"); //true
        s+="true=" +  answer + "\n";
		answer = UserRepository.isEmailRegistered("shawn@gmail.com"); //true
        s+="true=" +  answer + "\n";
		
		//Test for non-existing accounts (should always be false)
		answer = UserRepository.isEmailRegistered("JohnDoe@gmail.com"); //false
        s+="false=" +  answer + "\n";
        answer = UserRepository.isEmailRegistered("BobRoss@gmail.com"); //false
        s+="false=" +  answer + "\n";
        answer = UserRepository.isEmailRegistered("hussein.alosman@uottawa.ca"); //false
        s+="false=" +  answer + "\n";
		
		s+="\n";
		
		//Tests added for UserRepository.getUserTypeByEmail
		
		//Test for Attendee accounts (should always be "Attendee")
        temp = UserRepository.getUserTypeByEmail("jensenlarge.isaac@gmail.com"); //Attendee
        s+="Attendee=" +  temp + "\n";
        temp = UserRepository.getUserTypeByEmail("rluo123@gmail.com"); //Attendee
        s+="Attendee=" +  temp + "\n";
		
		//Test for Organizer accounts (should always be "Organizer")
        temp = UserRepository.getUserTypeByEmail("ronisemail@gmail.com"); //Organizer
        s+="Organizer=" +  temp + "\n";
        temp = UserRepository.getUserTypeByEmail("kdeotare@gmail.com"); //Organizer
        s+="Organizer=" +  temp + "\n";
		
		//Test for Administrator accounts (should always be "Administrator")
        temp = UserRepository.getUserTypeByEmail("shawn@gmail.com"); //Administrator
        s+="Administrator=" +  temp + "\n";
        temp = UserRepository.getUserTypeByEmail("admin@gmail.com"); //Administrator
        s+="Administrator=" +  temp + "\n";
		
		//Test for non-existing accounts (should always be null)
		temp = UserRepository.getUserTypeByEmail("JohnDoe@gmail.com"); //null
        s+="null=" +  temp + "\n";
        temp = UserRepository.getUserTypeByEmail("BobRoss@gmail.com"); //null
        s+="null=" +  temp + "\n";
        temp = UserRepository.getUserTypeByEmail("hussein.alosman@uottawa.ca"); //null
        s+="null=" +  temp + "\n";
		
		System.out.println(s);
    }
}
