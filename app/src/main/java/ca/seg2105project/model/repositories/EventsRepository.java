package ca.seg2105project.model.repositories;

import androidx.annotation.NonNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

import javax.management.Query;

import org.w3c.dom.events.Event;

// import ca.seg2105project.model.userClasses.Administrator;
// import ca.seg2105project.model.userClasses.Attendee;
// import ca.seg2105project.model.userClasses.User;
// import ca.seg2105project.model.userClasses.Organizer;

// import com.google.firebase.database.DataSnapshot;
// import com.google.firebase.database.DatabaseError;
// import com.google.firebase.database.DatabaseReference;
// import com.google.firebase.database.FirebaseDatabase;
// import com.google.firebase.database.ValueEventListener;

/**
 * A class for accessing any information on any registered user.
 * In the next iteration of this class in this sprint, this class will hold a 'real'
 * list of registered users that will actually be modifiable.
 */
public class EventsRepository {

	//Two types of events in firebase (upcoming and past) 
	private final ArrayList<Event> upcomingEvents; //NOTE: WE ARE ESTABLISHING THAT EVENT HAPPENING RIGHT NOW IS STILL AN UPCOMING EVENT (ATTENDEES CAN STILL REGISTER) 
    private final ArrayList<Event> pastEvents;

	//firebase database references
	private final DatabaseReference eventsDatabase;

    public EventsRepository() {
		// Initializing Firebase database references
		eventsDatabase = FirebaseDatabase.getInstance().getReference("events");

        //initialize the list of upcomingEvents and pastEvents, then update the lists from fb
		upcomingEvents = new ArrayList<>();
		pastEvents = new ArrayList<>();
		pullUpcoming();
		pullPast();
	}

	/**
	 * A method to update the final list of users "upcomingEvent" Does so 'in-place.' Takes its updated data from the firebase database. 
	 */
	private void pullUpcoming() {
		usersDatabase.addValueEventListener(new ValueEventListener() {

			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				upcomingEvents.clear();
				for (DataSnapshot requestSnapshot : dataSnapshot.getChildren()) { 
					//get each Event object in Firebase 
					Event event = requestSnapshot.getValue(Events.class);  

					//get the time of event and current time to tell if event is upcoming or not 
					LocalDate eventDate = curEvent.getLocalTime(); 
					LocalDate curDate = LocalDate.now(); //NOTE TO SELF: DOUBLE CHECK SYNTAX 
					LocalTime eventST = curEvent.getLocalStartTime(); 
					LocalTime eventET = curEvent.getLocalEndTime(); 
					LocalTime curTime = LocalTime.now(); //NOTE TO SELF: DOUBLE CHECK SYNTAX 

					//Checking between the two type of events: 
					//LocalDate compareTo is this-other 
					if(eventDate.compareTo(curDate)>0) {  
						//upcoming 
						upcomingEvents.add(event); 

						
					} else if(eventDate.compareTo(curDate)==0 && eventST.compareTo(curTime) > 0) { //they're equal 
						//upcoming 
						upcomingEvents.add(event); 
					}
				}
			}

			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {}
		});
	}


	/**
	 * A method to update the final list of users "pastEvents." Does so 'in-place.' Takes its updated data from the firebase database. 
	 */
	private void pullPast() {
		usersDatabase.addValueEventListener(new ValueEventListener() {

			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				pastEvents.clear();
				for (DataSnapshot requestSnapshot : dataSnapshot.getChildren()) {
					Event event = requestSnapshot.getValue(Events.class); 
					LocalDate eventDate = curEvent.getLocalTime(); 
					LocalDate curDate = LocalDate.now(); //NOTE TO SELF: DOUBLE CHECK SYNTAX 
					LocalTime eventST = curEvent.getLocalStartTime(); 
					LocalTime eventET = curEvent.getLocalEndTime(); 
					LocalTime curTime = LocalTime.now(); //NOTE TO SELF: DOUBLE CHECK SYNTAX 

					if(eventDate.compareTo(curDate)<0) {  
						//past 
						pastEvents.add(event); 
						
					} else if(eventDate.compareTo(curDate)==0 && eventET.compareTo(curTime) < 0) { //they're equal 
						//past 
						pastEvents.add(event);  
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
		pullUpcoming();
		return upcomingEvents;
    }

	/**
     * This method returns a list of past events in Firebase by first updating the list if there are 
	 * new past events and then returning the most up to date list 
     * @return a full list of all past events
     */
    public ArrayList<Event> getAllPastEvents() {
		pullPast();
		return pastEvents;
    } 

	/**
     * Adds new Event to event section in Firebase DB
     * @param newEvent the new event--either an upcoming or past event--to be added to DB
     * @return whether or not the new event was successfully added to the DB
     */
    public boolean addEvent(Event newEvent) {
        // generating a unique key for the event
        String eventID = mDatabase.push().getKey();

        // We failed to create a reference to a new child in the event section of Firebase
        if (eventID == null) {
            return false;
        }

        // Add the event to the event section
        mDatabase.child(eventID).setValue(newEvent);
        return true;
    }


	/**
     * Removes the event associated with a specific id from firebase 
	 * @param eventID the event id that identifies the event we want to remove 
     */
    public void deleteEvent(String eventID){
        //TODO: implement this 
    }

	


}