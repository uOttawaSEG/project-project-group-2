package ca.seg2105project.model.Testers;
import ca.seg2105project.model.userClasses.Administrator;
import ca.seg2105project.model.userClasses.Attendee;
import ca.seg2105project.model.userClasses.Organizer;
import ca.seg2105project.model.userClasses.User;
/**
* A tester class to test the implementation of the classes User, Attendee, Organizer, and Administrator.
*/
public class userClassesTester {
	
	/**
	* For each of the subclasses of User, this method:
	* - creates an object of that type
	* - prints it out using both toString(true) and toString(false)
	* - gets and prints each attribute (only email and password for Administrator, though)
	* - sets each attribute (only email and password for Administrator though) to another value and then calls toString(true) to show the updated attributes
	* - casts it to User (the parent) and then uses toString(true) and toString(false) on it, gets and sets (and gets again) each attribute (only email and password for Administrator, though, and excluding organization name for Organizer as User does not have that attribute)
	* @param args the command line arguments, will not be used
	*/
	public static void main (String[] args) {
		
		//Attendee stuff begins here
		Attendee att = new Attendee ("Kunala", "Deotare", "kdeot090@uottawa.ca", "myPassword", "myAddress", "123-456-789");
		System.out.println("Calling toString() on attendee with password showing:\n" + att.toString(true));
		System.out.println("Calling toString() on attendee without password showing:\n" + att.toString(false));
		System.out.println("Getting attendee's first name: " + att.getFirstName());
		System.out.println("Getting attendee's last name: " + att.getLastName());
		System.out.println("Getting attendee's email address: " + att.getEmail());
		System.out.println("Getting attendee's account password: " + att.getPassword());
		System.out.println("Getting attendee's address: " + att.getAddress());
		System.out.println("Getting attendee's phone number: " + att.getPhoneNumber()+"\n");
		att.setFirstName("Kun");
		att.setLastName("Deo");
		att.setEmail("kdeot090");
		att.setPassword("myNewPassword");
		att.setAddress("myNewAddress");
		att.setPhoneNumber("000-111-222");
		System.out.println("Calling toString() on attendee with password showing after setting new values:\n" + att.toString(true));
		//Attendee stuff ends here
		
		System.out.println("----------------\n");
		
		//Organizer stuff begins here
		Organizer org = new Organizer ("Rachel", "Luo", "rachelluo@uottawa.ca", "herPassword", "herAddress", "987-654-321", "herOrganization");
		System.out.println("Calling toString() on organizer with password showing:\n" + org.toString(true));
		System.out.println("Calling toString() on organizer without password showing:\n" + org.toString(false));
		System.out.println("Getting organizer's first name: " + org.getFirstName());
		System.out.println("Getting organizer's last name: " + org.getLastName());
		System.out.println("Getting organizer's email address: " + org.getEmail());
		System.out.println("Getting organizer's account password: " + org.getPassword());
		System.out.println("Getting organizer's address: " + org.getAddress());
		System.out.println("Getting organizer's phone number: " + org.getPhoneNumber());
		System.out.println("Getting organizer's organization name: " + org.getOrganizationName()+"\n");
		org.setFirstName("Rac");
		org.setLastName("Lu");
		org.setEmail("rachlu");
		org.setPassword("herNewPassword");
		org.setAddress("herNewAddress");
		org.setPhoneNumber("999-888-777");
		org.setOrganizationName("newOrgName");
		System.out.println("Calling toString() on organizer with password showing after setting new values:\n" + org.toString(true));
		//Organizer stuff ends here
		
		System.out.println("----------------\n");
		
		//Administrator stuff begins here
		Administrator adm = new Administrator ("administrator@email.com", "adminPassword");
		System.out.println("Calling toString() on administrator with password showing:\n" + adm.toString(true));
		System.out.println("Calling toString() on administrator without password showing:\n" + adm.toString(false));
		System.out.println("Getting administrator's email address: " + adm.getEmail());
		System.out.println("Getting administrator's account password: " + adm.getPassword()+"\n");
		adm.setEmail("newAdminEmail");
		adm.setPassword("newAdminPassword");
		System.out.println("Calling toString() on administrator with password showing after setting new values:\n" + adm.toString(true));
		//Administrator stuff ends here
		
		System.out.println("----------------\n");
		
		//User casting stuff begins here
		System.out.println("Setting User to the earlier Attendee object.");
		User us = (User) att;
		
		System.out.println("Calling toString() on User (really attendee) with password showing:\n" + us.toString(true));
		System.out.println("Calling toString() on User (really attendee) without password showing:\n" + us.toString(false));
		System.out.println("Getting User (really attendee)'s first name: " + us.getFirstName());
		System.out.println("Getting User (really attendee)'s last name: " + us.getLastName());
		System.out.println("Getting User (really attendee)'s email address: " + us.getEmail());
		System.out.println("Getting User (really attendee)'s account password: " + us.getPassword());
		System.out.println("Getting User (really attendee)'s address: " + us.getAddress());
		System.out.println("Getting User (really attendee)'s phone number: " + us.getPhoneNumber()+"\n");
		us.setFirstName("Kunala");
		us.setLastName("Deotare");
		us.setEmail("kdeot090@uottawa.ca");
		us.setPassword("myNewerPassword");
		us.setAddress("myNewerAddress");
		us.setPhoneNumber("123-456-789");
		System.out.println("Calling toString() on User (really attendee) with password showing after setting new values:\n" + us.toString(true));
		
		System.out.println("Setting User to the earlier Organizer object.");
		us = (User) org;
		
		System.out.println("Calling toString() on User (really organizer) with password showing:\n" + us.toString(true));
		System.out.println("Calling toString() on User (really organizer) without password showing:\n" + us.toString(false));
		System.out.println("Getting User (really organizer)'s first name: " + us.getFirstName());
		System.out.println("Getting User (really organizer)'s last name: " + us.getLastName());
		System.out.println("Getting User (really organizer)'s email address: " + us.getEmail());
		System.out.println("Getting User (really organizer)'s account password: " + us.getPassword());
		System.out.println("Getting User (really organizer)'s address: " + us.getAddress());
		System.out.println("Getting User (really organizer)'s phone number: " + us.getPhoneNumber()+"\n");
		//Cannot do us.getOrganizationName() since User does not have an organizationName
		org.setFirstName("Rachel");
		org.setLastName("Luo");
		org.setEmail("rachelluo@uottawa.ca");
		org.setPassword("herNewerPassword");
		org.setAddress("herNewerAddress");
		org.setPhoneNumber("987-654-321");
		//Cannot do us.setOrganizationName("newerOrgName") since User does not have an organizationName, so organizationName remains the same as at the end of the Organizer section
		System.out.println("Calling toString() on User (really organizer) with password showing after setting new values:\n" + us.toString(true));
		
		System.out.println("Setting User to the earlier Administrator object.");
		us = (User) adm;
		
		System.out.println("Calling toString() on User (really administrator) with password showing:\n" + us.toString(true));
		System.out.println("Calling toString() on User (really administrator) without password showing:\n" + us.toString(false));
		System.out.println("Getting User (really administrator)'s email address: " + us.getEmail());
		System.out.println("Getting User (really administrator)'s account password: " + us.getPassword()+"\n");
		us.setEmail("newerAdminEmail");
		us.setPassword("newerAdminPassword");
		System.out.println("Calling toString() on User (really administrator) with password showing after setting new values:\n" + us.toString(true));
		//User casting stuff ends here
	}
}