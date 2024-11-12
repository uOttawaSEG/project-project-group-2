package ca.seg2105project.model.repositories;

import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import com.google.firebase.database.Query;


import ca.seg2105project.model.eventClasses.Event;


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
@RequiresApi(api = Build.VERSION_CODES.O) //since LocalDate and LocalTime requires a newer API
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
		allEvents.clear();
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
	 * This method returns a list of upcoming events in Firebase by first updating the list if there are
	 * new upcoming events added and then returning the most up to date list
	 * @return a full list of all upcoming events
	 */
    public ArrayList<Event> getAllUpcomingEvents() {
		pullAllEvents(); //to update the lists
		return upcomingEvents;
	}


/**
 * This method returns a list of past events in Firebase by first updating the list if there are
 * new past events and then returning the most up to date list
 * @return a full list of all past events
 */
    public ArrayList<Event> getAllPastEvents() {
		pullAllEvents(); //to update the lists
		return pastEvents;
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

		pullAllEvents(); //update the local lists to fetch the new event


        return true;
    }


	/**
     * Removes the event associated with a specific id from firebase 
	 * @param eventID the event id that identifies the event we want to remove 
     */
    public void deleteEvent(String eventID){ 
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
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
		pullAllEvents(); //update the list after adding a new event
    }

	/**
	 * Returns the list of emails of the approved requests of the event specified by the given eventID.
	 * @param eventID the event's eventID whose approvedRequests are to be returned
	 * @return the list of emails of the approved requests of the event specified by the given eventID. Null if the specified eventID does not have an event associated to it.
	 */
	public ArrayList<String> getApprovedEventRequests (String eventID) {
		pullAllEvents();
		for (Event e : allEvents) {
			if (e.getEventID().equals(eventID)) {
				return e.getApprovedRequests();
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
		pullAllEvents();
		for (Event e : allEvents) {
			if (e.getEventID().equals(eventID)) {
				return e.getPendingRequests();
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
		pullAllEvents();
		for (Event e : allEvents) {
			if (e.getEventID().equals(eventID)) {
				return e.getRejectedRequests();
			}
		}
		return null;
	}
}