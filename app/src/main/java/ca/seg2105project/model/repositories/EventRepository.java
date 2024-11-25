package ca.seg2105project.model.repositories;

import android.util.Log;

import androidx.annotation.NonNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;

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
	public boolean canDeleteEvent (Event event) {
		if (event.getApprovedRequests() == null) return true; //if the approvedRequests read from fb is null, that means that there are no approved requests, so can delete
		return event.getApprovedRequests().isEmpty();
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
		String currentStatusFbRegistrationRequestListKey = getFbRegistrationRequestListKey(currentStatus);

		Query registrationRequestQuery = eventsDatabase.child(eventID)
				.child(currentStatusFbRegistrationRequestListKey).orderByValue().equalTo(attendeeEmail);

		registrationRequestQuery.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot snapshot) {
				if (snapshot.exists()) {
					for (DataSnapshot registrationRequestSnapshot: snapshot.getChildren()) {
						String key = registrationRequestSnapshot.getKey();
						eventsDatabase.child(eventID).child(currentStatusFbRegistrationRequestListKey)
								.child(key).removeValue();
					}
				}
				registrationRequestQuery.removeEventListener(this);
			}

			@Override
			public void onCancelled(@NonNull DatabaseError error) {
				registrationRequestQuery.removeEventListener(this);
			}
		});

		// Add the email to the list of event registration requests of new status
		String newStatusFbRegistrationRequestListKey = getFbRegistrationRequestListKey(newStatus);
		eventsDatabase.child(eventID).child(newStatusFbRegistrationRequestListKey).push().setValue(attendeeEmail);
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
		eventsDatabase.child(eventID).child(fbRegistrationRequestListKey).push().setValue(attendeeEmail);
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
//		LocalTime eventEndTime = e.getLocalEndTime();
		LocalDateTime eventDateTime = LocalDateTime.of(eventDate, eventStartTime);
//		LocalDateTime eventEndDateTime = LocalDateTime.of(eventDate, eventStartTime);

		//Note: I split it up into multiple if statements to make it easier to understand even though I could combine it
		if(curDateTime.isAfter(eventDateTime)) { //event already happened/is happening
			return false;
		}

		//check that it is within 24 hrs
		if (curDateTime.getYear()==eventDateTime.getYear() && curDateTime.getMonth()==eventDateTime.getMonth()) { //same year and month
			if (curDateTime.getDayOfMonth()==eventDateTime.getDayOfMonth()) { //same day so less than 24 hrs
				return false;
			} else if (curDateTime.getDayOfMonth()+1==eventDateTime.getDayOfMonth()) {
				if(curDateTime.getHour()>eventDateTime.getHour()) { //hours make it within than 24hrs
					return false;
				} else if (curDateTime.getHour()==eventDateTime.getHour() && curDateTime.getMinute()>eventDateTime.getMinute()) { //minutes make it within 24 hrs
 					return false;
				}
			}
		}
		return true;


	}


	public void cancelEventRegistrationRequest (String attendeeEmail, Event e) {
		//need to discuss this method with Issac
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

	public void registerForEvent(String attendeeEmail, Event e) {
		//put into pendingRequests if registrationRequired, put into approvedRequests if !registrationRequired
	}


}