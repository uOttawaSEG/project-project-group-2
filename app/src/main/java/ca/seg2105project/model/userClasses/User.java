package ca.seg2105project.model.userClasses;

/**
 * An Abstract class for user 
 * Used by the children classes to extend and get the variables inside that they all share 
 */
public abstract class User { 
    
	/**
	* Instance variables for User and its subclasses.
	*/
    protected String firstName, lastName, email, password, address, phoneNumber;  

    /**
     * An empty constructor in order to make reading users from fb possible.
     */
    public User() {}

    /** 
     * constructor children will call super() on 
     * 
     * @param firstName the first name of the user 
     * @param lastName the last name of the user 
     * @param email the email of the user 
     * @param password the password of the user 
     * @param address the address of the user 
     * @param phoneNumber the phone number of the user 
     */ 
    protected User(String firstName, String lastName, String email, String password, String address, String phoneNumber) {
        this.firstName = firstName; 
        this.lastName = lastName; 
        this.email = email; 
        this.password = password; 
        this.address = address; 
        this.phoneNumber = phoneNumber; 
    }

    
    //getter methods 

    /** 
     * Gets the first name of the user 
     * @return the first name of the user
     */
    public String getFirstName() {
        return firstName; 
    }

    /**
    * Gets the last name of the user 
    * @return the last name of the user
    */
    public String getLastName() {
        return lastName;
    }

    /**
     * Gets the email of the user 
     * @return the email of the user
     */
    public String getEmail() {
        return email; 
    }
    
    /**
     * Gets the password of the user 
     * @return the password of the user
     */
    public String getPassword() {
        return password; 
    }

    /**
     * Gets the address of the user 
     * @return the address of the user
     */
    public String getAddress() {
        return address; 
    } 

    /**
     * Gets the phone number of the user 
     * @return the phone number of the user
     */
    public String getPhoneNumber() {
        return phoneNumber; 
    }


    //setter methods 
    /**
     * Sets the first name of the user  
     * @param firstName the first name of the user 
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Sets the last name of the user  
     * @param lastName the last name of the user 
     */
    public void setLastName(String lastName) {
        this.lastName = lastName; 
    }

    /**
     * Sets the email of the user  
     * @param email the email of the user 
     */
    public void setEmail(String email) {
        this.email = email; 
    }
    
    /**
     * Sets the password of the user  
     * @param password the password of the user 
     */
    public void setPassword(String password) {
        this.password = password; 
    }

    /** 
     * Sets the address of the user  
     * @param address the address of the user 
     */
    public void setAddress(String address) {
        this.address = address; 
    }

    /** 
     * Sets the phone number of the user  
     * @param phoneNumber the phone number of the user 
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;  
    } 

    /**
     * Forces the children classes to have a toString() method that returns the details of the user. Includes the password in the details if the parameter includePassword == true, othewise omits it.
	 * @param includePassword determines whether or not to include the password in the (returned) details of the user
	 * @return a representation of the user as a String
     */
    public abstract String toString(boolean includePassword); 

 
}