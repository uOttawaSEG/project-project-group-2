package ca.seg2105project.model.repositories;

import android.util.Log;

import androidx.annotation.NonNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

import com.google.firebase.database.Query;

import ca.seg2105project.model.eventClasses.Event;
import ca.seg2105project.model.registrationRequestClasses.RegistrationRequestStatus;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A class for accessing any information on any registered user.
 * In the next iteration of this class in this sprint, this class will hold a 'real'
 * list of registered users that will actually be modifiable.
 */
public class EventRepository {

	//Two types of events in firebase (upcoming and past) 
	private final ArrayList<Event> upcomingEvents; //NOTE: WE ARE ESTABLISHING THAT EVENT HAPPENING RIGHT NOW IS STILL AN UPCOMING EVENT (ATTENDEES CAN STILL REGISTER) 
	private final ArrayList<Event> pastEvents;
	private final ArrayList<Event> allEvents;

	//firebase database references
	private final DatabaseReference eventsDatabase;

	public EventRepository() {
		// Initializing Firebase database references
		eventsDatabase = FirebaseDatabase.getInstance().getReference("events");

		//initialize the list of upcomingEvents and pastEvents, then update the lists from fb
		upcomingEvents = new ArrayList<>();
		pastEvents = new ArrayList<>();
		allEvents = new ArrayList<>();

		//update the lists from fb db
		pullAllEvents();
	}

