package ca.seg2105project;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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

}
