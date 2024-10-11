package ca.seg2105project.model.Testers;

import ca.seg2105project.model.UserRepository;

/**
* A class to test the methods in UserRepository.java
*/
public class UserRepositoryTester {

	/**
	* A method to test the implementation of the UserRepository methods authenticateEmailAndPassword, authenticateEmail, and getUserTypeByEmail. 
	* Prints out a string containing lines of format "value=returned_value_from_method". As long as the two values on either sides of the = sign are the same, the method is working as intended.
	* @param args the command line arguments, will not be used
	*/
    public static void main(String[] args) {
		
        //Tests added for UserRepository.authenticateEmailAndPassword
        String s = "";
        boolean answer;
		String temp = "";
        answer = UserRepository.authenticateEmailAndPassword("", ""); //false
        s+= "false=" + answer + "\n";

        //Test for existing accounts (and added mispellings for it to return false)
        answer = UserRepository.authenticateEmailAndPassword("jensenlarge.isaac@gmail.com", "awesomepassword"); //true
        s+= "true=" + answer + "\n";
        answer = UserRepository.authenticateEmailAndPassword("isaac@gmail.com", "awesomepassword"); //false
        s+= "false=" + answer + "\n";
        answer = UserRepository.authenticateEmailAndPassword("jensenlarge.isaac@gmail.com", "THE BEST PASSWORD EVER"); //false
        s+= "false=" + answer + "\n";

        answer = UserRepository.authenticateEmailAndPassword("ronisemail@gmail.com", "epicpassword"); //true
        s+="true=" +  answer + "\n";
        answer = UserRepository.authenticateEmailAndPassword("roniemail@gmail.com", "epicpassword"); //false
        s+="false=" +  answer + "\n";
        answer = UserRepository.authenticateEmailAndPassword("ronisemail@gmail.com", "coolpassword"); //false
        s+="false=" +  answer + "\n";

        answer = UserRepository.authenticateEmailAndPassword("admin@gmail.com", "adminpwd"); //true
        s+="true=" +  answer + "\n";
        answer = UserRepository.authenticateEmailAndPassword("administrator@gmail.com", "adminpwd"); //false
        s+="false=" +  answer + "\n";
        answer = UserRepository.authenticateEmailAndPassword("admin@gmail.com", "adminpassword"); //false
        s+="false=" +  answer + "\n";

        answer = UserRepository.authenticateEmailAndPassword("rluo123@gmail.com", "walkingIsOverrated"); //true
        s+="true=" +  answer + "\n";
        answer = UserRepository.authenticateEmailAndPassword("rluo123@gmail.com", "walkingIsNotOverrated"); //false
        s+="false=" +  answer + "\n";
        answer = UserRepository.authenticateEmailAndPassword("rluo12@gmail.com", "walkingIsOverrated"); //false
        s+="false=" +  answer + "\n";

        answer = UserRepository.authenticateEmailAndPassword("kdeotare@gmail.com", "the_best_pass"); //true
        s+="true=" +  answer + "\n";
        answer = UserRepository.authenticateEmailAndPassword("kdeotare@gmail.com", "THE_best_pass"); //false
        s+="false=" +  answer + "\n";
        answer = UserRepository.authenticateEmailAndPassword("kunaladeotare@gmail.com", "the_best_pass"); //false
        s+="false=" +  answer + "\n";

        answer = UserRepository.authenticateEmailAndPassword("shawn@gmail.com", "secure_pass"); //true
        s+="true=" +  answer + "\n";
        answer = UserRepository.authenticateEmailAndPassword("shawn@gmail.com", "not_secure_pass"); //false
        s+="false=" +  answer + "\n";
        answer = UserRepository.authenticateEmailAndPassword("shawnH@gmail.com", "secure_pass"); //false
        s+="false=" +  answer + "\n";


        //Test for non-existing accounts (should always be false)
        answer = UserRepository.authenticateEmailAndPassword("JohnDoe@gmail.com", "anonPassword0"); //false
        s+="false=" +  answer + "\n";
        answer = UserRepository.authenticateEmailAndPassword("BobRoss@gmail.com", "proAt Painting"); //false
        s+="false=" +  answer + "\n";
        answer = UserRepository.authenticateEmailAndPassword("hussein.alosman@uottawa.ca", "the most amazing password ever"); //false
        s+="false=" +  answer + "\n";

        s+="\n";
		
		//Tests added for UserRepository.authenticateEmail
		
		//Test for existing accounts (should always be true)
		answer = UserRepository.authenticateEmail("jensenlarge.isaac@gmail.com"); //true
        s+="true=" +  answer + "\n";
		answer = UserRepository.authenticateEmail("ronisemail@gmail.com"); //true
        s+="true=" +  answer + "\n";
		answer = UserRepository.authenticateEmail("admin@gmail.com"); //true
        s+="true=" +  answer + "\n";
		answer = UserRepository.authenticateEmail("rluo123@gmail.com"); //true
        s+="true=" +  answer + "\n";
		answer = UserRepository.authenticateEmail("kdeotare@gmail.com"); //true
        s+="true=" +  answer + "\n";
		answer = UserRepository.authenticateEmail("shawn@gmail.com"); //true
        s+="true=" +  answer + "\n";
		
		//Test for non-existing accounts (should always be false)
		answer = UserRepository.authenticateEmail("JohnDoe@gmail.com"); //false
        s+="false=" +  answer + "\n";
        answer = UserRepository.authenticateEmail("BobRoss@gmail.com"); //false
        s+="false=" +  answer + "\n";
        answer = UserRepository.authenticateEmail("hussein.alosman@uottawa.ca"); //false
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
