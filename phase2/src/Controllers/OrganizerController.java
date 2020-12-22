package Controllers;

import java.util.Arrays;
import java.util.Collections;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.time.LocalTime;
import java.time.LocalDateTime;

public class OrganizerController extends AttendeeController {
    public OrganizerController(String currentUserName) {
        super(currentUserName);
    }

    /**
     * The homepage for an Organizer user; takes input to determine which actions to perform.
     */
    public boolean homepage() {
        List<String> options = Arrays.asList("Administrative", "Events", "Mailbox", "Contacts", "Account settings", "Logout");

        presenter.print("##########################################################################");
        presenter.print("Welcome to your homepage.");
        int input = getInput(options, true);
        if (input == 1) {
            homepageAdmin();
            return true;
        } else if (input == 2) {
            homepageEvents();
            return true;
        } else if (input == 3) {
            homepageMailbox();
            return true;
        } else if (input == 4) {
            homepageContacts();
            return true;
        } else if (input == 5) {
            homepageSettings();
            return true;
        } else {
            logout();
            return false;
        }
    }

    // ADMIN MODULES

    /**
     * Home page for different admin tasks that can be performed.
     */
    private void homepageAdmin() {
        boolean looper = true;
        do {
            presenter.print("Administrative:");
            List<String> list = Arrays.asList("Create a user account", "Create a room", "Manage events", "Messaging options", "View summary statistics");
            List<String> options = new ArrayList<>(list); // changed to List

            int input = getInput(options, false);
            if (input == 1) {
                adminCreateUser();
                pause();
            } else if (input == 2) {
                adminCreateRoom();
                pause();
            } else if (input == 3) {
                adminEvents();
                // no pause here as it is a menu of its own
            } else if (input == 4) {
                adminMessaging();
                // no pause here as it is a menu of its own
            } else if (input == 5) {
                adminSummaryStats();
                // no pause here as it is a menu of its own
            } else {
                presenter.print("Returning to the previous menu.");
                looper = false;
            }
        } while (looper);
    }

    /**
     * Create a user in system
     */
    private void adminCreateUser() {
        presenter.print("Enter the user type of the new account:\n0 - Attendee\n1 - Organizer\n2 - Speaker \n3 - VIP");
        String userType = sc.nextLine();
        String intRegex = "[0-3]"; // 3 is for the new user type, whatever it is going to be (implemented)
        while (!userType.matches(intRegex)) {
            presenter.print("Please enter a valid user type.");
            userType = sc.nextLine();
        }
        presenter.print("Enter the username of the new account:");
        String username = sc.nextLine();
        presenter.print("Enter the desired password");
        String password = sc.nextLine();
        presenter.print("Enter the desired display name:");
        String displayName = sc.nextLine();

        if (!userManager.existsUser(username)) {
            userManager.createUser(username, password, Integer.parseInt(userType), displayName);
            presenter.print("Success - the new account has been created.");
        } else {
            presenter.print("Failure - the selected username is already in use. Please try again.");
        }
    }

    /**
     * Creates a room that can hold an event.
     */
    private void adminCreateRoom() {
        presenter.print("Enter the desired room name:");
        String roomName = sc.nextLine();
        String capacity;
        do {
            presenter.print("Please enter an integer for the capacity of the room.");
            capacity = sc.nextLine();
        } while (!intCheck(capacity) || Integer.parseInt(capacity) < 1);
        if (!roomManager.existsRoom(roomName)) {
            roomManager.createRoomInSystem(roomName, Integer.parseInt(capacity));
            presenter.print("Success - room created.");
        } else {
            presenter.print("Failure - the room ID is already is use. Please try again.");
        }
    }

    /**
     * page for different event management tasks to perform
     */
    private void adminEvents() {
        boolean looper = true;
        do {
            presenter.print("Event Management:");
            List<String> options = Arrays.asList("Create an Event", "Modify an Event", "Delete an Event", "Get an HTML file for all events");

            int input = getInput(options, false);
            if (input == 1) {
                eventCreate(); // replace with line above for p2
                pause();
            } else if (input == 2) {
                eventModify();
                pause();
            } else if (input == 3) {
                eventDelete();
                pause();
            } else if (input == 4) {
                createHTMLEvents(eventManager);
                pause();
            } else {
                presenter.print("Returning to the previous menu.");
                looper = false;
            }
        } while (looper);
    }

    /**
     * Page for the Organizer to view summary statistics, including user activation, organizer/speaker popularity,
     * events popularity
     */
    private void adminSummaryStats() {
        boolean looper = true;
        do {
            presenter.print("Summary statistics:");
            List<String> options = Arrays.asList("User activity summary", "Speaker popularity", "Events popularity");

            int input = getInput(options, false);
            if (input == 1) {
                adminUserActivity();
            } else if (input == 2) {
                mostPopularSpeakers();
                leastPopularSpeakers();
                pause();
            } else if (input == 3) {
                adminEventPopularity();
            } else {
                presenter.print("Returning to the previous menu.");
                looper = false;
            }
        } while (looper);
    }

