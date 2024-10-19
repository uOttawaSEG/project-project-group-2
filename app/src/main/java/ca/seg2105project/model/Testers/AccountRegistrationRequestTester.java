package ca.seg2105project.model.Testers;

import ca.seg2105project.model.registrationRequestClasses.AccountRegistrationRequest;
import ca.seg2105project.model.registrationRequestClasses.AccountRegistrationRequestStatus;

/**
* A class to test the implementation of AccountRegistrationRequest.java
*/
public class AccountRegistrationRequestTester {
	/**
	* A method to test the implementation of the AccountRegistrationRequest methods.
	* <p>
	* This method creates two requests, one made by an Attendee and one by an Organizer. For each of the requests, this method:
	* <p>
	* - calls and prints out the return values of the getters in AccountRegistrationRequest
	* <p>
	* - sets the status to another AccountRegistrationRequestStatus and prints out the updated status
	* <p>
	* This method prints out a string containing lines of the format "value=returned_value_from_method". As long as the two values on either sides of the = sign are the same, the method is working as intended.
	* @param args the command line arguments, will not be used
	*/
	public static void main (String[] args) {
		String s = "";
		
		//An attendee making a request r1
		AccountRegistrationRequest r1 = new AccountRegistrationRequest("Kunala", "Deotare", "kdeot090@uottawa.ca", "mypassword", "45 Mann", "1234567890", null);
		s += "Kunala=";
		s += r1.getFirstName() + "\n";
		s += "Deotare=";
		s += r1.getLastName() + "\n";
		s += "kdeot090@uottawa.ca=";
		s += r1.getEmail() + "\n";
		s += "mypassword=";
		s += r1.getPassword() + "\n";
		s += "45 Mann=";
		s += r1.getAddress() + "\n";
		s += "1234567890=";
		s += r1.getPhoneNumber() + "\n";
		s += "null=";
		s += r1.getOrganizationName() + "\n";
		s += "PENDING=";
		s += getStatusAsString(r1.getStatus()) + "\n";
		r1.setStatus(AccountRegistrationRequestStatus.REJECTED);
		s += "REJECTED=";
		s += getStatusAsString(r1.getStatus()) + "\n";
		
		s += "\n";
		
		//An organizer making a request r2
		AccountRegistrationRequest r2 = new AccountRegistrationRequest("Rachel", "Luo", "raclu@uottawa.ca", "herpassword", "Uottawa", "0987654321", "Events App");
		s += "Rachel=";
		s += r2.getFirstName() + "\n";
		s += "Luo=";
		s += r2.getLastName() + "\n";
		s += "raclu@uottawa.ca=";
		s += r2.getEmail() + "\n";
		s += "herpassword=";
		s += r2.getPassword() + "\n";
		s += "Uottawa=";
		s += r2.getAddress() + "\n";
		s += "0987654321=";
		s += r2.getPhoneNumber() + "\n";
		s += "Events App=";
		s += r2.getOrganizationName() + "\n";
		s += "PENDING=";
		s += getStatusAsString(r2.getStatus()) + "\n";
		r2.setStatus(AccountRegistrationRequestStatus.APPROVED);
		s += "APPROVED=";
		s += getStatusAsString(r2.getStatus()) + "\n";
		
		System.out.println(s);
	}
	
	/**
	* A private helper method to return a given AccountRegistrationRequestStatus as a String.
	* @param status the status whose String representation is to be returned
	* @return a String representation of the given AccountRegistrationRequestStatus
	*/
	private static String getStatusAsString(AccountRegistrationRequestStatus status) {
		if (status == AccountRegistrationRequestStatus.PENDING) return "PENDING";
		if (status == AccountRegistrationRequestStatus.REJECTED) return "REJECTED";
		if (status == AccountRegistrationRequestStatus.APPROVED) return "APPROVED";
		return null;
	}
}