package ca.seg2105project.model.eventClasses;

import java.time.LocalDate;
import java.time.LocalTime;

/**
* A class to model an Event.
*/
public class Event {
	private String eventID, title, description, eventAddress, organizerEmail;
	private LocalDate date;
	private LocalTime startTime, endTime;
	private boolean registrationRequired;
	private ArrayList<String> attendees, rejectedRequests, pendingRequests;
	
	/**
	* A parameterized constructor for Event.
	* @param eventID a unique id for the event
	* @param title the title of the event
	* @param description the description of the event
	* @param date the date the event is taking place on
	* @param startTime the time the event starts
	* @param endTime the time the event ends
	* @param eventAddress the address the event is taking place at
	* @param organizerEmail the email of the organizer organizing this event
	* @param registrationRequired true if the organizer has to manually approve event requests, false if the requests are automatically approved
	*/
	public Event (String eventID, String title, String description, LocalDate date, LocalTime startTime, LocalTime endTime, String eventAddress, String organizerEmail, boolean registrationRequired) {
		this.eventID = eventID;
		this.title = title;
		this.description = description;
		this.date = date;
		this.startTime = startTime;
		this.endTime = endTime;
		this.eventAddress = eventAddress;
		this.organizerEmail = organizerEmail;
		this.registrationRequired = registrationRequired;
	}
	
	/**
	* An empty public constructor for Event to enable read from and write to firebase database.
	*/
	public Event() {}
}