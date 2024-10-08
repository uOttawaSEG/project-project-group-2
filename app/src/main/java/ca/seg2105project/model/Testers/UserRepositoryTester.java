package ca.seg2105project.model.Testers;

import ca.seg2105project.model.UserRepository;

public class UserRepositoryTester {

    public static void main(String[] args) {
        //Tests added for Authenticator
        String s = "";
        boolean answer;
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

        System.out.println(s);
    }
}
