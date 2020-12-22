package Entities;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Event {
    private final String title;
    private final LocalDateTime[] time; // [0] = start, [1] = end
    private final String[] speakers; // array here is used as a safety check to prevent going over capacity, should be instantiated to max speakers
    private final String location;
    private String eventID = UUID.randomUUID().toString();
    private int capacity;
    private boolean status;
    private List<String> signups;
    private List<String> waitlist;

    public Event(String title, LocalDateTime[] time, String[] speakers, String location, int capacity, boolean status) {
        this.title = title;
        this.time = time;
        this.speakers = speakers;
        this.location = location;
        this.capacity = capacity; // has accounted for # of speakers already
        this.status = status;
        this.signups = new ArrayList<>();
        this.waitlist = new ArrayList<>();
    }

    /**
     * Adds speaker to event by replacing a placeholder (System) speaker.
     *
     * @param speakerUserName The username of the Speaker being added to the event.
     */
    public void addSpeaker(String speakerUserName) {
        for (int i = 0; i < speakers.length; i++) {
            if (speakers[i].equals("System")) {
                speakers[i] = speakerUserName;
            }
        }
    }

    /**
     * Get the title of an event.
     *
     * @return Title of the event
     */
    public String getTitle() {
        return title;
    }

    /**
     * Get the start time for the event.
     *
     * @return Entities.Event start time
     */
    public LocalDateTime getStartTime() {
        return time[0];
    }

    /**
     * Get the end time for the event.
     *
     * @return Entities.Event end time
     */
    public LocalDateTime getEndTime() {
        return time[1];
    }

    /**
     * Get the list of speakers' username.
     *
     * @return speaker' username
     */
    public String[] getSpeakers() {
        return speakers;
    }

    /**
     * Gets the location of the event.
     *
     * @return Entities.Room which the event takes place
     */
    public String getLocation() {
        return location;
    }

    /**
     * Gets the ID of the event.
     *
     * @return Entities.Event ID
     */
    public String getEventID() {
        return eventID;
    }

    /**
     * Gets ArrayList of all the attendees within the event
     *
     * @return all the attendees within the event
     */
    public List<String> getSignups() {
        return signups;
    }

    /**
     * Gets capacity (includes both speaker capacity and attendee capacity) of the event
     *
     * @return event capacity
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Gets list of all attendee usernames waitlisted for this event.
     *
     * @return list of waitlisted attendees
     */
    public List<String> getWaitlist() {
        return waitlist;
    }

    /**
     * Gets boolean for VIP status of event.
     *
     * @return True if VIP event, False if regular event
     */
    public boolean getStatus() {
        return status;
    }

    /**
     * Sets list of attendees that have signed up for this event.
     *
     * @param signups list of attendee usernames on the signup list
     */
    public void setSignups(List<String> signups) {
        this.signups = signups;
    }

    /**
     * Sets waitlist of attendees for this event.
     *
     * @param waitlist list of attendee usernames on the waitlist
     */
    public void setWaitlist(List<String> waitlist) {
        this.waitlist = waitlist;
    }

    /**
     * Sets event ID.
     *
     * @param eventID String version of the UUID
     */
    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    /**
     * Sets the capacity of the event.
     *
     * @param capacity the max capacity of the event
     */
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    /**
     * Returns a simplified string version for an event object
     *
     * @return string for an event object
     */
    @Override
    public String toString() {
        return this.title + " from " + getStartTime().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL, FormatStyle.SHORT)) +
                " to " + getEndTime().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL, FormatStyle.SHORT))
                + "; location: " + this.location;
    }

    /**
     * Add a user to the event waitlist.
     *
     * @param username attendee's username
     */
    public void addToWaitlist(String username) {
        waitlist.add(username);
    }

    /**
     * Remove a user from the event waitlist.
     *
     * @param username attendee's username
     */
    public void removeFromWaitlist(String username) {
        waitlist.remove(username);
    }

    /**
     * Add a user to the event signup list.
     *
     * @param username attendee's username
     */
    public void addToSignups(String username) {
        signups.add(username);
    }

    /**
     * Remove a user from the event singup list.
     *
     * @param username attendee's username
     */
    public void removeFromSignups(String username) {
        signups.remove(username);
    }

    /**
     * Return boolean for if this event has no speakers assigned.
     *
     * @return True if speakers are all System placeholder speakers, otherwise False
     */
    public boolean hasEmptySpeakers() {
        for (String item : speakers) {
            if (item.equals("System")) {
                return true;
            }
        }
        return false;
    }
}
