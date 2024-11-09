package ca.seg2105project.model.testers;

import android.annotation.SuppressLint;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.time.LocalDate;
import java.time.LocalTime;
import ca.seg2105project.model.userClasses.Organizer;
import ca.seg2105project.model.eventClasses.Event;

/**
* A tester class to test the implementation of Event.java
*/
@RequiresApi(api = Build.VERSION_CODES.O)
public class EventTester {
	
	/**
	* A method to test the implementation of Event.java
	* <p>
	* Prints out a string containing lines of format "value=returned_value_from_method". As long as the two values on either sides of the = sign are the same, the method is working as intended.
	*/
	public static void main (String[] args) {
		Organizer org = new Organizer("Mz", "Organizer", "organizer@gmail.com", "theirpassword", "Someplace", "1234567890", "EAMS app");
		ArrayList<Event> orgEvents = org.getEvents();
		
		LocalDate d1 = LocalDate.of(2024, 11, 02);
		LocalTime st1 = LocalTime.of(12, 00);
		LocalTime et1 = LocalTime.of(14, 00);
		Event e1 = new Event("qwertyuiop", "Midterm", "Computer Architecture, I am very cooked.", d1, st1, et1, "SITE 000", "organizer@gmail.com", false);
		
		System.out.println("qwertyuiop=" + e1.getEventID());
		System.out.println("Midterm=" + e1.getTitle());
		System.out.println("Computer Architecture, I am very cooked.=" + e1.getDescription());
		System.out.println(d1.toString() + "=" + e1.getLocalDate().toString());
		System.out.println(st1.toString() + "=" + e1.getLocalStartTime());
		System.out.println(et1.toString() + "=" + e1.getLocalEndTime());
		System.out.println("SITE 000=" + e1.getEventAddress());
		System.out.println("organizer@gmail.com=" + e1.getOrganizerEmail());
		System.out.println("false=" + e1.getRegistrationRequired());
		System.out.println("false=" + (e1.getAttendees() == null));
		System.out.println("true=" + (e1.getPendingRequests() == null));
		System.out.println("true=" + (e1.getRejectedRequests() == null));
		
		System.out.println();
		
		LocalDate d2 = LocalDate.of(2005, 03, 15);
		LocalTime st2 = LocalTime.of(8, 00);
		LocalTime et2 = LocalTime.of(23, 30);
		Event e2 = new Event("asdfghjkl", "My Birthday", "The day I was born.", d2, st2, et2, "Some Medical Hospital", "organizer@gmail.com", true);
		
		System.out.println("asdfghjkl=" + e2.getEventID());
		System.out.println("My Birthday=" + e2.getTitle());
		System.out.println("The day I was born.=" + e2.getDescription());
		System.out.println(d2.toString() + "=" + e2.getLocalDate().toString());
		System.out.println(st2.toString() + "=" + e2.getLocalStartTime().toString());
		System.out.println(et2.toString() + "=" + e2.getLocalEndTime().toString());
		System.out.println("Some Medical Hospital=" + e2.getEventAddress());
		System.out.println("organizer@gmail.com=" + e2.getOrganizerEmail());
		System.out.println("true=" + e2.getRegistrationRequired());
		System.out.println("false=" + (e2.getAttendees() == null));
		System.out.println("false=" + (e2.getPendingRequests() == null));
		System.out.println("false=" + (e2.getRejectedRequests() == null));
		
		System.out.println();
		
		System.out.println("0=" + orgEvents.size());
		orgEvents.add(e1);
		orgEvents.add(e2);
		System.out.println("2=" + orgEvents.size());
		
		System.out.println();

		e1.getAttendees().add("raclu@uottawa.ca");
		e1.getAttendees().add("ij@uottawa.ca");
		e1.getAttendees().add("s@uottawa.ca");
		e1.getAttendees().add("r@uottawa.ca");
		System.out.println("raclu@uottawa.ca=" + e1.getAttendees().get(0));
		System.out.println("ij@uottawa.ca=" + e1.getAttendees().get(1));
		System.out.println("s@uottawa.ca=" + e1.getAttendees().get(2));
		System.out.println("r@uottawa.ca=" + e1.getAttendees().get(3));
		
		System.out.println();
		
		System.out.println("0=" + e2.getAttendees().size());
		e2.getAttendees().add("youngme@birth.in");
		System.out.println("1=" + e2.getAttendees().size());
		
		System.out.println();
		System.out.println("0=" + e2.getPendingRequests().size());
		System.out.println("0=" + e2.getRejectedRequests().size());


		LocalDate d3 = LocalDate.of(2024, 11, 8);
		LocalTime st3 = LocalTime.of(00, 00);
		LocalTime et3 = LocalTime.of(23, 59);
		Event e3 = new Event("12345", "Assignment 2 SEG", "Client-Server Assignment Chat System", d3, st3, et3, "DMS1600", "organizer@gmail.com", true);

		System.out.println("12345=" + e3.getEventID());
		System.out.println("Assignment 2 SEG=" + e3.getTitle());
		System.out.println("Client-Server Assignment Chat System=" + e3.getDescription());
		System.out.println(d3.toString() + "=" + e3.getLocalDate().toString());
		System.out.println(st3.toString() + "=" + e3.getLocalStartTime().toString());
		System.out.println(et3.toString() + "=" + e3.getLocalEndTime().toString());
		System.out.println("DMS1600=" + e3.getEventAddress());
		System.out.println("organizer@gmail.com=" + e3.getOrganizerEmail());
		System.out.println("true=" + e3.getRegistrationRequired());


		System.out.println("0=" + (e3.getAttendees().size()));
		System.out.println("0=" + (e3.getPendingRequests().size()));
		System.out.println("0=" + (e3.getRejectedRequests().size()));


		e3.getAttendees().add("ImIn@gmail.com");
		e3.getRejectedRequests().add("ImRejected@hotmail.com");
		e3.getRejectedRequests().add("ImRejected2@hotmail.com");
		e3.getPendingRequests().add("ImWaiting@outlook.com");
		e3.getPendingRequests().add("ImWaiting2@outlook.com");
		e3.getPendingRequests().add("ImWaiting3@outlook.com");

		System.out.println("1=" + (e3.getAttendees().size()));
		System.out.println("3=" + (e3.getPendingRequests().size()));
		System.out.println("2=" + (e3.getRejectedRequests().size()));

		System.out.println();
	}
}