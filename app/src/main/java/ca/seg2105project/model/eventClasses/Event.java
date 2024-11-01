package ca.seg2105project.model.eventClasses;

import java.time.LocalDate;
import java.time.LocalTime;

/**
* A class to model an Event.
*/
public class Event {
	private String eventID, title, description, eventAddress, organizerEmail;
	private LocalDate date;
	private Local
	private boolean registrationRequired;
	ArrayList<String> attendees, rejectedRequests, pendingRequests;
	
	/**
	* A parameterized constructor for Event.
	* @param eventID a unique id for the event
	* @param title the title of the event
	* @param description the description of the event
	* @param date the date the event is taking place on
	* @param startTime the time the event starts. Must be in 30-minute increments.
	* Still not finished.
	*/
	public Event (String eventID, String title, String description, String date, String startTime, String endTime, String eventAddress, boolean registrationRequired, String organizerEmail) {
		//Still not finished
	}
}