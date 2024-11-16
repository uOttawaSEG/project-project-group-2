package ca.seg2105project.model.registrationRequestClasses;

/**
* A class to model an account registration request. This class will distinguish the type of user that made this request (only Organizer or Attendee, since Administrator does not register) depending on the value of organizationName. If it is null, this request was made by an Attendee. Otherwise, this request was made by an Organizer.
*/
public class AccountRegistrationRequest {
	private String firstName, lastName, email, password, address, phoneNumber, organizationName;
	private RegistrationRequestStatus status;
	
	/** 
     * A paramterized constructor for AccountRegistrationRequest. A request is PENDING to begin with and can be REJECTED or APPROVED later.
     * @param firstName the first name of the user making the request
     * @param lastName the last name of the user making the request
     * @param email the email of the user making the request
     * @param password the password of the user making the request
     * @param address the address of the user making the request
     * @param phoneNumber the phone number of the user making the request
	 * @param organizationName the name of the organization of the user making the request (null if an Attendee is making the request)
     */ 
	public AccountRegistrationRequest (String firstName, String lastName, String email, String password, String address, String phoneNumber, String organizationName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.organizationName = organizationName;

        //when a request is first created, the status is always PENDING until an admin approves it.
        this.status = RegistrationRequestStatus.PENDING;
    }

    public AccountRegistrationRequest() {

    }
	
	// Getter methods
	
	/** 
     * Gets the first name of the user making the request
     * @return the first name of the user making the request
     */
    public String getFirstName() {
        return firstName; 
    }

    /**
    * Gets the last name of the user making the request
    * @return the last name of the user making the request
    */
    public String getLastName() {
        return lastName;
    }

    /**
     * Gets the email of the user making the request
     * @return the email of the user making the request
     */
    public String getEmail() {
        return email; 
    }
    
    /**
     * Gets the password of the user making the request
     * @return the password of the user making the request
     */
    public String getPassword() {
        return password; 
    }

    /**
     * Gets the address of the user making the request
     * @return the address of the user making the request
     */
    public String getAddress() {
        return address; 
    } 

    /**
     * Gets the phone number of the user making the request
     * @return the phone number of the user making the request
     */
    public String getPhoneNumber() {
        return phoneNumber; 
    }
	
	/**
     * Gets the name of the organization of the user making the request
     * @return the name of the organization of the user making the request (null if an Attendee made the request)
     */
    public String getOrganizationName() {
        return organizationName; 
    }
	
	/**
     * Gets the status of the request (PENDING, REJECTED, or APPROVED)
     * @return the status of the request as an enum constant AccountRegistrationRequestStatus (PENDING, REJECTED, or APPROVED)
     */
    public RegistrationRequestStatus getStatus() {
        return status; 
    }
	
	// Setter methods
	
	/**
	* Sets the status of the request to a given AccountRegistrationRequestStatus
	* @param status the new status to set the status of the request to
	*/
	public void setStatus(RegistrationRequestStatus status) {
		this.status = status;
	}
}