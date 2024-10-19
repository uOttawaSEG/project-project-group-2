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

		s += "\n";


		//An attendee making a request r3
		AccountRegistrationRequest r3 = new AccountRegistrationRequest("Roni", "N", "rn@uottawa.ca", "pass", "Uottawa", "4161231234", null);
		s += "Roni=";
		s += r3.getFirstName() + "\n";
		s += "N=";
		s += r3.getLastName() + "\n";
		s += "rn@uottawa.ca=";
		s += r3.getEmail() + "\n";
		s += "pass=";
		s += r3.getPassword() + "\n";
		s += "Uottawa=";
		s += r3.getAddress() + "\n";
		s += "4161231234=";
		s += r3.getPhoneNumber() + "\n";
		s += "null=";
		s += r3.getOrganizationName() + "\n";
		s += "PENDING=";
		s += getStatusAsString(r3.getStatus()) + "\n";
		r3.setStatus(AccountRegistrationRequestStatus.APPROVED);
		s += "APPROVED=";
		s += getStatusAsString(r3.getStatus()) + "\n";

		s += "\n";


		//An organizer making a request r4
		AccountRegistrationRequest r4 = new AccountRegistrationRequest("Shawn", "H", "sh@uottawa.ca", "password", "somewhere", "1239876543", "EAMS");
		s += "Shawn=";
		s += r4.getFirstName() + "\n";
		s += "H=";
		s += r4.getLastName() + "\n";
		s += "sh@uottawa.ca=";
		s += r4.getEmail() + "\n";
		s += "password=";
		s += r4.getPassword() + "\n";
		s += "somewhere=";
		s += r4.getAddress() + "\n";
		s += "1239876543=";
		s += r4.getPhoneNumber() + "\n";
		s += "EAMS=";
		s += r4.getOrganizationName() + "\n";
		s += "PENDING=";
		s += getStatusAsString(r4.getStatus()) + "\n";
		r4.setStatus(AccountRegistrationRequestStatus.REJECTED);
		s += "REJECTED=";
		s += getStatusAsString(r4.getStatus()) + "\n";

		s += "\n";


		//An organizer making a request r5
		AccountRegistrationRequest r5 = new AccountRegistrationRequest("Issac", "J", "ij@uottawa.ca", "hispassword", "somewhere as well", "1111111111", "EAMS");
		s += "Issac=";
		s += r5.getFirstName() + "\n";
		s += "J=";
		s += r5.getLastName() + "\n";
		s += "ij@uottawa.ca=";
		s += r5.getEmail() + "\n";
		s += "hispassword=";
		s += r5.getPassword() + "\n";
		s += "somewhere as well=";
		s += r5.getAddress() + "\n";
		s += "1111111111=";
		s += r5.getPhoneNumber() + "\n";
		s += "EAMS=";
		s += r5.getOrganizationName() + "\n";
		s += "PENDING=";
		s += getStatusAsString(r5.getStatus()) + "\n";
		r5.setStatus(AccountRegistrationRequestStatus.APPROVED);
		s += "APPROVED=";
		s += getStatusAsString(r5.getStatus()) + "\n";

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