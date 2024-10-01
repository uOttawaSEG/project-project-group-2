public abstract class User {
	
/* 
An Abstract class for user 
Used by the children classes to extend and get the variables inside that they all share
 */
public abstract class User { 
    //variables all children will need
    protected String firstName, lastName, email, password, address, phoneNumber;  


    //constructor children will call super() on 
    protected User(String firstName, String lastName, String email, String password, String address, String phoneNumber) {
        this.firstName = firstName; 
        this.lastName = lastName; 
        this.email = email; 
        this.password = password; 
        this.address = address; 
        this.phoneNumber = phoneNumber; 
    }

    
    //getter methods 
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


    //setter methods 
    protected void setFirstName(String firstName) {
        this.firstName = firatName; 
    }

    protected void setLastName(String lastName) {
        this.lastName = lastName; 
    }

    protected void setEmail(String email) {
        this.email = email; 
    }
    
    protected void setPassword(String password) {
        this.password = password; 
    }

    protected void setAddress(String address) {
        this.address = address; 
    }

    protected void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;  
    }
>>>>>>> c69eb8c96dbac32eabd8d8064aecd1137fd94049
}