package ca.seg2105project.model.Testers;

import ca.seg2105project.model.Authenticator;

public class AuthenTester {

    Authenticator.authenticate("", ""); //true

    //Test for existing accounts (and added mispellings for it to return false)
//    Authenticator.authenticate("jensenlarge.isaac@gmail.com", "awesomepassword"); //true
//    Authenticator.authenticate("isaac@gmail.com", "awesomepassword"); //false
//    Authenticator.authenticate("jensenlarge.isaac@gmail.com", "THE BEST PASSWORD EVER"); //false
//
//    Authenticator.authenticate("ronisemail@gmail.com", "epicpassword"); //true
//    Authenticator.authenticate("roniemail@gmail.com", "epicpassword"); //false
//    Authenticator.authenticate("ronisemail@gmail.com", "coolpassword"); //false
//
//    Authenticator.authenticate("admin@gmail.com", "adminpwd"); //true
//    Authenticator.authenticate("administrator@gmail.com", "adminpwd"); //false
//    Authenticator.authenticate("admin@gmail.com", "adminpassword"); //false
//
//    Authenticator.authenticate("rluo123@gmail.com", "walkingIsOverrated"); //true
//    Authenticator.authenticate("rluo123@gmail.com", "walkingIsNotOverrated"); //false
//    Authenticator.authenticate("rluo12@gmail.com", "walkingIsOverrated"); //false
//
//    Authenticator.authenticate("kdeotare@gmail.com", "the_best_pass"); //true
//    Authenticator.authenticate("kdeotare@gmail.com", "THE_best_pass"); //false
//    Authenticator.authenticate("kunaladeotare@gmail.com", "the_best_pass"); //false
//
//    Authenticator.authenticate("shawn@gmail.com", "secure_pass"); //true
//    Authenticator.authenticate("shawn@gmail.com", "not_secure_pass"); //false
//    Authenticator.authenticate("shawnH@gmail.com", "secure_pass"); //false
//
//    //Test for non-existing accounts (should always be false)
//    Authenticator.authenticate("JohnDoe@gmail.com", "anonPassword0"); //false
//    Authenticator.authenticate("BobRoss@gmail.com", "proAt Painting"); //false
//    Authenticator.authenticate("hussein.alosman@uottawa.ca", "the most amazing password ever"); //false


}
