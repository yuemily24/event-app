package Controllers;


public class VIPController extends AttendeeController {
    /**
     * Constructor for the Controllers.AttendeeController class.
     *
     * @param currentUserName The current username of the attendee.
     */
    public VIPController(String currentUserName) {
        super(currentUserName);
    }

    /**
     * Shows all Entities.Events that VIPAttendee can view
     */
    void viewAllEvents() {
        if (eventManager.getAllEvents().isEmpty()) {
            presenter.print("Looks like there are no events yet!");
        } else {
            int i = 1;
            for (String eventID : eventManager.getAllEvents()) {
                viewAllEventsHelper(eventID, i);
                i++;
            }
        }
    }

    /**
     * Attempts to sign up a VIPAttendee for an event; if capacity is reached, the user is waitlisted instead.
     *
     * @param username The username of the attendee being signed up for the event.
     * @param eventID  The ID of the event.
     */
    void eventSignUp(String username, String eventID) {
        if (!eventManager.getAllEvents().contains(eventID)) {
            presenter.print("Failure - no such event exists");
        } else if (userManager.getInfo(username).get("eventList").contains(eventID)) {
            presenter.print("Failure - you are already signed up for this event.");
        } else {
            eventSignupHelper(username, eventID);
        }
    }
}