    /**
     * Page for the Organizer to choose which type of user activity summary to view
     */
    private void adminUserActivity() {
        boolean looper = true;
        do {
            presenter.print("User activity summary:");
            List<String> options = Arrays.asList("Attendees", "Organizers", "Speakers","VIP attendees");

            int input = getInput(options, false);
            if (input == 1) {
                mostActiveAttendees();
                leastActiveAttendees();
                pause();
            } else if (input == 2) {
                mostActiveOrganizer();
                leastActiveOrganizer();
                pause();
            } else if (input == 3) {
                mostActiveSpeakers();
                leastActiveSpeakers();
                pause();
            } else if (input == 4){
                mostActiveVIP();
                leastActiveVIP();
                pause();
            } else {
                presenter.print("Returning to the previous menu.");
                looper = false;
            }
        } while (looper);
    }

    /**
     * Page for the Organizer to choose to view popularity of all events/ events given be a specific speaker
     */
    private void adminEventPopularity() {
        boolean looper = true;
        do {
            presenter.print("Events popularity");
            List<String> options = Arrays.asList("of all Events", "Events given by a specific speaker");

            int input = getInput(options, false);
            if (input == 1) {
                mostPopularEventsOfAll();
                leastPopularEventsOfAll();
                pause();
            } else if (input == 2) {
                adminEventsPopularityBySpeaker();
            } else {
                presenter.print("Returning to the previous menu.");
                looper = false;
            }
        } while (looper);
    }

    /**
     * Show top 10 most and least popular talks given be a specific speaker.
     */
    private void adminEventsPopularityBySpeaker() {
        boolean looper = true;
        do {
            presenter.print("Please choose a speaker to view the most/least popular talks he/she given:");
            List<String> list = userManager.getAllSpeakers();
            List<String> options = new ArrayList<>(list); // changed to List

            int input = getInput(options, false);
            for (int i = 1; i <= list.size(); i++) {
                if (input == i) {
                    if (userManager.getInfo(list.get(i - 1)).get("eventList").size() == 0) {
                        presenter.print("Looks like this speaker hasn't given any talk!");
                    } else {
                        presenter.print("Top 10 most popular talks given by " + list.get(i - 1) + " are: ");
                        mostPopularEventsOfOne(list.get(i - 1));
                        presenter.print("Top 10 least popular talks given by " + list.get(i - 1) + " are: ");
                        leastPopularEventsOfOne(list.get(i - 1));
                    }
                    pause();
                }
            }
            if (input == -1) {
                presenter.print("Returning to the previous menu.");
                looper = false;
            }
        } while (looper);
    }

    /**
     * Create an event in system.
     */
    private void eventCreate() {
        // set title
        presenter.print("Enter the event title:");
        String eventTitle = sc.nextLine();

        // set time
        LocalDateTime[] timeOfEvent = new LocalDateTime[2];
        timeOfEvent[0] = ecSetTime(true);
        timeOfEvent[1] = ecSetTime(false);

        // set room
        presenter.print("Please select a room for your event: ");
        String roomName = ecSetRoom(timeOfEvent);

        // if room exists, set speaker, status and attendee capacity, set speakers (optional), check for room and speaker conflicts
        if (roomManager.existsRoom(roomName)) {
            int overallCapacity = Integer.parseInt(roomManager.getInfo(roomName).get("capacity").get(0));
            int maxSpeakers = overallCapacity - 1;
            int numberOfSpeakers = ecSetCapacity(maxSpeakers, "speaker");
            int maxAttendees = overallCapacity - numberOfSpeakers;
            int numberOfAttendees = ecSetCapacity(maxAttendees, "attendee");
            boolean statusOfEvent = ecSetEventStatus();

            String[] speakers = new String[numberOfSpeakers];
            Arrays.fill(speakers, "System");

            boolean speakersNow = ecSetSpeakersNow(); // to set speakers now or not
            boolean validSpeakers;
            if (speakersNow) {
                validSpeakers = ecAssignSpeakers(numberOfSpeakers, speakers, timeOfEvent); // assigns speakers, and checks for conflicts in speakers
            } else {
                validSpeakers = true;
            }
            if (validSpeakers && checkEventValidity(timeOfEvent, roomName)) {
                ecEventCreator(eventTitle, timeOfEvent, speakers, roomName, numberOfAttendees, statusOfEvent);
                presenter.print("Success - event created.");
            } else {
                presenter.print("Failure - one or more inputted parameters were invalid. Please try again.");
            }
        } else {
            presenter.print("Failure - one or more inputted parameters were invalid. Please try again.");
        }
    }

