/**
* A concrete class to model an Administrator. An administrator only has an email address and a password but is made a child of User (and thus technically has access to far more instance variables) in order to remain consistent with the 'isa' rule as an Administrator is a User.
* <p>
* The instance variables that Administrator inherits from User that it does not 'use' are set to null.
*/
public class Administrator extends User {
	/**
	* A parameterized constructor for Administrator. Calls the parent (User)'s parameterized constructor.
	* @param emailAddress the email address to set this Administrator's email address to.
	* @param accountPassword the account password to set this Administrator's account password to.
	*/
	public Administrator (String emailAddress, String accountPassword) {
		super(null, null, emailAddress, accountPassword, null, null);
	}
	
	/**
	* A method to return the details of the Administrator. A concrete implementation of the parent (User)'s abstract toString() method.
	* @param includePassword determines whether or not to include the account password in the (returned) details of the administrator
	* @return a String representation of the Administrator
	*/
	public String toString(boolean includePassword) {
		String ret = ("Administrator Email Address: " + email + "\n");
		if (includePassword) ret += ("\tAccount Password: " + password + "\n");
		return ret;
	}
}