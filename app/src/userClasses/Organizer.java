/**
* A concrete class to model an Organizer.
*/
public class Organizer extends User {
	
	protected String organizationName; //the only unique field the Organizer has compared to User
	
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
	* @return String the organizer's organization name
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
}