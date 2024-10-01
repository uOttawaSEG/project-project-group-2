/**
* A concrete class to model an Organizer.
*/
public class Organizer extends User {
	
	/**
	* An additional instance variable for Organizers.
	*/
	protected String organizationName;
	
	/**
	* A paramterized constructor for Organizer. Calls the parent (User)'s paramterized constructor. Additionally, sets the organizationName too.
	* @param firstName the Organizer's first name
	* @param lastName the Organizer's last name
	* @param emailAddress the Organizer's email address
	* @param accountPassword the Organizer's password
	* @param address the Organizer's living address
	* @param phoneNumber the Organizer's phone number
	* @param organizationName the Organizer's organization name
	*/
	public Organizer (String firstName, String lastName, String emailAddress, String accountPassword, String address, String phoneNumber, String organizationName) {
		super(firstName, lastName, emailAddress, accountPassword, address, phoneNumber);
		this.organizationName = organizationName;
	}
	
	/**
	* A getter for organizationName.
	* @return the organizer's organization name
	*/
	public String getOrganizationName() {
		return this.organizationName;
	}
	
	/**
	* A setter for organizationName.
	* @param organizationName the new name that the organizer's organization name should be set to.
	*/
	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}
	
	/**
	* A method to return the details of the Organizer as a String. A concrete implementation of the parent (User)'s abstract toString() method.
	* @return a String representation of the Organizer
	*/
	public String toString() {
		String ret = ("Organizer " + firstName + " " + lastName + "\n");
		ret += ("\tEmail Address: " + email + "\n");
		ret += ("\tAccount Password: " + password + "\n");
		ret += ("\tAddress: " + address + "\n");
		ret += ("\tPhone Number: " + phoneNumber + "\n");
		ret += ("\tOrganization Name: " + organizationName + "\n");
		return ret;
	}
}