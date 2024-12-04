package ca.seg2105project.model.eventClasses;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;

import ca.seg2105project.model.registrationRequestClasses.RegistrationRequestStatus;

/**
 * A class to model an Event. If registrationRequired is false, then pendingRequests and rejectedRequests are set to null.
 */
public class Event {
	private String eventID, title, description, eventAddress, organizerEmail;
	private long dateMillis; // Store date as milliseconds since epoch for Firebase
	private long startTimeMillis, endTimeMillis; // Store times as milliseconds since midnight for Firebase
	private boolean registrationRequestsAreAutoApproved;
	private HashMap<String, String> approvedRequests, pendingRequests, rejectedRequests;

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
	 * @param registrationRequestsAreAutoApproved true if the organizer has to manually approve event requests, false if the requests are automatically approved
	 */
	public Event (String eventID, String title, String description, LocalDate date,
				  LocalTime startTime, LocalTime endTime, String eventAddress, String organizerEmail,
				  boolean registrationRequestsAreAutoApproved) {
		this.eventID = eventID;
		this.title = title;
		this.description = description;
		this.dateMillis = date.toEpochDay() * 24 * 60 * 60 * 1000; // Convert to milliseconds
		this.startTimeMillis = startTime.toNanoOfDay() / 1000000; // Convert to milliseconds
		this.endTimeMillis = endTime.toNanoOfDay() / 1000000; // Convert to milliseconds
		this.eventAddress = eventAddress;
		this.organizerEmail = organizerEmail;
		this.registrationRequestsAreAutoApproved = registrationRequestsAreAutoApproved;

		this.approvedRequests = new HashMap<>();

		if (!registrationRequestsAreAutoApproved) { //if requests need to be manually approved by organizer, then keep track of pending and approved requests
			this.pendingRequests = new HashMap<>();
			this.rejectedRequests = new HashMap<>();
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
	 * @param registrationRequestsAreAutoApproved true if the organizer has to manually approve event requests, false if the requests are automatically approved
	 */
	public Event (String eventID, String title, String description, int year, int month, int date, int startTimeHour, int startTimeMinute, int endTimeHour, int endTimeMinute, String eventAddress, String organizerEmail, boolean registrationRequestsAreAutoApproved) {
		this(eventID, title, description,
				LocalDate.of(year, month, date),
				LocalTime.of(startTimeHour, startTimeMinute),
				LocalTime.of(endTimeHour, endTimeMinute),
				eventAddress, organizerEmail, registrationRequestsAreAutoApproved);
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
	public LocalDate getLocalDate() {
		return LocalDate.ofEpochDay(dateMillis / (24 * 60 * 60 * 1000));
	}

	/**
	 * A getter for startTime as a LocalTime object.
	 * @return the start time of the event as a LocalTime object
	 */
	public LocalTime getLocalStartTime() {
		return LocalTime.ofNanoOfDay(startTimeMillis * 1000000);
	}

	/**
	 * A getter for endTime as a LocalTime object.
	 * @return the end time of the event as a LocalTime object
	 */
	public LocalTime getLocalEndTime() {
		return LocalTime.ofNanoOfDay(endTimeMillis * 1000000);
	}

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
	 * A getter for dateMillis
	 * @return the time in millis since epoch for the date of the event
	 */
	public long getDateMillis() { return dateMillis; }

	/**
	 * A getter for startTimeMillis
	 * @return the time in millis since midnight of the start time of the event
	 */
	public long getStartTimeMillis() { return startTimeMillis; }

	/**
	 * A getter for endTimeMillis
	 * @return the time in millis since midnight of the end time of the event
	 */
	public long getEndTimeMillis() { return endTimeMillis; }

	/**
	 * A getter for registrationRequired.
	 * @return true if the organizer must manually approve requests for this event, false if all requests are automatically approved
	 */
	public boolean getRegistrationRequestsAreAutoApproved() { return registrationRequestsAreAutoApproved; }

	/**
	 * A getter for the array list approvedRequests.
	 * @return the map of the attendees' emails (key is firebase generated hash) that will be attending this event (synonymous to approvedRequests)
	 */
	public HashMap<String, String> getApprovedRequests() { return approvedRequests; }

	/**
	 * A getter for the array list pendingRequests.
	 * @return the map of the attendees' emails (key is firebase generated hash) that have requested to join this event, null if registration is not required
	 */
	public HashMap<String, String> getPendingRequests() { return pendingRequests; }

	/**
	 * A getter for the array list rejectedRequests.
	 * @return the map of the attendees' emails (key is firebase generated hash) whose requests have been rejected, null if registration is not required
	 */
	public HashMap<String, String> getRejectedRequests() { return rejectedRequests; }

	//setter methods

	public void setEventID (String eventId) {
		this.eventID = eventId;
	}

	/**
	 * Right now this method is only used to be able to test the RecyclerView adapters for the attendee
	 * event related screens
	 * @param email the email of the attendee who is making the request
	 * @param status the status the request should have
	 */
	public void addRequest(String email, RegistrationRequestStatus status) {
		if (status == RegistrationRequestStatus.PENDING) {
			pendingRequests.put("randomhash", email);
		} else if (status == RegistrationRequestStatus.REJECTED) {
			rejectedRequests.put("randomhash", email);
		} else if (status == RegistrationRequestStatus.APPROVED) {
			approvedRequests.put("randomhash", email);
		} else {
			throw new NullPointerException();
		}
	}

	public void setApprovedRequests(HashMap<String, String> approvedRequests) {
		this.approvedRequests = approvedRequests;
	}

	public void setPendingRequests(HashMap<String, String> pendingRequests) {
		this.pendingRequests = pendingRequests;
	}
}