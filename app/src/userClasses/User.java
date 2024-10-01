/* 
An Abstract class for user 
Used by the children classes to extend and get the variables inside that they all share 
 */

public abstract class User { 
    protected String firstName, lastName, email, password, address, phoneNumber;  

    protected String getFirstName() {
        return firstName; 
    }

    protected String getLastName() {
        return getLastName; 
    }

    protected String getEmail() {
        return email; 
    }
    
    protected String getPassword() {
        return password; 
    }
    protected String getAddress() {
        return address; 
    }
    protected String getPhoneNumber() {
        return phoneNumber; 
    }
}