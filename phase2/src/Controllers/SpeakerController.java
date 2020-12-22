package Controllers;

import java.util.*;

public class SpeakerController extends UserController {

    /**
     * Constructs a SpeakerController object.
     *
     * @param currentUserName The current username of the attendee.
     */
    public SpeakerController(String currentUserName) {
        super(currentUserName);
    }

    /**
     * The homepage for a Speaker user; takes input to determine which actions to perform.
     */
    @Override
    public boolean homepage() {
        List<String> options1 = new ArrayList<>(Arrays.asList("Events", "Chats", "Account settings", "Log out"));
        // changed to List
        presenter.print("##########################################################################");
        presenter.print("Welcome to your home page.");
        int input1 = getInput(options1, true);
        //Events
        if (input1 == 1) {
            homepageEventsSpeaker();
            return true;
            //Chats
        } else if (input1 == 2) {
            homepageMailboxSpeaker();
            return true;
            //Account info
        } else if (input1 == 3) {
            homepageSettings();
            return true;
        } else {
            logout();
            return false;
        }
    }

    // MESSAGE MODULES

    /**
     * Home page for different actions that the Speaker can perform in their mailbox.
     */
    void homepageMailboxSpeaker() {
        boolean looper = true;
        List<String> list = Arrays.asList("Manage all read messages", "Manage all unread messages", "Manage archived messages",
                "Message a user in an event", "Message all users in an event", "Search chat history");
        List<String> options = new ArrayList<>(list); //changed to List
        do {
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
                messageIndividualInEvent();
                pause();
            } else if (input == 5) {
                messageEveryoneInEvent();
                pause();
            } else if (input == 6) {
                searchChatHistory();
                pause();
            } else {
                presenter.print("Returning to the previous menu.");
                looper = false;
            }
        } while (looper);
    }

    /**
     * Messages everyone within the event if the sender is a speaker.
     * If the speaker is not attending the event, it will not be possible to message everyone in that event.
     */
    private void messageEveryoneInEvent() {
        presenter.print("You have selected the option to message everyone within an event.");

        String message = messageSelectPrompt();

        String eventIDChoice = eventSelectPrompt();
        if (!eventIDChoice.equals("")) {
            sendMessageToAllInEvent(eventIDChoice, message);
        } else {
            presenter.print("Returning to previous menu.");
        }

    }

    /**
     * Messages a user within the event if the sender is a speaker.
     * If the speaker is not attending the event, it will not be possible to message individual in that event.
     */
    private void messageIndividualInEvent() {
        presenter.print("You have selected the option to message an individual within an event.");

        String message = messageSelectPrompt();

        String eventIDChoice = eventSelectPrompt();
        if (!eventIDChoice.equals("")) {
            sendMessageToOneInEvent(eventIDChoice, message);
        } else {
            presenter.print("Returning to previous menu.");
        }
    }


    // EVENT MODULES

    /**
     * Home page that allows the Speaker to view the events they are set to speak at.
     */
    private void homepageEventsSpeaker() {
        boolean looper = true;
        List<String> list = Arrays.asList("View all events you are speaking at", "Get an HTML file for all events");
        List<String> options = new ArrayList<>(list); //changed to List
        do {
            int input = getInput(options, false);
            if (input == 1) {
                printListOfTalksForSpeaker(currentUserName);
                pause();
            } else if (input == 2) {
                createHTMLEvents(eventManager);
                pause();
            } else {
                presenter.print("Returning to the previous menu");
                looper = false;
            }
        } while (looper);
    }

    /**
     * Prints out a list of events in string that a speaker is speaking at.
     *
     * @param currentUserName Speaker's username.
     */
    private void printListOfTalksForSpeaker(String currentUserName) {
        Map<String, List<String>> currentUserInfo = userManager.getInfo(currentUserName);
        if (!eventManager.getAllEvents().isEmpty()) {
            for (String eventID : currentUserInfo.get("eventList")) {
                presenter.print(eventManager.getInfo(eventID).get("eventToString"));
            }
        } else {
            presenter.print("Looks like you aren't speaking in any events for now.");
        }
    }

    /**
     * Prompt the user to select an event from the list of events that they are attending/speaking at.
     *
     * @return the eventID of the event selected by the user; if user cancels the action or chooses an invalid action,
     * returns an empty string
     */
    private String eventSelectPrompt() {
        List<String> events = userManager.getInfo(currentUserName).get("eventList");
        if (!events.isEmpty()) {
            presenter.print("Please enter which event you would like to message.");
            List<String> options = new ArrayList<>();
            for (String eventID : events) {
                options.add(eventManager.getInfo(eventID).get("eventToString").get(0));
            }
            int eventChoice = getInput(options, false);
            if (eventChoice != -1) {
                return events.get(eventChoice - 1);
            } else {
                return "";
            }
        } else {
            presenter.print("You cannot send messages to an event because you are not signed up for any events.");
            return "";
        }
    }

    /**
     * Sends the message to a User within the Event. Takes input to determine the User.
     *
     * @param message       The message to be sent.
     * @param eventIDChoice The event that the receiver of the message is in.
     */
    private void sendMessageToOneInEvent(String eventIDChoice, String message) {
        if (!eventIDChoice.equals("")) {
            presenter.print("Choose the user that you want to message.");

            List<String> userOptionString = new ArrayList<>();
            List<String> users = eventManager.getInfo(eventIDChoice).get("signups");
            for (String username : users) {
                userOptionString.add(userManager.getInfo(username).get("userToString").get(0));
            }
            int userChoice = getInput(userOptionString, false);
            if (userChoice != -1) {
                sendSingleMessage(currentUserName, users.get(userChoice - 1), message);
                presenter.print("Success - message sent to " +
                        userManager.getInfo(users.get(userChoice - 1)).get("displayName").get(0));
            }
        }
    }

    /**
     * Sends the message to all Users within the Event
     *
     * @param message       The message to be sent.
     * @param eventIDChoice The event that all the users attending will receive the message.
     */
    private void sendMessageToAllInEvent(String eventIDChoice, String message) {
        if (!eventIDChoice.equals("")) {
            presenter.print("Sending messages...");
            for (String usernamesAttendingEvent :
                    eventManager.getInfo(eventIDChoice).get("signups")) {
                sendSingleMessage(currentUserName, usernamesAttendingEvent, message);
            }
        }
    }
}
