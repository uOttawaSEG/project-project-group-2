package ca.seg2105project.model.Testers;

import ca.seg2105project.model.UserRepository;

public class UserRepositoryTester {

    public static void main(String[] args) {
        //Tests added for Authenticator
        String s = "";
        boolean answer;
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

        System.out.println(s);
    }
}
