package UseCases;

import Builders.EventBuilder;
import Entities.Event;
import org.bson.Document;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class EventManager implements Manager {
    private final Map<String, Event> eventList;

    /**
     * Constructor for the Entities.EventManager class; creates a new HashMap to keep track of events and rooms.
     */
    public EventManager() {
        this.eventList = new HashMap<>();
    }

    /**
     * Creates a new event and puts it in the eventList; includes various attributes of the event.
     *
     * @param title    The title of the event.
     * @param time     The time that the event will take place.
     * @param speakers The name of the speaker featured in the event.
     * @param location The room that the event will be held in.
     * @return ID for the event.
     */
    public String createNewEvent(String title, LocalDateTime[] time, String[] speakers, String location, int capacity, boolean status) { // Organizer Only
        Event newEvent = new Event(title, time, speakers, location, capacity, status);
        eventList.put(newEvent.getEventID(), newEvent); // Put it in eventList
        return newEvent.getEventID();
    }

    /**
     * Adds a user to an event permitted that there is still space. If event capacity is full, add user to waitlist.
     * Returns true if user was added to signups, false if added to waitlist.
     *
     * @param userID  The ID of the user being added.
     * @param eventId The id of the event that the user is being added to.
     * @return whether the user was added to the signup list or the waitlist; true for signups, false for waitlist
     */
    public boolean addUserToEvent(String userID, String eventId) {
        Event specificEvent = eventList.get(eventId);
        if (specificEvent.getSignups().size() < specificEvent.getCapacity()) {
            specificEvent.addToSignups(userID);
            return true;
        } else {
            specificEvent.addToWaitlist(userID);
            return false;
        }
    }

    /**
     * Removes the inputted user from an event's signup list. If the waitlist is non-empty,
     * remove the first user in the waitlist from the wait list.
     * <p>
     * Returns the username of the user removed from the waitlist, if there was one. Otherwise, returns "System".
     *
     * @param username The username of the user being removed.
     * @param eventId  The id of the event that the user is being removed from.
     */
    public String removeUserFromEvent(String username, String eventId) {
        Event event = eventList.get(eventId);
        event.removeFromSignups(username);
        event.removeFromWaitlist(username);
        List<String> eventWaitlist = event.getWaitlist();
        if (!eventWaitlist.isEmpty()) {
            String nextUser = eventWaitlist.get(0);
            event.removeFromWaitlist(nextUser);
            return nextUser;
        }
        return "System"; // indicates that there are no waitlists, since system is not allowed as a username
    }

    /**
     * Returns list of all events by their IDs.
     *
     * @return List of all events listed by their IDs.
     */
    public List<String> getAllEvents() {
        List<String> allEventList = new ArrayList<>();
        for (Event val : eventList.values()) {
            allEventList.add(val.getEventID());
        }
        return allEventList;
    }

    /**
     * Removes an event from the eventList.
     *
     * @param eventID The ID of the event to be removed.
     */
    public void removeEvent(String eventID) {
        eventList.remove(eventID);
    }

    /**
     * Add a speaker to the list of speakers for an event.
     *
     * @param eventID  The id of the event that the speaker is being added to.
     * @param username The speaker's username.
     */
    public void addEventSpeaker(String eventID, String username) {
        eventList.get(eventID).addSpeaker(username);
    }

    /**
     * Returns a boolean indicating whether an event has capacity for more speakers to be assigned.
     *
     * @param eventID The id of the event.
     * @return true if event has unfilled speakers, false if speaker capacity is full
     */
    public boolean hasUnfilledSpeakers(String eventID) {
        return eventList.get(eventID).hasEmptySpeakers();
    }

    /**
     * Gets a Map of all information for an event. Stores each field as the key and a list of Strings for the
     * value(s) corresponding to the field.
     *
     * @param eventID The id for the event.
     * @return A Map representing an Entities.Event.
     */
    @Override
    public Map<String, List<String>> getInfo(String eventID) {
        Map<String, List<String>> eventInfo = new HashMap<>();
        Event currentEvent = eventList.get(eventID);
        eventInfo.put("title", Collections.singletonList(currentEvent.getTitle()));
        // create formatted string out of LocalDateTime objects
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String startTime = currentEvent.getStartTime().format(formatter);
        String endTime = currentEvent.getEndTime().format(formatter);
        eventInfo.put("time", Arrays.asList(startTime, endTime));
        eventInfo.put("speakers", Arrays.asList(currentEvent.getSpeakers()));
        eventInfo.put("location", Collections.singletonList(currentEvent.getLocation()));
        eventInfo.put("eventID", Collections.singletonList(currentEvent.getEventID()));
        eventInfo.put("signups", currentEvent.getSignups());
        eventInfo.put("waitlist", currentEvent.getWaitlist());
        eventInfo.put("capacity", Collections.singletonList(Integer.toString(currentEvent.getCapacity())));
        eventInfo.put("status", Collections.singletonList(Boolean.toString(currentEvent.getStatus())));
        eventInfo.put("eventToString", Collections.singletonList(currentEvent.toString()));
        return eventInfo;
    }

    /**
     * Gets a list of HashMaps, each representing an event and containing relevant information of that event.
     * This list represents all events existing in the program at the moment.
     *
     * @return A list of HashMaps, which represent an event each.
     */
    @Override
    public List<Map<String, List<String>>> getInfoAsList() {
        List<Map<String, List<String>>> allEventInfo = new ArrayList<>();
        for (Event singleEvent : eventList.values()) {
            allEventInfo.add(getInfo(String.valueOf(singleEvent.getEventID())));
        }
        return allEventInfo;
    }

    /**
     * Create a new Event from the imported Document from the database and add it to the EventManager.
     *
     * @param event The Document containing information about a single event.
     */
    @Override
    public void importEntity(Document event) {
        Event newEvent = new EventBuilder()
                .setTitle(event.getList("title", String.class))
                .setTime(event.getList("time", String.class))
                .setSpeakers(event.getList("speakers", String.class))
                .setLocation(event.getList("location", String.class))
                .setEventID(event.getList("eventID", String.class))
                .setCapacity(event.getList("capacity", String.class))
                .setStatus(event.getList("status", String.class))
                .setSignups(event.getList("signups", String.class))
                .setWaitlist(event.getList("waitlist", String.class))
                .build();
        eventList.put(newEvent.getEventID(), newEvent);
    }

    /**
     * Returns a simplified string version for an EventManager object.
     *
     * @return String representation of EventManager object.
     */
    @Override
    public String toString() {
        return "Event Manager";
    }

}

