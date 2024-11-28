package ca.seg2105project.model.Testers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;

import ca.seg2105project.model.eventClasses.Event;
import ca.seg2105project.model.registrationRequestClasses.RegistrationRequestStatus;
import ca.seg2105project.model.userClasses.Attendee;


/**
 * A class to test the Event Registration Request CRUD made in PR#82
 */
public class EventRegistrationRequestCRUDTester {
    public static Event[] events = new Event[8];

    public static void addEventRegistrationRequest(int index, String attendeeEmail, RegistrationRequestStatus status) {
        if(status.equals(RegistrationRequestStatus.APPROVED)) {
            events[index].getApprovedRequests().put(attendeeEmail, attendeeEmail);
        } else if(status.equals(RegistrationRequestStatus.PENDING)) {
            events[index].getPendingRequests().put(attendeeEmail, attendeeEmail);
        }
    }

    /**
     * Method to determine if attendee is able to cancel their event registration request
     * They are only allowed to cancel when event has not yet happened yet
     * and it is 24 hours away from the current time
     * @param e The event that the attendee is trying to cancel their registration for
     * @return whether the attendee is able to cancel their event registration request or not
     */
    public static boolean canCancelEventRegistrationRequest (Event e) {
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
    private static boolean within24Hours(LocalDateTime curDateTime, LocalDateTime eventDateTime) {

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


    public static void cancelEventRegistrationRequest (String attendeeEmail, int index) {

        if(canCancelEventRegistrationRequest(events[index])) {
            if (events[index].getApprovedRequests().get(attendeeEmail) != null) {
                removeEventRegistrationRequest(index, attendeeEmail, RegistrationRequestStatus.APPROVED);
            } else if (events[index].getPendingRequests().get(attendeeEmail) != null) {
                removeEventRegistrationRequest(index, attendeeEmail, RegistrationRequestStatus.PENDING);
            }
        }
    }

    public static void removeEventRegistrationRequest(int index, String attendeeEmail,
                                                      RegistrationRequestStatus currentStatus) {

        if(currentStatus.equals(RegistrationRequestStatus.APPROVED)) {
            events[index].getApprovedRequests().remove(attendeeEmail, attendeeEmail);
        } else if(currentStatus.equals(RegistrationRequestStatus.PENDING)) {
            events[index].getPendingRequests().remove(attendeeEmail, attendeeEmail);
        }
    }

    public static void printEventInfo(int index) {
        Event e = events[index];
        System.out.println("index: " + index + "\nTitle: " + e.getTitle() + " \nDescription: " + e.getDescription() + "\nEvent address: " + e.getEventAddress() + "\nOrg Email: " + e.getOrganizerEmail() + "\nDate Millis: " + e.getDateMillis() + "\nStart Millis: " + e.getStartTimeMillis() + "\nEnd Millis: " + e.getEndTimeMillis() + "\nAuto Approve: " + e.getRegistrationRequestsAreAutoApproved());
        HashMap<String, String> approvedRequests = e.getApprovedRequests();
        if(approvedRequests!=null)
            System.out.println("Size of approved: " + approvedRequests.size());
        HashMap<String, String> pendingRequests = e.getPendingRequests();

        if(pendingRequests!=null)
            System.out.println("Size of pending: " + pendingRequests.size());
        HashMap<String, String> rejectedRequests = e.getRejectedRequests();

        if(rejectedRequests!=null)
            System.out.println("Size of rejected: " + rejectedRequests.size());
    }


    /**
     * Method to determine if attendee is able to register to an event
     * They are only allowed to register for an event that is currently happening or will happen
     * and when there are no time conflicts with other event's they are attending or possibly attending
     * @param attendeeEmail the email of the attendee trying to register for an event
     * @param e the event the attendee is trying to register for
     * @return whether the attendee is able to register for the event they chose
     */
    public static boolean canRegisterForEvent (String attendeeEmail, Event e) {
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

        for(int i = 0; i<events.length; i++) {
            Event curEvent = events[i];
            if(curEvent!=null) {

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
    private static boolean hasConflict(Event e, Event curEvent) {
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


    public static void registerForEvent(String attendeeEmail, int index) {
        //put into pendingRequests if registrationRequired, put into approvedRequests if !registrationRequired

        //first check if can register for an event
        if(canRegisterForEvent(attendeeEmail, events[index])) {
            if (events[index].getRegistrationRequestsAreAutoApproved()) { //check if registration is autoapproved
//				e.getApprovedRequests().put(attendeeEmail, attendeeEmail);
                addEventRegistrationRequest(index, attendeeEmail, RegistrationRequestStatus.APPROVED);
            } else { //place in pending request and await approval from organizer
//				e.getPendingRequests().put(attendeeEmail, attendeeEmail);
                addEventRegistrationRequest(index, attendeeEmail, RegistrationRequestStatus.PENDING);
            }
        }

    }

    public static void main(String[] args) {

        Attendee a1 = new Attendee("Rachel", "L", "rl@g.com", "p", "45 Mann", "123-456-7891");

        LocalDate d1 = LocalDate.of(2024, 11, 02);
        LocalTime st1 = LocalTime.of(12, 00);
        LocalTime et1 = LocalTime.of(14, 00);
        //past event auto approve
        Event e1 = new Event("e1", "e1", "first event", d1, st1, et1, "45 Mann", "j6@g.com", true);
        events[0] = e1;
        registerForEvent(a1.getEmail(), 0); //should not be able to register for event since past
        System.out.println("\n\nShould not be able to register for e1 since past");
        printEventInfo(0);

        //past event no approval
        Event e2 = new Event("e2", "e2", "second event", d1, st1, et1, "45 Mann", "j6@g.com", false);
        events[1] = e2;
        registerForEvent(a1.getEmail(), 1); //should not be able to register for event since past
        System.out.println("\n\nShould not be able to register for e2 since past event");
        printEventInfo(1);


        //TESTING WITHIN 24 HOURS FOR CANCEL REGISTRATION REQUEST
        LocalDate d24 = LocalDate.of(2024, 11, 29);
        LocalTime st24 = LocalTime.of(15, 30);
        LocalTime et24 = LocalTime.of(20, 00);
        Event e3 = new Event("e3", "e3", "within 24hrs event", d24, st24, et24, "45 Mann", "j6@g.com", true);
        events[2] = e3;
        registerForEvent(a1.getEmail(), 2); //should be able to register for event
        System.out.println("\n\nShould be able to register for e3 since future event");
        printEventInfo(2);
        cancelEventRegistrationRequest(a1.getEmail(), 2); //should not be able to cancel since within 24 hours
        System.out.println("\n\nShould not be able to cancel for e3 since within 24 hrs event");
        printEventInfo(2);

        //TESTING TIME CONFLICT FOR REGISTER FOR EVENTS
        //future event next month
        LocalDate dNM = LocalDate.of(2024, 12, 02);
        LocalTime stNM = LocalTime.of(12, 00);
        LocalTime etNM = LocalTime.of(14, 00);
        Event e4 = new Event("e4", "e4", "Next month future event", dNM, stNM, etNM, "45 Mann", "j6@g.com", true);
        events[3] = e4;
        registerForEvent(a1.getEmail(), 3); //should be able to register
        System.out.println("\n\nShould be able to register for e4 since future event");
        printEventInfo(3);


        //time conflict with future next month event #1 possibility
        LocalDate dTC1 = LocalDate.of(2024, 12, 02);
        LocalTime stTC1 = LocalTime.of(13, 30);
        LocalTime etTC1 = LocalTime.of(20, 00);
        Event e5 = new Event("e5", "e5", "Time conflict with e4 1st possibility", dTC1, stTC1, etTC1, "45 Mann", "j6@g.com", true);
        events[4] = e5;
        registerForEvent(a1.getEmail(), 4); //should not be able to register
        System.out.println("\n\nShould not be able to register for e5 since event time conflict with e4");
        printEventInfo(4);

        //time conflict with future next month event #2 possibility
        LocalDate dTC2 = LocalDate.of(2024, 12, 02);
        LocalTime stTC2 = LocalTime.of(10, 30);
        LocalTime etTC2 = LocalTime.of(13, 00);
        Event e6 = new Event("e6", "e6", "Time conflict with e4 2nd possibility", dTC2, stTC2, etTC2, "45 Mann", "j6@g.com", true);
        events[5] = e6;
        registerForEvent(a1.getEmail(), 5); //should not be able to register
        System.out.println("\n\nShould not be able to register for e6 since event time conflict with e4");
        printEventInfo(5);

        //time conflict with future next month event #3 possibility
        LocalDate dTC3 = LocalDate.of(2024, 12, 02);
        LocalTime stTC3 = LocalTime.of(10, 30);
        LocalTime etTC3 = LocalTime.of(20, 00);
        Event e7 = new Event("e7", "e7", "Time conflict with e4 3rd possibility", dTC3, stTC3, etTC3, "45 Mann", "j6@g.com", true);
        events[6] = e7;
        registerForEvent(a1.getEmail(), 6); //should not be able to register
        System.out.println("\n\nShould not be able to register for e7 since event time conflict with e4");
        printEventInfo(6);

        //time conflict with future next month event #4 possibility
        LocalDate dTC4 = LocalDate.of(2024, 12, 02);
        LocalTime stTC4 = LocalTime.of(12, 00);
        LocalTime etTC4 = LocalTime.of(13, 30);
        Event e8 = new Event("e8", "e8", "Time conflict with e4 4th possibility", dTC4, stTC4, etTC4, "45 Mann", "j6@g.com", true);
        events[7] = e8;
        registerForEvent(a1.getEmail(), 7); //should not be able to register
        System.out.println("\n\nShould not be able to register for e8 since event time conflict with e4");
        printEventInfo(7);

        cancelEventRegistrationRequest(a1.getEmail(), 3); //should be able to cancel an event next month
        System.out.println("\n\nShould be able to cancel for e4 since event time next month");
        printEventInfo(3);


    }

}
