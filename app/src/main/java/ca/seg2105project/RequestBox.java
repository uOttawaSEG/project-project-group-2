package ca.seg2105project;

public class RequestBox {

    String firstName;
    String lastName;
    String email;
    String phoneNumber;
    String address;
    String organizationName;

    int image;

    //Constructor that creates a requestBox for Attendee
    public RequestBox(String firstName, String lastName, String email, String phoneNumber, String address, int image) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.image = image;
    }

    //Constructor that creates a requestBox for Organiser
    public RequestBox(String firstName, String lastName, String email, String phoneNumber, String address, String organizationName, int image) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.organizationName = organizationName;
        this.image = image;
    }

    //getters
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public int getImage() {
        return image;
    }

    //setters
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
