package ca.seg2105project.model.eventClasses;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

/**
* A class to model an Event. If registrationRequired is false, then pendingRequests and rejectedRequests are set to null.
*/
public class Event {
	private String eventID, title, description, eventAddress, organizerEmail;
	private LocalDate date;
	private LocalTime startTime, endTime;
	private boolean registrationRequired;
	private ArrayList<String> attendees, pendingRequests, rejectedRequests;
	
	/**
	* A parameterized constructor for Event that takes in LocalDate and LocalTime objects.
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
		
		this.attendees = new ArrayList<String>();
		
		if (registrationRequired == true) { //if requests need to be manually approved by organizer, then keep track of pending and approved requests
			this.pendingRequests = new ArrayList<String>();
			this.rejectedRequests = new ArrayList<String>();
		} else { //if requests are automatically approved, there won't ever be any pending or rejected requests, so we won't keep track of them
			this.pendingRequests = null;
			this.rejectedRequests = null;
		}
	}
	
	/**
	* A parameterized constructor for Event that takes in ints for the date and times instead of LocalDate/LocalTime objects.
	* @param eventID a unique id for the event
	* @param title the title of the event
	* @param description the description of the event
	* @param year the year the event is taking place in
	* @param month the month the event is taking place in, {1,2,3,4,5,6,7,8,9,10,11,12}
	* @param date the date the event is taking place in, {1 - 31}
	* @param startTimeHour the hour the event starts
	* @param startTimeMinute the minute the event starts
	* @param endTimeHour the hour the event ends
	* @param endTimeMinute the minute the event ends
	* @param eventAddress the address the event is taking place at
	* @param organizerEmail the email of the organizer organizing this event
	* @param registrationRequired true if the organizer has to manually approve event requests, false if the requests are automatically approved
	*/
	public Event (String eventID, String title, String description, int year, int month, int date, int startTimeHour, int startTimeMinute, int endTimeHour, int endTimeMinute, String eventAddress, String organizerEmail, boolean registrationRequired) {
		this(eventID, title, description, 
			LocalDate.of(year, month, date), 
			LocalTime.of(startTimeHour, startTimeMinute), 
			LocalTime.of(endTimeHour, endTimeMinute), 
			eventAddress, organizerEmail, registrationRequired);
	}
	
	/**
	* An empty public constructor for Event to enable read from and write to firebase database.
	*/
	public Event() {}
	
	//getter methods
	
	/**
	* A getter for eventID.
	* @return the unique id for this event
	*/
	public String getEventID() { return eventID; }
	
	/**
	* A getter for title.
	* @return the title of the event
	*/
	public String getTitle() { return title; }
	
	/**
	* A getter for description.
	* @return the description of the event
	*/
	public String getDescription() { return description; }
	
	/**
	* A getter for date as a LocalDate object.
	* @return the date the event is happening on as a LocalDate object
	*/
	public LocalDate getLocalDate() { return date; }
	
	/**
	* A getter for the year the event is taking place in as an int.
	* @return the year the event is taking place in
	*/
	public int getYear() { return date.getYear(); } 
	
	/**
	* A getter for the month the event is taking place in as an int.
	* @return the month the event is taking place in as an int. 1 for January, 2 for February, ..., 12 for December.
	*/
	public int getMonth() { return date.getMonthValue(); } 
	
	/**
	* A getter for the date the event is taking place on as an int.
	* @return the date the event is taking place on
	*/
	public int getDate() { return date.getDayOfMonth(); }
	
	/**
	* A getter for startTime as a LocalTime object.
	* @return the start time of the event as a LocalTime object
	*/
	public LocalTime getLocalStartTime() { return startTime; }
	
	/**
	* A getter for the hour the event is starting at as an int.
	* @return the hour the event is starting at as an int
	*/
	public int getStartTimeHour() { return startTime.getHour(); }
	
	/**
	* A getter for the minute the event is starting at as an int.
	* @return the minute the event is starting at as an int
	*/
	public int getStartTimeMinute() { return startTime.getMinute(); }
	
	/**
	* A getter for endTime as a LocalTime object.
	* @return the end time of the event as a LocalTime object
	*/
	public LocalTime getLocalEndTime() { return endTime; }
	
	/**
	* A getter for the hour the event is ending at as an int.
	* @return the hour the event is ending at as an int
	*/
	public int getEndTimeHour() { return endTime.getHour(); }
	
	/**
	* A getter for the minute the event is ending at as an int.
	* @return the minute the event is ending at as an int
	*/
	public int getEndTimeMinute() { return endTime.getMinute(); }
	
	/**
	* A getter for eventAddress.
	* @return the address the event is happening at
	*/
	public String getEventAddress() { return eventAddress; }
	
	/**
	* A getter for organizerEmail.
	* @return the email of the organizer organizing this event
	*/
	public String getOrganizerEmail() { return organizerEmail; }
	
	/**
	* A getter for registrationRequired.
	* @return true if the organizer must manually approve requests for this event, false if all requests are automatically approved
	*/
	public boolean getRegistrationRequired() { return registrationRequired; }
	
	/**
	* A getter for the array list attendees.
	* @return the list of the attendees' emails that will be attending this event (synonymous to approvedRequests)
	*/
	public ArrayList<String> getAttendees() { return attendees; }
	
	/**
	* A getter for the array list pendingRequests.
	* @return the list of the attendees' emails that have requested to join this event, null if registration is not required
	*/
	public ArrayList<String> getPendingRequests() { return pendingRequests; }
	
	/**
	* A getter for the array list rejectedRequests.
	* @return the list of the attendees' emails whose requests have been rejected, null if registration is not required
	*/
	public ArrayList<String> getRejectedRequests() { return rejectedRequests; }
}