/* 
 * An Abstract class for user 
 * Used by the children classes to extend and get the variables inside that they all share 
 */
public abstract class User { 
    //variables all children will need
    protected String firstName, lastName, email, password, address, phoneNumber;  


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
    protected String getFirstName() {
        return firstName; 
    }

    /**
    * Gets the last name of the user 
    * @return the last name of the user
    */
    protected String getLastName() {
        return getLastName; 
    }

    /**
     * Gets the email of the user 
     * @return the email of the user
     */
    protected String getEmail() {
        return email; 
    }
    
    /**
     * Gets the password of the user 
     * @return the password of the user
     */
    protected String getPassword() {
        return password; 
    }

    /**
     * Gets the address of the user 
     * @return the address of the user
     */
    protected String getAddress() {
        return address; 
    } 

    /**
     * Gets the phone number of the user 
     * @return the phone number of the user
     */
    protected String getPhoneNumber() {
        return phoneNumber; 
    }


    //setter methods 
    /**
     * Sets the first name of the user  
     * @param firstName the first name of the user 
     */
    protected void setFirstName(String firstName) {
        this.firstName = firatName; 
    }

    /**
     * Sets the last name of the user  
     * @param lastName the last name of the user 
     */
    protected void setLastName(String lastName) {
        this.lastName = lastName; 
    }

    /**
     * Sets the email of the user  
     * @param email the email of the user 
     */
    protected void setEmail(String email) {
        this.email = email; 
    }
    
    /**
     * Sets the password of the user  
     * @param password the password of the user 
     */
    protected void setPassword(String password) {
        this.password = password; 
    }

    /** 
     * Sets the address of the user  
     * @param address the address of the user 
     */
    protected void setAddress(String address) {
        this.address = address; 
    }

    /** 
     * Sets the phone number of the user  
     * @param phoneNumber the phone number of the user 
     */
    protected void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;  
    } 

    /*
     * Forces the children classes to have a toString() method that returns the details of the user 
     */
    public abstract String toString(); 

 
}