    /**
     * Helper method for eventCreate which sets start time and end time.
     *
     * @param startStop If true, set the start time. If false, set the end time.
     * @return The time set based on user input.
     */
    private LocalDateTime ecSetTime(boolean startStop) {
        String startOrEnd = startStop ? "start " : "end "; // if true, "start", else "end"
        presenter.print("Enter the Event's " + startOrEnd + "time in 24-hour time in the following format, HH:MM.");
        String timeString = sc.nextLine();
        boolean canYouReadTime = timeValidityCheck(timeString);
        while (!canYouReadTime) {
            presenter.print("Please enter the Event's " + startOrEnd + "time in 24-hour time using the HH:MM format, e.g. 09:05, 11:30, 15:00.");
            timeString = sc.nextLine();
            canYouReadTime = timeValidityCheck(timeString);
        }
        LocalTime time = LocalTime.parse(timeString);

        presenter.print("Enter the Event's " + startOrEnd + "date using the YYYY-MM-DD format, e.g. 1989-12-13.");
        String dateString = sc.nextLine();
        boolean canYouReadDate = dateValidityCheck(dateString);
        while (!canYouReadDate) {
            presenter.print("Please enter the Event's " + startOrEnd + "date in the YYYY-MM-DD format, e.g. 1989-12-13.");
            dateString = sc.nextLine();
            canYouReadDate = dateValidityCheck(dateString);
        }
        LocalDate date = LocalDate.parse(dateString);
        return time.atDate(date);
    }

    /**
     * Helper method for eventCreate which sets room for the event.
     *
     * @param timeOfEvent The start/end time for the event.
     * @return The name of the room set for the event.
     */
    private String ecSetRoom(LocalDateTime[] timeOfEvent) {
        String roomName = "";
        presenter.print("Available rooms");
        List<String> list = new ArrayList<>();
        List<String> roomNameList = new ArrayList<>();
        for (String room : roomManager.getAllRooms()) {
            if (!checkForRoomConflict(timeOfEvent, room)) {
                String roomToAdd = "Room " + room + "  Maximum capacity: " + roomManager.getInfo(room).get("capacity").get(0);
                list.add(roomToAdd);
                roomNameList.add(room);
            }
        }
        int input = getInput(list, false);
        for (int i = 1; i <= list.size(); i++) {
            if (input == i) {
                roomName = roomNameList.get(i - 1);
            }
        }
        return roomName;
    }

    /**
     * Helper method for eventCreate which sets the status of the event.
     *
     * @return The status for the event, true for VIP event and false for regular event.
     */
    private boolean ecSetEventStatus() {
        presenter.print("Enter the status of the event: True - VIP Event, False - Regular Event");
        String status = sc.nextLine();
        while (!((status.equalsIgnoreCase("true")) || (status.equalsIgnoreCase("false")))) {
            presenter.print("Please enter True or False");
            status = sc.nextLine();
        }
        return Boolean.parseBoolean(status);
    }

    /**
     * Helper function for eventCreate which sets the capacity of given type of user for the event
     *
     * @param maxCapacity Maximum capacity for the type of user given.
     * @param type Type of user.
     * @return The capacity for the given type of user.
     */
    private int ecSetCapacity(int maxCapacity, String type) {
        presenter.print("Enter the " + type + " capacity of the event, which can be no larger than " + maxCapacity + ".");
        String capacity = sc.nextLine();
        while (!naturalCheck(capacity) || Integer.parseInt(capacity) > maxCapacity) {
            if (!naturalCheck(capacity)) {
                presenter.print("The " + type + " capacity has to be an integer. Please try again.");
            } else {
                presenter.print("The " + type + " capacity of the event cannot be larger than " + maxCapacity + ". Please try again.");
            }
            capacity = sc.nextLine();
        }
        return Integer.parseInt(capacity);
    }

    /**
     * Helper method for eventCreate which asks the Organizer whether to assign speakers during the event creation.
     *
     * @return The number of speakers assigning during the event creation.
     */
    private boolean ecSetSpeakersNow() {
        presenter.print("To assign speakers now, enter 1; otherwise, enter 0.");
        String binaryRegex = "[0-1]";
        String input = sc.nextLine();
        while (!input.matches(binaryRegex)) {
            presenter.print("Please enter a valid option.");
            input = sc.nextLine();
        }
        return Integer.parseInt(input) == 1;
    }

