package Controllers;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AttendeeController extends UserController {

    /**
     * Constructor for the Controllers.AttendeeController class.
     *
     * @param currentUserName The current username of the attendee.
     */
    public AttendeeController(String currentUserName) {
        super(currentUserName);
    }

    /**
     * Homepage for an Attendee user; takes input to determine which actions to perform.
     */
    boolean homepage() {
        List<String> options1 = Arrays.asList("Events", "Mailbox", "Contacts", "Account settings", "Log out");

        presenter.print("##########################################################################");
        presenter.print("Welcome to your homepage.");
        int input1 = getInput(options1, true);
        if (input1 == 1) {
            // events
            homepageEvents();
            return true;
        } else if (input1 == 2) {
            // messaging
            homepageMailbox();
            return true;
        } else if (input1 == 3) {
            // contacts
            homepageContacts();
            return true;
        } else if (input1 == 4) {
            // account info
            homepageSettings();
            return true;
        } else {
            // logout
            logout();
            return false;
        }
    }

    // EVENT MODULES

    /**
     * Page for actions that an Attendee user can perform related to events.
     */
    void homepageEvents() {
        boolean looper = true;
        do {
            presenter.print("Events:");
            List<String> options = Arrays.asList("View schedule for all events", "View schedule for all registered events", "Sign up for an event", "Cancel registration for an event", "Get an HTML file for all events");

            int input = getInput(options, false);
            if (input == 1) {
                presenter.print("Published events:");
                viewAllEvents();
                pause();
            } else if (input == 2) {
                viewRegisteredAndWaitlistedEvents();
                pause();
            } else if (input == 3) {
                eventsEdit(1);
                pause();
            } else if (input == 4) {
                eventsEdit(2);
                pause();
            } else if (input == 5) {
                createHTMLEvents(eventManager);
                pause();
            } else {
                presenter.print("Returning to the previous menu.");
                looper = false;
            }
        } while (looper);
    }

    /**
     * Prints all events available
     */
    void viewAllEvents() { // Non-VIP Events
        if (eventManager.getAllEvents().isEmpty()) {
            presenter.print("Looks like there are no events yet!");
        } else {
            int i = 1;
            for (String eventID : eventManager.getAllEvents()) {
                if (eventManager.getInfo(eventID).get("status").get(0).equals("false")) { // only for non-vip events only
                    Map<String, List<String>> eventInfo = eventManager.getInfo(eventID);
                    viewAllEventsHelper(eventID, i);
                    i++;
                }
            }
        }
    }

    /**
     * Helper function for viewAllEvents. Prints out the current event (indicated by eventID), formatted.
     *
     * @param eventID the eventID of the event to be printed out
     * @param index   the current number in the loop of viewAllEvents.
     */
    void viewAllEventsHelper(String eventID, int index) {
        Map<String, List<String>> eventInfo = eventManager.getInfo(eventID);
        int remainingSpace = Integer.parseInt(eventInfo.get("capacity").get(0)) - eventInfo.get("signups").size();
        String spaceText = remainingSpace > 0 ? "Remaining spots: " + remainingSpace : "Waitlist rank: " + eventInfo.get("waitlist").size();
        // prints out a string of this format: # - Event title @ Location, from startTime to endTime; Remaining spots: 3
        presenter.print(index + " - " + eventInfo.get("title").get(0) + " @ " + eventInfo.get("location").get(0) +
                ", from " + eventInfo.get("time").get(0) + " to " + eventInfo.get("time").get(1) + "; " + spaceText);
    }

    /**
     * Attempts to sign up an attendee for an event; if capacity is reached, the user is waitlisted instead.
     *
     * @param username The username of the attendee being signed up for the event.
     * @param eventID  The ID of the event.
     */
    void eventSignUp(String username, String eventID) {
        if (!eventManager.getAllEvents().contains(eventID) || eventManager.getInfo(eventID).get("status").get(0).equals("true")) {
            presenter.print("Failure - either no such event exists, or this is a VIP-only event.");
        } else if (userManager.getInfo(username).get("eventList").contains(eventID)) {
            presenter.print("Failure - you are already signed up for this event.");
        } else {
            eventSignupHelper(username, eventID);
        }
    }

    /**
     * Helper for eventSignUp. Signs up or waitlists the attendee to the event, depending on whether the event is full.
     *
     * @param username username of the attendee
     * @param eventID  eventID of the event
     */
    void eventSignupHelper(String username, String eventID) {
        boolean signedUp = eventManager.addUserToEvent(username, eventID);
        if (signedUp) {
            userManager.addEvent(username, eventID);
        } else {
            userManager.addWaitlistedEvent(username, eventID);
        }
        String outputText = signedUp ? "signed up" : "waitlisted";
        String eventTitle = eventManager.getInfo(eventID).get("title").get(0);
        presenter.print("Success - you are " + outputText + " for " + eventTitle + ".");
    }

    /**
     * Cancels an attendee's registration for an event. If the attendee is waitlisted for the event, then they get removed
     * from the waitlist instead.
     *
     * @param username username of the attendee.
     * @param eventID  eventID of the event.
     */
    void eventCancel(String username, String eventID) {
        if (!eventManager.getAllEvents().contains(eventID)) {
            presenter.print("Failure - no such event exists.");
        } else {
            userManager.removeEvent(username, eventID);
            userManager.removeWaitlistedEvent(username, eventID);
            // moves next user out of waitlist and into signups
            String eventTitle = eventManager.getInfo(eventID).get("title").get(0);
            String nextUser = eventManager.removeUserFromEvent(username, eventID);
            if (!nextUser.equals("System")) {
//                if (!eventManager.getInfo("waitlist").isEmpty()) {
                userManager.removeWaitlistedEvent(nextUser, eventID);
                userManager.addEvent(nextUser, eventID);
                sendSystemMessage(nextUser, "You have left the waitlist for " + eventTitle + ", and are now signed up for the event!");
//                }
            }
            presenter.print("Success - registration cancelled.");
        }
    }

    /**
     * Prints the list of events that an attendee is signed up or waitlisted for.
     */
    void viewRegisteredAndWaitlistedEvents() {
        List<String> eventList = userManager.getInfo(currentUserName).get("eventList");
        List<String> eventWaitlist = userManager.getInfo(currentUserName).get("eventWaitlist");
        int i = 1;
        presenter.print("Your registered events:");
        if (eventList.isEmpty()) {
            presenter.print("Looks like you aren't registered for anything yet!");
        } else {
            for (String eventID : eventList) {
                presenter.print(i + " - " + eventManager.getInfo(eventID).get("eventToString").get(0));
                i++;
            }
        }
        presenter.print("Your waitlisted events:");
        if (eventWaitlist.isEmpty()) {
            presenter.print("Looks like you aren't on any waitlists! Nice!");
        } else {
            for (String eventID : eventWaitlist) {
                presenter.print(i + " - " + eventManager.getInfo(eventID).get("eventToString").get(0));
                i++;
            }
        }
    }

    /**
     * Edits registration and cancellation status of the user's events.
     *
     * @param index The input from the user.
     */
    void eventsEdit(int index) {
        // looper if there are no events to sign up or cancel for
        Map<String, List<String>> userInfo = userManager.getInfo(currentUserName);
        int numOfEvents;
        List<String> eventList;
        if (index == 1) {
            presenter.print("Select an event to sign up for. If you wish to go back to the previous menu, enter -1.");
            eventList = eventManager.getAllEvents();
            this.viewAllEvents();
        } else {
            presenter.print("Select the event you would like to cancel registration for. If you wish to go back to the previous menu, enter -1.");
            eventList = Stream.concat(userInfo.get("eventList").stream(),
                    userInfo.get("eventWaitlist").stream()).distinct().collect(Collectors.toList());
            this.viewRegisteredAndWaitlistedEvents();
        }
        numOfEvents = eventList.size();
        String intRegex = "[1-9]+";
        String selection;
        String eventID;
        do {
            selection = sc.nextLine();
            if (selection.equals("-1")) {
                break;
            }
            if (!selection.matches(intRegex) || Integer.parseInt(selection) > numOfEvents || Integer.parseInt(selection) < 0) {
                presenter.print("Please enter a valid option.");
            }
        } while (!selection.matches(intRegex));
        if (!selection.equals("-1")) {
            eventID = eventList.get(Integer.parseInt(selection) - 1);
            if (index == 1) {
                eventSignUp(currentUserName, eventID);
            } else {
                eventCancel(currentUserName, eventID);
            }
        }
    }

    // MAILBOX MODULES

    /**
     * Homepage for an Attendee's mailbox, can view and send messages.
     */
    void homepageMailbox() {
        boolean looper = true;
        do {
            presenter.print("Mailbox:");
            List<String> options = Arrays.asList("Manage all read messages", "Manage all unread messages", "Manage all archived messages",
                    "Send a new message", "Search chat history");

            int input = getInput(options, false);
            if (input == 1) {
                messageManage("readList");
                pause();
            } else if (input == 2) {
                messageManage("unreadList");
                pause();
            } else if (input == 3) {
                messageManage("archiveList");
                pause();
            } else if (input == 4) {
                presenter.print("Enter the username of the receiver:");
                String receiverUsername = sc.nextLine();
                String message = messageSelectPrompt();
                sendSingleMessage(currentUserName, receiverUsername, message);
                pause();
            } else if (input == 5) {
                searchChatHistory();
                pause();
            } else {
                presenter.print("Returning to the previous menu.");
                looper = false;
            }
        } while (looper);
    }


    // CONTACTS MODULE

    /**
     * Homepage for an Attendee's contacts, can view, add, and remove contacts, as well as recommended contacts.
     */
    void homepageContacts() {
        boolean looper = true;
        List<String> options = Arrays.asList("View all contacts", "View recommended contacts", "Add a contact", "Remove a contact");
        Map<String, List<String>> currentUserInfo = userManager.getInfo(currentUserName);
        do {
            presenter.print("Contacts:");
            int input = getInput(options, false);
            if (input == 1) {
                List<String> users = currentUserInfo.get("contactList");
                currentContacts(users);
                pause();
            } else if (input == 2) {
                List<String> listOfEvents = currentUserInfo.get("eventList");
                recommendedContacts(listOfEvents);
                pause();
            } else if (input == 3) {
                contactsEdit(1);
                pause();
            } else if (input == 4) {
                contactsEdit(2);
                pause();
            } else {
                presenter.print("Returning to the previous menu.");
                looper = false;
            }
        } while (looper);
    }

    /**
     * Edits the user's contact list; can add or remove a contact depending on the input parameter.
     *
     * @param i The input from the user.
     */
    void contactsEdit(int i) {
        Map<String, List<String>> currentUserInfo = userManager.getInfo(currentUserName);
        if (i == 1) {
            presenter.print("Enter the username of the contact you would like to add.");
        } else {
            presenter.print("Enter the username of the contact you would like to remove.");
        }
        String otherUsername = sc.nextLine();
        if (i == 1) {
            if (userManager.existsUser(otherUsername) && !currentUserName.equals(otherUsername)) {

                // if the user being added is already in the list, there is no need to tell them
                if (!currentUserInfo.get("contactList").contains(otherUsername)) {
                    userManager.addContact(currentUserName, otherUsername);
                }
                presenter.print("Success - contact added.");
            } else if (currentUserName.equals(otherUsername)) {
                presenter.print("Failure - you cannot add yourself to the contact list.");
            } else {
                presenter.print("Failure - the selected user does not exist.");
            }
        } else {
            if (currentUserInfo.get("contactList").contains(otherUsername)) {
                userManager.removeContact(currentUserName, otherUsername);
                presenter.print("Success - contact removed.");
            } else {
                presenter.print("Failure - the selected user does not exist.");
            }
        }
    }

    /**
     * Prints out recommended contacts for the current attendee, based on the number of shared events.
     *
     * @param listOfEvents list of all current events
     */
    void recommendedContacts(List<String> listOfEvents) {
        // note that we aren't getting events from their waitlist
        List<String> listOfCommonUsers = new ArrayList<>();
        for (String event : listOfEvents) {
            listOfCommonUsers.addAll(eventManager.getInfo(event).get("signups"));
        }
        Map<String, Long> mapOfUsers = listOfCommonUsers.stream()
                .collect(Collectors.groupingBy(w -> w, Collectors.counting())); // maps the number of common events to their username

        // removes self and already added contacts
        mapOfUsers.remove(currentUserName);
        List<String> usersAdded = new ArrayList<>();
        List<String> currentContacts = userManager.getInfo(currentUserName).get("contactList");
        for (String username : mapOfUsers.keySet()) {
            if (currentContacts.contains(username)) {
                usersAdded.add(username);
            }
        }
        for (String username : usersAdded) {
            mapOfUsers.remove(username);
        }

        List<Map.Entry<String, Long>> top5Users = mapOfUsers.entrySet().stream() // converts each item in map to a key-value pair, then converts the map to a stream
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())) // compares each pair by value, and sorts by highest to lowest
                .limit(5) // gets top 5 of the list
                .collect(Collectors.toList()); // converts to list
        if (!top5Users.isEmpty()) {
            for (Map.Entry<String, Long> userPair : top5Users) {
                String username = userPair.getKey();
                presenter.print(userManager.getInfo(username).get("displayName").get(0) + " - username: " + username + "; shares " + userPair.getValue() + " events with you.");
            }
        } else {
            System.out.println("You have no recommended contacts!");
        }
    }

    /**
     * Prints out the contacts the attendee has added to their contact list.
     *
     * @param users the list of users the attendee has added
     */
    void currentContacts(List<String> users) {
        if (users.isEmpty()) {
            presenter.print("Looks like you have no contacts!");
        } else {
            presenter.print("Your contacts:");
            for (String i : users) {
                Map<String, List<String>> userInfo = userManager.getInfo(i);
                presenter.print(userInfo.get("displayName").get(0));
            }
        }
    }

}
