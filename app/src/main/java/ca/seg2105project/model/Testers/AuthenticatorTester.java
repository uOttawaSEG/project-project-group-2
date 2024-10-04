/**
* A class to test the implementation of the class Authenticator.
*/
public class AuthenticatorTester {
	
	/**
	* A method to test multiple different calls to Authenticator.authenticate method, trying different combinations of correct and incorrect emails and passwords.
	* @param args the command line arguments, will not be used
	*/
	public static void main (String[] args) {
		System.out.println("Should be true: " + Authenticator.authenticate("jensenlarge.isaac@gmail.com", "awesomepassword")); //true
		System.out.println("Should be false: " + Authenticator.authenticate("isaac@gmail.com", "awesomepassword")); //false
		System.out.println("Should be false: " + Authenticator.authenticate("jensenlarge.isaac@gmail.com", "THE BEST PASSWORD EVER")); //false
		
		System.out.println("Should be true: " + Authenticator.authenticate("ronisemail@gmail.com", "epicpassword")); //true
		System.out.println("Should be false: " + Authenticator.authenticate("roniemail@gmail.com", "epicpassword")); //false
		System.out.println("Should be false: " + Authenticator.authenticate("ronisemail@gmail.com", "coolpassword")); //false
		
		System.out.println("Should be true: " + Authenticator.authenticate("admin@gmail.com", "adminpwd")); //true
		System.out.println("Should be false: " + Authenticator.authenticate("administrator@gmail.com", "adminpwd")); //false
		System.out.println("Should be false: " + Authenticator.authenticate("admin@gmail.com", "adminpassword")); //false
		
		System.out.println("Should be true: " + Authenticator.authenticate("rluo123@gmail.com", "walkingIsOverrated")); //true
		System.out.println("Should be false: " + Authenticator.authenticate("rluo123@gmail.com", "walkingIsNotOverrated")); //false
		System.out.println("Should be false: " + Authenticator.authenticate("rluo12@gmail.com", "walkingIsOverrated")); //false
		
		System.out.println("Should be true: " + Authenticator.authenticate("kdeotare@gmail.com", "the_best_pass")); //true
		System.out.println("Should be false: " + Authenticator.authenticate("kdeotare@gmail.com", "THE_best_pass")); //false
		System.out.println("Should be false: " + Authenticator.authenticate("kunaladeotare@gmail.com", "the_best_pass")); //false
		
		System.out.println("Should be true: " + Authenticator.authenticate("shawn@gmail.com", "secure_pass")); //true
		System.out.println("Should be false: " + Authenticator.authenticate("shawn@gmail.com", "not_secure_pass")); //false
		System.out.println("Should be false: " + Authenticator.authenticate("shawnH@gmail.com", "secure_pass")); //false
		
		//Test for non-existing accounts (should always be false)
		System.out.println("Should be false: " + Authenticator.authenticate("JohnDoe@gmail.com", "anonPassword0")); //false
		System.out.println("Should be false: " + Authenticator.authenticate("BobRoss@gmail.com", "proAt Painting")); //false
		System.out.println("Should be false: " + Authenticator.authenticate("hussein.alosman@uottawa.ca", "the most amazing password ever")); //false
	}
}