    /**
     * Helper method for eventCreate which assigns speakers to the event. Returns true if speaker assignment was successful.
     *
     * @param numberOfSpeakers  Number of speakers assigning for the current event.
     * @param speakers A String array with placeholder speakers
     * @param timeOfEvent The start/end date and time for the event.
     * @return Whether or not the speakers have be assigned to the event successfully.
     */
    private boolean ecAssignSpeakers(int numberOfSpeakers, String[] speakers, LocalDateTime[] timeOfEvent) {
        for (int i = 0; i < numberOfSpeakers; i++) {
            presenter.print("Enter speaker " + (i + 1) + "'s username. If you realize you don't have enough speakers, type in System to exit the event creation process.");
            String speakerUserName;

            List<String> allSpeakers = userManager.getAllSpeakers();

            speakerUserName = sc.nextLine();
            while (!allSpeakers.contains(speakerUserName)) {
                if (speakerUserName.equals("System")) {
                    break;
                } else {
                    presenter.print("There is no Speaker with that username, please enter a valid username.");
                    speakerUserName = sc.nextLine();
                }
            }
            if (!speakerUserName.equals("System")) {
                String speakerType = userManager.getInfo(speakerUserName).get("userType").get(0);
                if (!userManager.existsUser(speakerUserName) || Integer.parseInt(speakerType) != 2 ||
                        checkForSpeakerConflict(timeOfEvent, speakerUserName)) {
                    presenter.print("Failure - the speaker you chose is not available at this time, or is not a speaker. Please try again.");
                    return false;
                }
                speakers[i] = speakerUserName;
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * Creates an event with eventManager, and adds the eventID to each speaker.
     *
     * @param title    The title of the event.
     * @param time     The time of the event.
     * @param speakers The names of the speaker featured.
     * @param location The room in which the event will take place.
     * @param status   The status of the event, True = VIP, False = Regular
     */
    private void ecEventCreator(String title, LocalDateTime[] time, String[] speakers, String location, int capacity, boolean status) {
        String eventID = eventManager.createNewEvent(title, time, speakers, location, capacity, status);

        // if speakers were assigned, then add the event to each speaker's event list
        if (!speakers[0].equals("System")) {
            for (String speakerUserName : speakers) {
                if (!speakerUserName.equals("System")) {
                    userManager.addEvent(speakerUserName, eventID);
                }
            }
        }
    }

    /**
     * Check whether the speaker's schedule has a conflict with the given event date and time or not.
     * Returns true if there is a conflict.
     *
     * @param eventTime array contaning start/end date and time for the event
     * @param speakerUsername speaker's username.
     * @return true if there is a conflict, false if there is not.
     */
    private boolean checkForSpeakerConflict(LocalDateTime[] eventTime, String speakerUsername) {
        for (String eventId : userManager.getInfo(speakerUsername).get("eventList")) {
            LocalDateTime[] eventAltTime = checkHelper(eventId);
            if (checkForTimeConflict(eventTime, eventAltTime)) { // true if there is a time conflict
                return true;
            }
        }
        return false;
    }

    /**
     * Helper method for checkForSpeakerConflict and checkForRoomConflict.
     *
     * @param eventId Event Id
     * @return Alternative event time
     */
    private LocalDateTime[] checkHelper(String eventId){
        String altStartTime = eventManager.getInfo(eventId).get("time").get(0);
        String altEndTime = eventManager.getInfo(eventId).get("time").get(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return new LocalDateTime[]{LocalDateTime.parse(altStartTime, formatter),
                LocalDateTime.parse(altEndTime, formatter)};
    }

    /**
     * Check whether or not there is a conflict with the room selected during the given time
     *
     * @param eventTime arrays containing the start/end date and time for the event.
     * @param roomName Name of the room selected.
     * @return true if there is a conflict, false if there is not.
     */
    private boolean checkForRoomConflict(LocalDateTime[] eventTime, String roomName) {
        // returns true if there is a conflict
        for (String eventID : eventManager.getAllEvents()) {
            LocalDateTime[] eventAltTime = checkHelper(eventID);
            if (checkForTimeConflict(eventTime, eventAltTime) &&
                    eventManager.getInfo(eventID).get("location").get(0).equals(roomName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the event ID is available, and if there is a room conflict.
     *
     * @param eventTime array containing start/end time of event.
     * @param roomName  room ID of the location that the event is taking place in.
     * @return true if there is no existing event ID, amd there is no room conflict
     */
    private boolean checkEventValidity(LocalDateTime[] eventTime, String roomName) {
        return !checkForRoomConflict(eventTime, roomName) && eventTime[0].isBefore(eventTime[1]);
    }

    /**
     * Checks if the time are parsable.
     *
     * @param input The intended time.
     * @return Whether or not it can be parsed.
     */
    private boolean timeValidityCheck(String input) {
        try {
            LocalTime.parse(input);
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }

    /**
     * Checks if the date are parsable.
     *
     * @param input The intended date.
     * @return Whether or not it can be parsed.
     */
    private boolean dateValidityCheck(String input) {
        try {
            LocalDate.parse(input);
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }


    /**
     * Check whether two time periods have conflict.
     *
     * @param eventTime1 start and end date and time for time period 1
     * @param eventTime2 start and end date and time for time period 2
     * @return true if there is a conflict, false if there is not.
     */
    private boolean checkForTimeConflict(LocalDateTime[] eventTime1, LocalDateTime[] eventTime2) {
        if (eventTime2[0].isEqual(eventTime1[0])) {
            return true;
        }
        else if (eventTime2[0].isBefore(eventTime1[0])) {
            return !eventTime2[1].isBefore(eventTime1[0]) && !eventTime2[1].isEqual(eventTime1[0]);
        }
        else {
            return !eventTime2[0].isAfter(eventTime1[1]) && !eventTime2[0].isEqual(eventTime1[1]);
        }
    }

    /**
     * Modify the event by changing its capacity or adding a speaker.
     */
    private void eventModify() {
        String eventID = selectEvent();
        String roomID = eventManager.getInfo(eventID).get("location").get(0);
        List<String> options = Arrays.asList("Change event capacity", "Add a speaker to an event");
        int input = getInput(options, false);
        switch (input) {
            case 1: // change event capacity
                int roomCapacity = Integer.parseInt(roomManager.getInfo(roomID).get("capacity").get(0));
                int numSpeakers = eventManager.getInfo(eventID).get("speakers").size();
                int maxCapacity = roomCapacity - numSpeakers;
                eventModifyCapacity(maxCapacity, eventID);
                break;
            case 2: // add speaker to event
                if (eventManager.hasUnfilledSpeakers(eventID)) {
                    presenter.print("Enter the username of the speaker you would like to add to the event:");

                    String username = sc.nextLine();
                    if (!userManager.existsUser(username) ||
                            Integer.parseInt(userManager.getInfo(username).get("userType").get(0)) != 2 || eventManager.getInfo(eventID).get("speakers").contains(username)) {
                        presenter.print("Failure - either the user does not exist, or they are not a speaker, or they are already speaking at the event. Please try again.");
                    } else {
                        eventManager.addEventSpeaker(eventID, username);
                        userManager.addEvent(username, eventID);
                        presenter.print("Success - the selected speaker has been added to the event.");
                    }
                } else {
                    presenter.print("The speaker capacity has been reached, so no more speakers can be added.");
                }
                break;
            case -1:
                presenter.print("Returning to the previous menu.");
                break;
        }
    }

    /**
     * Change the capacity of an event
     *
     * @param maxCapacity Maximum capacity for attendees.
     * @param eventID ID of the event.
     */
    private void eventModifyCapacity(int maxCapacity, String eventID) {
        // maxCapacity is room - speakers
        int currentAttendees = eventManager.getInfo(eventID).get("signups").size();
        presenter.print("Enter the attendee capacity of the event, which has to be between" +
                currentAttendees + " and " + maxCapacity + ".");
        String capacity = sc.nextLine();
        while (!naturalCheck(capacity) || Integer.parseInt(capacity) > maxCapacity || Integer.parseInt(capacity) < currentAttendees) {
            if (!naturalCheck(capacity)) {
                presenter.print("The attendee capacity has to be an integer. Please try again.");
            } else {
                presenter.print("The attendee capacity of the event has to be between" + currentAttendees + " and " + maxCapacity + ". Please try again.");
            }
            capacity = sc.nextLine();
        }
    }

    /**
     * Delete an event from the system.
     */
    private void eventDelete() { // phase 2
        String eventID = selectEvent();
        presenter.print("Are you sure you want to delete this event? Enter 1 if yes, 0 if no.");
        String binaryCheck = "[0-1]";
        String input = sc.nextLine();
        while (!input.matches(binaryCheck)) {
            presenter.print("Please enter a valid option.");
            input = sc.nextLine();
        }
        if (Integer.parseInt(input) == 1) {
            List<String> attendees = eventManager.getInfo(eventID).get("signups");
            for (String username : attendees) {
                userManager.removeEvent(username, eventID);
            }
            List<String> speakers = eventManager.getInfo(eventID).get("speakers");
            for (String username: speakers) {
                userManager.removeEvent(username, eventID);
            }
            eventManager.removeEvent(eventID);
            presenter.print("Success - event has been deleted.");
        } else {
            presenter.print("No changes were made.");

        }
    }

    /**
     * @return User selected EventID
     */
    private String selectEvent() {
        presenter.print("Please select an event. Input a number to the left of an event to select that event.");
        int i = 1;
        List<Map<String, List<String>>> eventInfoAsList = eventManager.getInfoAsList();
        for (Map<String, List<String>> eventInfoHash : eventInfoAsList) {
            presenter.print("[" + i + "]: " + eventInfoHash.get("eventToString"));
            i++;
        }

        String anyPosNumRegex = "^[1-9]\\d*$";
        String input;
        boolean validInput = false;
        do {
            input = sc.nextLine();
            if (input.matches(anyPosNumRegex) && Integer.parseInt(input) <= eventInfoAsList.size()) {
                validInput = true;
            } else {
                presenter.print("Not a valid input. Please try again.");
            }
        } while (!validInput);
        return eventInfoAsList.get(Integer.parseInt(input) - 1).get("eventID").get(0);
    }


    /**
     * Home page for messaging related actions that can be performed by the Organizer.
     */
    private void adminMessaging() {
        boolean looper = true;
        do {
            List<String> list = Arrays.asList("Message all Attendees and VIPAttendees", "Message all Speakers");
            List<String> options = new ArrayList<>(list);

            int input = getInput(options, false);
            if (input == 1) {
                presenter.print("Enter the message:");
                String message = sc.nextLine();
                messageAllAttendees(message, currentUserName);
                pause();
            } else if (input == 2) {
                presenter.print("Enter the message:");
                String message = sc.nextLine();
                messageAllSpeakers(message, currentUserName);
                pause();
            } else {
                presenter.print("Returning to the previous menu.");
                looper = false;
            }
        } while (looper);
    }

    /**
     * Method for sending a message to all Attendees.
     *
     * @param message        The message being sent.
     * @param senderUsername The username of the sender.
     */
    private void messageAllAttendees(String message, String senderUsername) {
        List<String> listOfAttendees = userManager.getAllAttendees(); // changed to List
        List<String> listOfVIPAttendees = userManager.getAllVIPAttendees();
        for (String attendee : listOfAttendees) {
            this.sendSingleMessage(senderUsername, attendee, message);
        }
        for (String VIPAttendee : listOfVIPAttendees) {
            this.sendSingleMessage(senderUsername, VIPAttendee, message);
        }
    }

    /**
     * Method for sending a message to all Speakers.
     *
     * @param message        The message being sent.
     * @param senderUsername The username of the sender.
     */
    private void messageAllSpeakers(String message, String senderUsername) {
        List<String> listOfSpeakers = userManager.getAllSpeakers(); // changed to List
        for (String speaker : listOfSpeakers) {
            this.sendSingleMessage(senderUsername, speaker, message);
        }
    }

    // ADMIN MODULES - GENERAL HELPER METHODS

    /**
     * Checks if the string input consists of integers.
     *
     * @param input The input to be checked.
     * @return Whether or not the string is made up of integers..
     */
    private boolean intCheck(String input) {
        String intRegex = "-?[0-9]+";
        return input.matches(intRegex);
    }

    /**
     * Checks if the string input consists of nature numbers.
     *
     * @param input The input to be checked.
     * @return Whether or not the string is made up of natural number.
     */
    private boolean naturalCheck(String input) {
        String naturalRegex = "[0-9]+";
        return input.matches(naturalRegex);
    }

    // STATISTICS MODULE

    /**
     * Show the top 10 most active speakers, based on the number of talks the speakers have given.
     */
    private void mostActiveSpeakers() {
        presenter.print("Top 10 most active speakers are: ");
        List<String> topList = findUsersAttendingMostEvents(userManager.getAllSpeakers());
        presenter.print(findActive(topList, "given"));
    }

    /**
     * Show the top 10 least active VIP attendees, based on the number of talks they have attended
     */
    private void leastActiveVIP() {
        presenter.print("Top 10 least active VIP members are: ");
        List<String> topList = findUsersAttendingFewestEvents(userManager.getAllVIPAttendees());
        presenter.print(findActive(topList, "attended"));
    }

    /**
     * Show the top 10 most active VIP attendees, based on the number of talks they have attended
     */
    private void mostActiveVIP() {
        presenter.print("Top 10 most active VIP members are: ");
        List<String> topList = findUsersAttendingMostEvents(userManager.getAllVIPAttendees());
        presenter.print(findActive(topList, "attended"));
    }

    /**
     * Show the top 10 least active speakers, based on the number of talks the speakers have given.
     */
    private void leastActiveSpeakers() {
        presenter.print("Top 10 least active speakers are: ");
        List<String> topList = findUsersAttendingFewestEvents(userManager.getAllSpeakers());
        presenter.print(findActive(topList, "given"));
    }

    /**
     * Show the top 10 most active attendees, based on the number of events the attendees have attended.
     */
    private void mostActiveAttendees() {
        presenter.print("Top 10 most active attendees are: ");
        List<String> topList = findUsersAttendingMostEvents(userManager.getAllAttendees());
        presenter.print(findActive(topList, "attended"));
    }

    /**
     * Show the top 10 least active attendees, based on the number of events the attendees have attended.
     */
    private void leastActiveAttendees() {
        presenter.print("Top 10 least active attendees are: ");
        List<String> topList = findUsersAttendingFewestEvents(userManager.getAllAttendees());
        presenter.print(findActive(topList, "attended"));
    }

    /**
     * Show the top 10 most active organizers, based on the number of events the organizers have organized.
     */
    private void mostActiveOrganizer() {
        presenter.print("Top 10 most active organizers are: ");
        List<String> topList = findUsersAttendingMostEvents(userManager.getAllOrganizer());
        presenter.print(findActive(topList, "organized"));
    }

    /**
     * Show the top 10 least active organizers, based on the number of events the organizers have organized.
     */
    private void leastActiveOrganizer() {
        presenter.print("Top 10 least active organizers are: ");
        List<String> topList = findUsersAttendingFewestEvents(userManager.getAllOrganizer());
        presenter.print(findActive(topList, "organized"));
    }

    /**
     * Show the top 10 most popular speakers, based on the average number of people attended the talks
     * the speakers have given.
     */
    private void mostPopularSpeakers() {
        presenter.print("Top 10 most popular speakers are:");
        List<String> topList = findUserMostPopular(userManager.getAllSpeakers());
        presenter.print(findPopular(topList));
    }

    /**
     * Show the top 10 least popular speakers, based on the average number of people attended the talks
     * the speakers have given.
     */
    private void leastPopularSpeakers() {
        presenter.print("Top 10 least popular speakers are:");
        List<String> topList = findUserLeastPopular(userManager.getAllSpeakers());
        presenter.print(findPopular(topList));
    }

    /**
     * Show the top 10 most popular events of all events in the system.
     */
    private void mostPopularEventsOfAll() {
        presenter.print("Top 10 most popular events are: ");
        List<String> topList = findEventsMostPopular(eventManager.getAllEvents());
        presenter.print(findPopularOfAll(topList));
    }

    /**
     * Show the top 10 least popular events of all events in the system.
     */
    private void leastPopularEventsOfAll() {
        presenter.print("Top 10 least popular events are: ");
        List<String> topList = findEventsLeastPopular(eventManager.getAllEvents());
        presenter.print(findPopularOfAll(topList));
    }

    /**
     * Show the top 10 most popular talks given by a specific speaker.
     */
    private void mostPopularEventsOfOne(String username) {
        List<String> topList = findEventsMostPopular(userManager.getInfo(username).get("eventList"));
        presenter.print(findPopularOfAll(topList));
    }

    /**
     * Show the top 10 least popular talks given by a specific speaker.
     */
    private void leastPopularEventsOfOne(String username) {
        List<String> topList = findEventsLeastPopular(userManager.getInfo(username).get("eventList"));
        presenter.print(findPopularOfAll(topList));
    }

    /**
     * Find user attending most events in the given user list.
     *
     * @param allUsers List of users.
     * @return Sorted list of users based on the number of events they attended.
     */
    private List<String> findUsersAttendingMostEvents(List<String> allUsers) {
        List<Integer> numEvents = getNumEvents(allUsers);
        Integer[] result = rankTheMost(numEvents);
        return userOrEventListBasedOnRank(allUsers, result);
    }
    /**
     * Find user attending fewest events in the given user list.
     *
     * @param allUsers List of users.
     * @return Sorted list of users based on the number of events they attended.
     */
    private List<String> findUsersAttendingFewestEvents(List<String> allUsers) {
        List<Integer> numEvents = getNumEvents(allUsers);
        Integer[] result = rankTheFewest(numEvents);
        return userOrEventListBasedOnRank(allUsers, result);
    }

    /**
     * Return the sorted list based on the rank array provided.
     *
     * @param listToBeSorted List of users or events to be sorted based on the rank array provided.
     * @param result rank array.
     * @return sorted list of users or events based on the rank.
     */
    private List<String> userOrEventListBasedOnRank(List<String> listToBeSorted, Integer[] result) {
        List<String> users = new ArrayList<>();
        int rank = 1;
        while (users.size() < 10 && rank <= Collections.max(Arrays.asList(result))) {
            for (int i = 0; i < result.length; i++) {
                if (result[i] == rank) {
                    users.add(listToBeSorted.get(i));
                }
            }
            rank++;
        }
        return users;
    }

    /**
     * Return an array containing the rank of each element in a given list, with rank 1 for the largest integer
     *
     * @param intList a list of integers to be sorted.
     * @return the array containing hte rank of each element in intList
     */
    private Integer[] rankTheMost(List<Integer> intList) {
        Integer[] result = new Integer[intList.size()];
        for (int i = 0; i < intList.size(); i++) {
            int count = 0;
            for (Integer integer : intList) {
                if (integer > intList.get(i)) {
                    count++;
                }
            }
            result[i] = count + 1;
        }
        return result;
    }

    /**
     * Return an array containing the rank of each element in a given list, with rank 1 for the smallest integer
     *
     * @param intList a list of integers to be sorted.
     * @return the array containing hte rank of each element in intList.
     */
    private Integer[] rankTheFewest(List<Integer> intList) {
        Integer[] result = new Integer[intList.size()];
        for (int i = 0; i < intList.size(); i++) {
            int count = 0;
            for (Integer integer : intList) {
                if (integer < intList.get(i)) {
                    count++;
                }
            }
            result[i] = count + 1;
        }
        return result;
    }

    /**
     * Find the most popular event in the given event list, based on the number of users attended
     *
     * @param eventIdList List of event IDs of the events to be sorted
     * @return a sorted list of event IDs
     */
    private List<String> findEventsMostPopular(List<String> eventIdList) {
        List<Integer> numSignUps = getNumSignups(eventIdList);
        Integer[] result = rankTheMost(numSignUps);
        return userOrEventListBasedOnRank(eventIdList, result);
    }

    /**
     * Find the least popular event in the given event list, based on the number of users attended
     *
     * @param eventIdList List of event IDs of the events to be sorted
     * @return a sorted list of event IDs
     */
    private List<String> findEventsLeastPopular(List<String> eventIdList) {
        List<Integer> numSignUps = getNumSignups(eventIdList);
        Integer[] result = rankTheFewest(numSignUps);
        return userOrEventListBasedOnRank(eventIdList, result);
    }

    /**
     * Find the most popular organizer/speaker in the given user list, based on the average number of users attended
     * the talks/events they have given/organized.
     *
     * @param userList List of usernames of the users to be sorted
     * @return a sorted list of usernames
     */
    private List<String> findUserMostPopular(List<String> userList) {
        List<Integer> avgNumOfAttendee = getAvgNumAttendees(userList);
        Integer[] result = rankTheMost(avgNumOfAttendee);
        return userOrEventListBasedOnRank(userList, result);
    }

    /**
     * Find the least popular organizer/speaker in the given user list, based on the average number of users attended
     * the talks/events they have given/organized.
     *
     * @param userList List of usernames of the users to be sorted
     * @return a sorted list of usernames
     */
    private List<String> findUserLeastPopular(List<String> userList) {
        List<Integer> avgNumOfAttendee = getAvgNumAttendees(userList);
        Integer[] result = rankTheFewest(avgNumOfAttendee);
        return userOrEventListBasedOnRank(userList, result);
    }

    /**
     * Get the average number of users attended the talks/events giver/organized by a specific speaker/organizer.
     *
     * @param username the username of the speaker/organizer.
     * @return the average number of users attended the talks/events.
     */
    private Integer getAvgAttendees(String username) {
        List<String> talks = userManager.getInfo(username).get("eventList");
        int numAttendee = 0;
        if (talks.size() == 0) {
            return 0;
        }
        for (String talk : talks) {
            numAttendee = numAttendee + eventManager.getInfo(talk).get("signups").size();
        }
        return numAttendee / talks.size();
    }

    /**
     * Get a list of number of registered events for the user list provided
     *
     * @param allUsers a list of usernames
     * @return a list of number of registered events for the users in allUsers
     */
    private List<Integer> getNumEvents(List<String> allUsers) {
        List<Integer> numEvents = new ArrayList<>();
        for (String username : allUsers) {
            numEvents.add(userManager.getInfo(username).get("eventList").size());
        }
        return numEvents;
    }

    /**
     * Get a list of number of event sign ups for the event list provided
     *
     * @param eventIdList a list of event IDs
     * @return a list of number of event sign ups for the events in eventIDList
     */
    private List<Integer> getNumSignups(List<String> eventIdList) {
        List<Integer> numSignUps = new ArrayList<>();
        for (String i : eventIdList) {
            numSignUps.add(eventManager.getInfo(i).get("signups").size());
        }
        return numSignUps;
    }

    /**
     * Get average number of users attended the talks/events given/organized by the speakers/organizers in a list
     *
     * @param userList a list of speakers/organizers
     * @return a list of average number of users attended the talks/events given/organized by the speakers/organizers
     */
    private List<Integer> getAvgNumAttendees(List<String> userList) {
        List<Integer> avgNumOfAttendee = new ArrayList<>();
        for (String userName : userList) {
            List<String> talks = userManager.getInfo(userName).get("eventList");
            int numAttendee = 0;
            for (String talk : talks) {
                numAttendee = numAttendee + eventManager.getInfo(talk).get("signups").size();
            }
            if (talks.size() == 0) {
                avgNumOfAttendee.add(0);
            } else {
                Integer avgNumAttendee = numAttendee / talks.size();
                avgNumOfAttendee.add(avgNumAttendee);
            }
        }
        return avgNumOfAttendee;
    }

    /**
     * Get the top 10 result of the user activation list.
     *
     * @param topList A user activation list.
     * @param type The type of user
     * @return A string containing the top 10 results
     */
    private String findActive(List<String> topList, String type) {
        int i = 1;
        StringBuilder newString = new StringBuilder();
        while (i < 11 && topList.size() >= i) {
            newString.append(i).append(": ").append(topList.get(i - 1)).append("  Number of talks ").append(type).append(": ").append(userManager.getInfo(topList.get(i - 1)).get("eventList").size()).append("\n");
            i++;
        }
        return newString.toString();
    }

    /**
     * Get the top 10 result of the popularity of the given type of user
     *
     * @param topList A top list for the popularity of users
     * @return A string containing the top 10 results
     */
    private String findPopular(List<String> topList) {
        int i = 1;
        StringBuilder newString = new StringBuilder();
        while (i < 11 && topList.size() >= i) {
            Integer avgNumAttendee = getAvgAttendees(topList.get(i - 1));
            newString.append(i).append(": ").append(topList.get(i - 1)).append("  Average number of users attended ").append("his/her talks: ").append(avgNumAttendee).append("\n");
            i++;
        }
        return newString.toString();
    }

    /**
     * Get the top 10 result of the popularity of all the events
     *
     * @param topList A top list for the popularity of all events
     * @return A string containing the top 10 results
     */
    private String findPopularOfAll(List<String> topList) {
        int i = 1;
        StringBuilder newString = new StringBuilder();
        while (i < 11 && topList.size() >= i) {
            newString.append(i).append(": ").append(eventManager.getInfo(topList.get(i - 1)).get("title").get(0)).append("  Number of people attended: ").append(eventManager.getInfo(topList.get(i - 1)).get("signups").size()).append("\n");
            i++;
        }
        return newString.toString();
    }
}
