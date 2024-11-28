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
}