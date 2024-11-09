package ca.seg2105project.model.repositories;

import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.lang.reflect.Array;
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
public class EventsRepository {


	//Two types of events in firebase (upcoming and past) 
	private final ArrayList<Event> upcomingEvents; //NOTE: WE ARE ESTABLISHING THAT EVENT HAPPENING RIGHT NOW IS STILL AN UPCOMING EVENT (ATTENDEES CAN STILL REGISTER) 
    private final ArrayList<Event> pastEvents;
	private final ArrayList<Event> allEvents;

	//firebase database references
	private final DatabaseReference eventsDatabase;

    public EventsRepository() {
		// Initializing Firebase database references
		eventsDatabase = FirebaseDatabase.getInstance().getReference("events");

        //initialize the list of upcomingEvents and pastEvents, then update the lists from fb
		upcomingEvents = new ArrayList<>();
		pastEvents = new ArrayList<>();
		allEvents = new ArrayList<>();
		pullAllEvents();
		getAllPastEvents();
		getAllUpcomingEvents();
	}

	/**
	 * A method to update the final list of users "upcomingEvent" Does so 'in-place.' Takes its updated data from the firebase database.
	 */
	private void pullAllEvents() {
		allEvents.clear();
		eventsDatabase.addValueEventListener(new ValueEventListener() {

			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				allEvents.clear();
				for (DataSnapshot requestSnapshot : dataSnapshot.getChildren()) {
					//get each Event object in Firebase
					Event curEvent = requestSnapshot.getValue(Event.class);

					allEvents.add(curEvent);
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
    private ArrayList<Event> getAllUpcomingEvents() {
		upcomingEvents.clear();
		for (int i = 0; i<allEvents.size(); i++) {
			//get each Event object in Firebase
			Event curEvent = allEvents.get(i);

			//get the time of event and current time to tell if event is upcoming or not
			LocalDate eventDate = curEvent.getLocalDate();
			LocalDate curDate = LocalDate.now(); //NOTE TO SELF: DOUBLE CHECK SYNTAX
//					LocalTime eventST = curEvent.getLocalStartTime();
			LocalTime eventET = curEvent.getLocalEndTime();
			LocalTime curTime = LocalTime.now(); //NOTE TO SELF: DOUBLE CHECK SYNTAX

			//Checking between the two type of events:
			//LocalDate compareTo is this-other
			if (eventDate.compareTo(curDate) > 0) {
				//upcoming
				upcomingEvents.add(curEvent);
			} else if (eventDate.compareTo(curDate) == 0 && eventET.compareTo(curTime) > 0) { //they're equal
				//upcoming
				upcomingEvents.add(curEvent);
			}
		}
		return upcomingEvents;
	}


/**
 * This method returns a list of past events in Firebase by first updating the list if there are
 * new past events and then returning the most up to date list
 * @return a full list of all past events
 */
    private ArrayList<Event> getAllPastEvents() {
		pastEvents.clear();
		for (int i = 0; i<allEvents.size(); i++) {
			Event curEvent = allEvents.get(i);
			LocalDate eventDate = curEvent.getLocalDate();
			LocalDate curDate = LocalDate.now(); //NOTE TO SELF: DOUBLE CHECK SYNTAX
	//					LocalTime eventST = curEvent.getLocalStartTime();
			LocalTime eventET = curEvent.getLocalEndTime();
			LocalTime curTime = LocalTime.now(); //NOTE TO SELF: DOUBLE CHECK SYNTAX

			if(eventDate.compareTo(curDate)<0) {
				//past
				pastEvents.add(curEvent);

			} else if(eventDate.compareTo(curDate)==0 && eventET.compareTo(curTime) < 0) { //they're equal
				//past
				pastEvents.add(curEvent);
			}
		}
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

        // Add the event to the event section
        eventsDatabase.child(eventID).setValue(newEvent);
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
                                .addOnSuccessListener(v -> Log.d("Firebase", "Succesfully deleted event from eventID: " + eventID))
                                .addOnFailureListener(e -> Log.e("Firebase", "Error trying to delete event from eventID: " + eventID));
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

	


}