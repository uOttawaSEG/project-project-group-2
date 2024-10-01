/**
* A concrete class to model an Attendee.
*/
public class Attendee extends User {
	/**
	* A paramterized constructor for Attendee. Calls the parent (User)'s paramterized constructor.
	* @param firstName the Attendee's first name
	* @param lastName the Attendee's last name
	* @param emailAddress the Attendee's email address
	* @param accountPassword the Attendee's password
	* @param address the Attendee's living address
	* @param phoneNumber the Attendee's phone number
	*/
	public Attendee (String firstName, String lastName, String emailAddress, String accountPassword, String address, String phoneNumber) {
		super(firstName, lastName, emailAddress, accountPassword, address, phoneNumber);
	}
	
	/**
	* A method to return the details of the Attendee as a String. A concrete implementation of the parent (User)'s abstract toString() method.
	* @return String a String representation of the Attendee
	*/
	public String toString() {
		String ret = ("Attendee " + firstName + " " + lastName + "\n");
		ret += ("\tEmail Address: " + email + "\n");
		ret += ("\tAccount Password: " + password + "\n");
		ret += ("\tAddress: " + address + "\n");
		ret += ("\tPhone Number: " + phoneNumber + "\n");
		return ret;
	}
}