	/**
	 * Updates the lists allEvents, pastEvents, and upcomingEvents. Does so 'in-place.' Takes its updated data from the firebase database.
	 */
	private void pullAllEvents() {
		eventsDatabase.addValueEventListener(new ValueEventListener() {

			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				allEvents.clear();
				pastEvents.clear();
				upcomingEvents.clear();

				for (DataSnapshot requestSnapshot : dataSnapshot.getChildren()) {
					//get each Event object in Firebase
					Event curEvent = requestSnapshot.getValue(Event.class);
					//add each event into allEvents
					allEvents.add(curEvent);

					//now need to sort curEvent into either a pastEvent or an upcomingEvent
					LocalDate currentDate = LocalDate.now();
					LocalTime currentTime = LocalTime.now();
					LocalDate eventDate = curEvent.getLocalDate();
					LocalTime eventStartTime = curEvent.getLocalStartTime();
					LocalTime eventEndTime = curEvent.getLocalEndTime();


					if (eventDate.isBefore(currentDate)) {
						pastEvents.add(curEvent);
					} else if (eventDate.isAfter(currentDate)) {
						upcomingEvents.add(curEvent);
					} else { //must be today
						if (eventStartTime.isAfter(currentTime)) {
							upcomingEvents.add(curEvent);
						} else { //eventStartTime.isBefore(currentTime) || eventStartTime.isEqual(currentTime)
							if (eventEndTime.isAfter(currentTime)) {
								upcomingEvents.add(curEvent);
							} else { //eventEndTime.isBefore(currentTime) || eventEndTime.isEqual(currentTime)
								pastEvents.add(curEvent);
							}
						}
					}
				}
			}

			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {}
		});
	}

	/**
	 * This method returns a list of upcoming events in Firebase by returning upcomingEvents
	 * @return a full list of all upcoming events
	 */
	public ArrayList<Event> getAllUpcomingEvents() {
		return upcomingEvents;
	}

	/**
	 * This method returns a list of upcoming events whose organizerEmail matches the given one
	 * @return a full list of all upcoming events whose organizerEmail matches the given one
	 */
	public ArrayList<Event> getAllUpcomingEvents (String organizerEmail) {
		ArrayList<Event> ret = new ArrayList<Event>();
		for (Event e : upcomingEvents) {
			//go through every upcoming event, add to return arraylist if the organizerEmail matches
			if (e.getOrganizerEmail().equals(organizerEmail)) {
				ret.add(e);
			}
		}
		return ret;
	}

	/**
	 * This method returns a list of past events in Firebase by returning pastEvents
	 * @return a full list of all past events
	 */
	public ArrayList<Event> getAllPastEvents() {
		return pastEvents;
	}

	/**
	 * This method returns a list of past events whose organizerEmail matches the given one
	 * @return a full list of all past events whose organizerEmail matches the given one
	 */
	public ArrayList<Event> getAllPastEvents(String organizerEmail) {
		ArrayList<Event> ret = new ArrayList<Event>();
		for (Event e : pastEvents) {
			if (e.getOrganizerEmail().equals(organizerEmail)) {
				ret.add(e);
			}
		}
		return ret;
	}

	/**
	 * Adds new Event to event section in Firebase DB
	 * @param newEvent the new event--either an upcoming or past event--to be added to DB
	 * @return whether or not the new event was successfully added to the DB
	 */
	public boolean addEvent(Event newEvent) {
		// generating a unique key for the event
		String eventID = eventsDatabase.push().getKey();

		// We failed to create a reference to a new child in the event section of Firebase
		if (eventID == null) {
			return false;
		}

		//if event doesn't already have an eventID, set it to the fb key
		if (newEvent.getEventID() == null) {
			newEvent.setEventID(eventID);
		}

		// Add the event to the event section
		eventsDatabase.child(eventID).setValue(newEvent);

		return true;
	}

	/**
	 * A helper method to get the event object with the given eventID. Returns null if there is no event with the given eventID.
	 * @param eventID the unique eventID of the event to be returned
	 * @return the event object with the given eventID. Null if no such event exists.
	 */
	private Event getEventByEventID (String eventID) {
		for (Event e : allEvents) {
			if (e.getEventID().equals(eventID))
				return e;
		}
		//went through the entire allEvents list and found no event with the given eventID
		return null;
	}

	/**
	 * A method to determine if a given event can be deleted. A given event cannot be deleted if:
	 * <p>
	 *     - it has any approved requests
	 * </p>
	 * @param event the event that we need to determine if can be deleted or not. Must not be given a null event reference.
	 * @return true if the event can be deleted, false if it cannot be deleted
	 */
	private boolean canDeleteEvent (Event event) {
		if (event.getApprovedRequests() == null) return true; //if the approvedRequests read from fb is null, that means that there are no approved requests, so can delete
		return event.getApprovedRequests().isEmpty();
	}

	/**
	 * A method to determine if the event given by eventID can be deleted. See canDeleteEvent(Event) for deletion criteria
	 * @param eventID the id of the event to be deleted
	 * @return true if the event can be deleted and false if not.
	 */
	public boolean canDeleteEvent (String eventID) {
		Event event = getEventByEventID(eventID);
		if (event == null) return false;
		return canDeleteEvent(event);
	}

	/**
     * Removes the event associated with a specific id from firebase if that event can be removed.
	 * @param eventID the event id that identifies the event we want to remove. Must not be given a null String reference.
	 */
	public void deleteEvent(String eventID) {
		Event event = getEventByEventID(eventID);
		if (event == null) return; //the event with the given eventID was not found
		if (!canDeleteEvent(event)) return; //event cannot be deleted

		Query eventIDQuery = eventsDatabase.orderByChild("eventID").equalTo(eventID); //filter data based on eventID field in fb and then get the one with the matching eventID

		//listens for changes in eventIDQuery results
		eventIDQuery.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				if(dataSnapshot.exists()){
					for(DataSnapshot snapshot : dataSnapshot.getChildren()){ //should only be one event but looping just in case
						String key = snapshot.getKey();
						eventsDatabase.child(key).removeValue()
								.addOnSuccessListener(v -> Log.d("Firebase", "Successfully deleted event from eventID: " + eventID))
								.addOnFailureListener(e -> Log.e("Firebase", "Error trying to delete event from eventID: " + eventID));
					}
				}
				eventsDatabase.removeEventListener(this);
			}
			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {
				eventsDatabase.removeEventListener(this);
			}
		});
	}

	/**
	 * Returns a list of upcoming events that the attendeeEmail has not registered for and that also contain the keyword in either their title or description.
	 * @param keyword the keyword to search
	 * @param attendeeEmail the attendeeEmail that must not be registered for any events in the returned events list
	 * @return an arraylist of upcoming events that the attendeeEmail has not registered for and that also contain the keyword in either their title or description.
	 */
	public ArrayList<Event> getEventsByKeyword (String keyword, String attendeeEmail) {
		ArrayList<Event> ret = new ArrayList<Event>();
		for (Event e : upcomingEvents) { //for all upcoming events
			if (attendeeHasRegisteredForEvent(attendeeEmail, e))
				continue;
			//attendee has not registered for e

			boolean titleContainsKeyword = false;
			if (e.getTitle() != null && e.getTitle().contains(keyword))
				titleContainsKeyword = true;

			boolean descriptionContainsKeyword = false;
			if (e.getDescription() != null && e.getDescription().contains(keyword))
				titleContainsKeyword = true;

			if (titleContainsKeyword || descriptionContainsKeyword)
				ret.add(e);
		}
		//went through all events

		sortEventsByStartTime(ret);
		return ret;
	}

	/**
	 * Returns a list of upcoming events where attendeeEmail is in the list of pending, rejected, or approved requests. Returns a list that has been sorted from earliest start time to latest.
	 * @param attendeeEmail the attendee email whose list of upcoming events will be returned
	 * @return an arraylist of type event containing all the upcoming events (sorted by start time) that have attendeeEmail in either 3 of the lists. If given a null reference for attendeeEmail, returns an empty arraylist.
	 */
	public ArrayList<Event> getEventRegistrationRequests(String attendeeEmail) {
		ArrayList<Event> ret = new ArrayList<Event>();

		for (Event e : upcomingEvents) { //for all events, check if attendeeEmail is in one of its lists
			if (attendeeHasRegisteredForEvent(attendeeEmail, e))
				ret.add(e);
		}
		//went through all events

		sortEventsByStartTime(ret);
		return ret;
	}

	/**
	 * A private helper method to determine if attendeeEmail is in any of event's 3 requests lists
	 * @param attendeeEmail the attendeeEmail to check for
	 * @param event the event to check in
	 * @return true if attendeeEmail is in at least one request list of event, false otherwise. Also returns false if either of the given parameters are null references.
	 */
	private boolean attendeeHasRegisteredForEvent(String attendeeEmail, Event event) {
		if (event == null || attendeeEmail == null)
			return false;
		//event != null && attendeeEmail != null

		if (event.getApprovedRequests() != null && event.getApprovedRequests().containsValue(attendeeEmail)) //check in approved requests list
			return true;
		if (event.getPendingRequests() != null && event.getPendingRequests().containsValue(attendeeEmail)) //check in pending requests list
			return true;
		if (event.getRejectedRequests() != null && event.getRejectedRequests().containsValue(attendeeEmail)) //check in rejected requests list
			return true;

		return false; //if attendeeEmail was not in any of the lists
	}

	/**
	 * Returns the list of emails of the approved requests of the event specified by the given eventID.
	 * @param eventID the event's eventID whose approvedRequests are to be returned
	 * @return the list of emails of the approved requests of the event specified by the given eventID. Null if the specified eventID does not have an event associated to it.
	 */
	public ArrayList<String> getApprovedEventRequests (String eventID) {
		for (Event e : allEvents) {
			if (e.getEventID().equals(eventID)) {
				if (e.getApprovedRequests() == null) {
					return new ArrayList<>();
				} else {
					return new ArrayList<>(e.getApprovedRequests().values());
				}
			}
		}
		return null;
	}

	/**
	 * Returns the list of emails of the pending requests of the event specified by the given eventID.
	 * @param eventID the event's eventID whose pendingRequests are to be returned
	 * @return the list of emails of the pending requests of the event specified by the given eventID. Null if the specified eventID does not have an event associated to it or registration is not required for that event.
	 */
	public ArrayList<String> getPendingEventRequests (String eventID) {
		for (Event e : allEvents) {
			if (e.getEventID().equals(eventID)) {
				if (e.getPendingRequests() == null) {
					return new ArrayList<>();
				} else {
					return new ArrayList<>(e.getPendingRequests().values());
				}
			}
		}
		return null;
	}

	/**
	 * Returns the list of emails of the rejected requests of the event specified by the given eventID.
	 * @param eventID the event's eventID whose rejectedRequests are to be returned
	 * @return the list of emails of the rejected requests of the event specified by the given eventID. Null if the specified eventID does not have an event associated to it or registration is not required for that event.
	 */
	public ArrayList<String> getRejectedEventRequests(String eventID) {
		for (Event e : allEvents) {
			if (e.getEventID().equals(eventID)) {
				if (e.getRejectedRequests() == null) {
					return new ArrayList<>();
				} else {
					return new ArrayList<>(e.getRejectedRequests().values());
				}
			}
		}
		return null;
	}

	/**
	 * Changes the status of an existing event registration request
	 * @param eventID The eventID of the event that the registration request is associated with
	 * @param attendeeEmail the email of the attendee making the event registration request
	 * @param currentStatus the current status of the event registration request
	 * @param newStatus the new status of the event registration request
	 */
	public void changeEventRegistrationRequestStatus(String eventID, String attendeeEmail,
													 RegistrationRequestStatus currentStatus,
													 RegistrationRequestStatus newStatus) {
		// First remove the event registration request from the currentStatus list of event
		// registration requests
		removeEventRegistrationRequest(eventID, attendeeEmail, currentStatus);

		// Add the email to the list of event registration requests of new status
		String newStatusFbRegistrationRequestListKey = getFbRegistrationRequestListKey(newStatus);
		eventsDatabase.child(eventID).child(newStatusFbRegistrationRequestListKey).push().setValue(attendeeEmail);
	}

	/**
	 * Removes an existing event registration request
	 * @param eventID The eventID of the event that the registration request is associated with
	 * @param attendeeEmail the email of the attendee making the event registration request
	 * @param currentStatus the current status of the event registration request
	 */
	public void removeEventRegistrationRequest(String eventID, String attendeeEmail,
											   RegistrationRequestStatus currentStatus) {

		String currentStatusFbRegistrationRequestListKey = getFbRegistrationRequestListKey(currentStatus);

		Query registrationRequestQuery = eventsDatabase.child(eventID)
				.child(currentStatusFbRegistrationRequestListKey).orderByValue().equalTo(attendeeEmail);

		registrationRequestQuery.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot snapshot) {
				if (snapshot.exists()) {
					for (DataSnapshot registrationRequestSnapshot: snapshot.getChildren()) {
						String key = registrationRequestSnapshot.getKey();
						eventsDatabase.child(eventID).child(currentStatusFbRegistrationRequestListKey)
								.child(key).removeValue()
								.addOnSuccessListener(aVoid -> Log.d("Firebase", "Succesfullt removed registration request for: " + attendeeEmail))
								.addOnFailureListener(e -> Log.e("Firebase", "Failed to remove registration request", e));
					}
				}
				else{
					Log.e("Firebase", "No registration request found for: " + attendeeEmail);
				}
				registrationRequestQuery.removeEventListener(this);
			}

			@Override
			public void onCancelled(@NonNull DatabaseError error) {
				Log.e("Firebase", "Error querying registration requests");
				registrationRequestQuery.removeEventListener(this);
			}
		});
	}

	/**
	 * Utility method for getting the child key string for a list of event registration requests for a given status
	 * @param currentStatus The status of the list of event registration requests
	 * @return the key string for specifying fb child location for event registration requests of given status
	 */
	@NonNull
	private String getFbRegistrationRequestListKey(RegistrationRequestStatus currentStatus) {
		String fbRegistrationRequestListKey;
		if (currentStatus.equals(RegistrationRequestStatus.PENDING)) {
			fbRegistrationRequestListKey = "pendingRequests";
		} else if (currentStatus.equals(RegistrationRequestStatus.REJECTED)) {
			fbRegistrationRequestListKey = "rejectedRequests";
		} else {
			fbRegistrationRequestListKey = "approvedRequests";
		}
		return fbRegistrationRequestListKey;
	}

	/**
	 * Adds event registration request to fb with provided status
	 * @param eventID the event ID of the event that should have the new registration request
	 * @param attendeeEmail the email of the attendee requesting registration
	 * @param status the status of the new registration request
	 */
	public void addEventRegistrationRequest(String eventID, String attendeeEmail, RegistrationRequestStatus status) {
		String fbRegistrationRequestListKey = getFbRegistrationRequestListKey(status);
		eventsDatabase.child(eventID).child(fbRegistrationRequestListKey).push().setValue(attendeeEmail)
				.addOnSuccessListener(aVoid -> Log.d("Firebase", "Registration request added successfully"))
				.addOnFailureListener(e -> Log.e("Firebase", "Failed to add registration request", e));
	}

	public ArrayList<Event> mockGetListOfEventsWithERRsFromAttendee(String attendeeEmail) {
		ArrayList<Event> ret = new ArrayList<>();

		Event eventToAdd = new Event(
				"event1",
				"Event 1",
				"The First Event",
				LocalDate.of(2024, 12, 1),
				LocalTime.of(11, 0),
				LocalTime.of(12, 0),
				"Event 1 Address",
				"event1org@gmail.com",
				false);

		eventToAdd.addRequest(attendeeEmail, RegistrationRequestStatus.PENDING);

		ret.add(eventToAdd);

		eventToAdd = new Event(
				"event2",
				"Event 2",
				"The Second Event",
				LocalDate.of(2024, 12, 2),
				LocalTime.of(11, 0),
				LocalTime.of(12, 0),
				"Event Address",
				"eventorg@gmail.com",
				false
		);

		eventToAdd.addRequest(attendeeEmail, RegistrationRequestStatus.REJECTED);

		ret.add(eventToAdd);

		eventToAdd = new Event(
				"event3",
				"Event 3",
				"The Third Event",
				LocalDate.of(2024, 12, 3),
				LocalTime.of(11, 0),
				LocalTime.of(12, 0),
				"Event Address",
				"eventorg@gmail.com",
				false
		);

		eventToAdd.addRequest(attendeeEmail, RegistrationRequestStatus.APPROVED);

		ret.add(eventToAdd);

		eventToAdd = new Event(
				"event4",
				"Event 4",
				"The Second Event",
				LocalDate.of(2024, 12, 4),
				LocalTime.of(11, 0),
				LocalTime.of(12, 0),
				"Event Address",
				"eventorg@gmail.com",
				false
		);

		eventToAdd.addRequest(attendeeEmail, RegistrationRequestStatus.PENDING);

		ret.add(eventToAdd);

		return ret;
	}

	/**
	 * A private helper method to sort an arraylist of type event in-place from earliest to latest start time.
	 * This will make it so that the given events is transformed such that the events happening earlier have smaller indices than ones happening later.
	 * Uses a selective sort algorithm, so runs in O(n^2) time if n = events.size()
	 * @param events the list of events to be sorted in-place from earliest start time to latest
	 */
	private void sortEventsByStartTime (ArrayList<Event> events) {
		int n = events.size();
		for (int i = 0; i < n; i++) {
			int earliestEventIndex = i;
			for (int j = i + 1; j < n; j++) {
				Event a = events.get(earliestEventIndex); //a = earliestEvent so far, at index earliestEventIndex
				Event b = events.get(j); //b = the event being considered right now, at index j

				LocalDate aDate = a.getLocalDate();
				LocalTime aStartTime = a.getLocalStartTime();
				LocalDateTime aDateTime = LocalDateTime.of(aDate, aStartTime); //the LocalDateTime object corresponding to the event a's start time
				LocalDate bDate = b.getLocalDate();
				LocalTime bStartTime = b.getLocalStartTime();
				LocalDateTime bDateTime = LocalDateTime.of(bDate, bStartTime); //the LocalDateTime object corresponding to the event b's start time

				if (bDateTime.isBefore(aDateTime)) {
					//if b started before a did
					earliestEventIndex = j;
				}
			}
			//earliestEventIndex now has the index of the earliest event found from index i onwards, so now need to switch the event at index i with the event at index earliestEventIndex
			Event temp = events.get(i);
			events.set(i, events.get(earliestEventIndex));
			events.set(earliestEventIndex, temp);
		}
	}

	/**
	 * Method to determine if attendee is able to cancel their event registration request
	 * They are only allowed to cancel when event has not yet happened yet
	 * and it is 24 hours away from the current time
	 * @param e The event that the attendee is trying to cancel their registration for
	 * @return whether the attendee is able to cancel their event registration request or not
	 */
	public boolean canCancelEventRegistrationRequest (Event e) {
		//to cancel: request must be more than 24 hours away from now
		LocalDate currentDate = LocalDate.now();
		LocalTime currentTime = LocalTime.now();
		LocalDateTime curDateTime = LocalDateTime.of(currentDate, currentTime);
		LocalDate eventDate = e.getLocalDate();
		LocalTime eventStartTime = e.getLocalStartTime();
		LocalDateTime eventDateTime = LocalDateTime.of(eventDate, eventStartTime);

		//Note: I split it up into multiple if statements to make it easier to understand even though I could combine it
		if(curDateTime.isAfter(eventDateTime)) { //event already happened/is happening
			return false;
		}

		//can only cancel request when not within 24 hours
		return !within24Hours(curDateTime, eventDateTime);
	}

	/**
	 * A private helper method to determine if there the event time is within 24 hours of the current time
	 * To check if an event is within 24 hours of the current time, I need to check that the year and month are the same
	 * Then I need to check that the days are the same (guaranteed within 24 hours) or differ by 1 (then need to check hours)
	 * I check that the current hour is greater than the event hour (guarantees within 24 hours when current day is one day earlier than event day)
	 * If hour is the same, I need to check that the current time minutes is greater than the event time minutes (this makes it almost 24 hours with only minutes of difference)
	 * @param curDateTime the current time in LocalDateTime
	 * @param eventDateTime the time of the event in LocalDateTime
	 * @return whether the event time is within 24 hours of the current time
	 */
	private boolean within24Hours(LocalDateTime curDateTime, LocalDateTime eventDateTime) {

		//check that it is within 24 hrs
		if (curDateTime.getYear()==eventDateTime.getYear() && curDateTime.getMonth()==eventDateTime.getMonth()) { //same year and month
			if (curDateTime.getDayOfMonth()==eventDateTime.getDayOfMonth()) { //same day so less than 24 hrs
				return true;
			} else if (curDateTime.getDayOfMonth()+1==eventDateTime.getDayOfMonth()) {
				if(curDateTime.getHour()>eventDateTime.getHour()) { //hours make it within than 24hrs
					return true;
				} else if (curDateTime.getHour()==eventDateTime.getHour() && curDateTime.getMinute()>eventDateTime.getMinute()) { //minutes make it within 24 hrs
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Method to cancel the attendee's event registration request
	 * It checks if canCancel using the canCancelEventRegistrationRequest method
	 * and then removes the request from pending or approved request list
	 * @param attendeeEmail the email of the attendee trying to cancel registration for an event
	 * @param e the event the attendee is trying to cancel register for
	 */
	public void cancelEventRegistrationRequest(String attendeeEmail, Event e) {
		if (e == null) {
			Log.e("EventRepository", "Event is null, cannot cancel registration.");
			return;
		}
		if (attendeeEmail == null || attendeeEmail.isEmpty()) {
			Log.e("EventRepository", "Attendee email is null or empty, cannot cancel registration.");
			return;
		}
		if(e.getApprovedRequests() != null && e.getApprovedRequests().containsValue(attendeeEmail)) {
			Log.d("EventRepository", "Removing approved request for attendee");
			removeEventRegistrationRequest(e.getEventID(), attendeeEmail, RegistrationRequestStatus.APPROVED);
		}
		else if(e.getPendingRequests() != null && e.getPendingRequests().containsValue(attendeeEmail)) {
			Log.d("EventRepository", "Removing pending request for attendee");
			removeEventRegistrationRequest(e.getEventID(), attendeeEmail, RegistrationRequestStatus.PENDING);
		}
		else{
			Log.d("EventRepository", "No matching request found");
		}
	}

	/**
	 * Method to determine if attendee is able to register to an event
	 * They are only allowed to register for an event that is currently happening or will happen
	 * and when there are no time conflicts with other event's they are attending or possibly attending
	 * @param attendeeEmail the email of the attendee trying to register for an event
	 * @param e the event the attendee is trying to register for
	 * @return whether the attendee is able to register for the event they chose
	 */
	public boolean canRegisterForEvent (String attendeeEmail, Event e) {
		//no time conflicts, haven't already registered, not a past event

		LocalDate currentDate = LocalDate.now();
		LocalTime currentTime = LocalTime.now();
		LocalDateTime curDateTime = LocalDateTime.of(currentDate, currentTime);
		LocalDate eventDate = e.getLocalDate();
		LocalTime eventStartTime = e.getLocalStartTime();
		LocalTime eventEndTime = e.getLocalEndTime();
		LocalDateTime eventStartDateTime = LocalDateTime.of(eventDate, eventStartTime);
		LocalDateTime eventEndDateTime = LocalDateTime.of(eventDate, eventEndTime);

		if(curDateTime.isAfter(eventEndDateTime)) { //event already passed, so cannot register
			return false;
		}

		for(int i = 0; i<allEvents.size(); i++) {
			Event curEvent = allEvents.get(i);

			//attendee has been approved for event, check no time conflict
			if(curEvent.getApprovedRequests()!=null && curEvent.getApprovedRequests().get(attendeeEmail)!=null) {
				if(hasConflict(e, curEvent)) {
					return false;
				}
			} else if(curEvent.getPendingRequests()!=null && curEvent.getPendingRequests().get(attendeeEmail)!=null) {
				if (hasConflict(e, curEvent)) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * A private helper method to determine if there is a time conflict between two events 
	 * There are four possibilties for a conflict, when event a is within event b or event b is within event a 
	 * or when event a starts in between event b or ends in between event b 
	 * @param e the first event we will use to check for time conflict (event a) 
	 * @param curEvent the second event we will use to check time conflict (event b) 
	 * @return whether there is a time conflict between the two events
	 */
	private boolean hasConflict(Event e, Event curEvent) {
		LocalDate eventDate = e.getLocalDate();
		LocalTime eventStartTime = e.getLocalStartTime();
		LocalTime eventEndTime = e.getLocalEndTime();
		LocalDateTime eventStartDateTime = LocalDateTime.of(eventDate, eventStartTime);
		LocalDateTime eventEndDateTime = LocalDateTime.of(eventDate, eventEndTime);

		LocalDate curEventDate = curEvent.getLocalDate();
		LocalTime curEventStartTime = curEvent.getLocalStartTime();
		LocalTime curEventEndTime = curEvent.getLocalEndTime();
		LocalDateTime curStartDateTime = LocalDateTime.of(curEventDate, curEventStartTime);
		LocalDateTime curEndDateTime = LocalDateTime.of(curEventDate, curEventEndTime);

		/*
		4 possibilities for a conflict:
		Note: curEvent refers to the current event we are checking in the events list that attendee already registered for
		1. event start time is in between curEventTime while it ends after curEventTime
		2. event end time is between the curEventTime and it starts before curEventTime
		3. event starts and ends within curEventTime
		4. curEventTime starts and ends within eventTime
		 */

		//catch first and third possibility:
		if(eventStartDateTime.isAfter(curStartDateTime) && eventStartDateTime.isBefore(curEndDateTime)) {
			return true;
		} else if (eventEndDateTime.isAfter(curStartDateTime) && eventEndDateTime.isBefore(curEndDateTime)) { //checks for possibility #2
			return true;
		} else if (curStartDateTime.isAfter(eventStartDateTime) && curEndDateTime.isBefore(eventEndDateTime)) { //check 4th possibility
			return true;
		}
		return false; //no conflict since didn't meet the 4 possibilities
	}

	/**
	 * Method to register attendee to an event
	 * It checks if canRegister using the canRegisterForEvents method
	 * and then adds the request from pending or approved request list
	 * @param attendeeEmail the email of the attendee trying to create a registration for an event
	 * @param e the event the attendee is trying to register for
	 */
	public void registerForEvent(String attendeeEmail, Event e) {
		//put into pendingRequests if registrationRequired, put into approvedRequests if !registrationRequired

		//first check if can register for an event
		if(canRegisterForEvent(attendeeEmail, e)) {
			if (e.getRegistrationRequestsAreAutoApproved()) { //check if registration is autoapproved
				addEventRegistrationRequest(e.getEventID(), attendeeEmail, RegistrationRequestStatus.APPROVED);
			} else { //place in pending request and await approval from organizer
				addEventRegistrationRequest(e.getEventID(), attendeeEmail, RegistrationRequestStatus.PENDING);
			}
		}
	}

	//END of EventRepository class
}