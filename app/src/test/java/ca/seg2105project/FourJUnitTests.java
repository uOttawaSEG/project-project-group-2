package ca.seg2105project;

import static org.junit.Assert.*;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import ca.seg2105project.model.eventClasses.Event;
import ca.seg2105project.model.userClasses.Administrator;
import ca.seg2105project.model.userClasses.Attendee;
import ca.seg2105project.model.userClasses.Organizer;

/**
 * A class to handle the JUnit testing.
 */
public class FourJUnitTests {
    @Test
    /**
     * Tests the method getFirstName() of an Attendee.
     */
    public void testAttendeeGetFirstName() {
        Attendee att = new Attendee("Kunala", "Deotare", "kdeot090@uottawa.ca", "myPassword", "45 Mann", "123-456-6789");
        String actual = att.getFirstName();
        String expected = "Kunala";
        assertEquals(expected, actual);
    }

    @Test
    /**
     * Tests the method getOrganizationName() of an Organizer.
     */
    public void testOrganizerGetOrganizationName() {
        Organizer org = new Organizer("Rachel", "Luo", "raclu@uottawa.ca", "herPassword", "also 45 Mann", "999-999-9999", "EAMS Events App");
        String actual = org.getOrganizationName();
        String expected = "EAMS Events App";
        assertEquals(expected, actual);
    }

    @Test
    /**
     * Tests the method getEventId() for Event.
     */
    public void testEventGetEventID() {
        LocalDate d1 = LocalDate.of(2024, 11, 12);
        LocalTime st1 = LocalTime.of(12, 30);
        LocalTime et1 = LocalTime.of(14, 30);
        //past event auto approve
        Event e1 = new Event("12345EventID", "Event Testing", "testing getEventID()", d1, st1, et1, "45 Mann", "j6@g.com", true);

        String expected = "12345EventID";
        String actual = e1.getEventID();
        assertEquals("getEventId() of Event class failed", expected, actual);
    }

    @Test
    /**
     * Tests the fact that an Administrator's address is null but it still has a getAddress() method due to inheritance from User.
     */
    public void testAdministratorGetAddress() {
        Administrator adm = new Administrator("mickey@gmail.com", "mickeysPassword");
        String actual = adm.getAddress();
        assertNull(actual);
    }
}
