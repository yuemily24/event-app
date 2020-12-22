package Builders;

import Entities.Event;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class EventBuilder {

    private String title;
    private LocalDateTime[] time; // [0] = start, [1] = end
    private String[] speakers;
    private String location;
    private String eventID;
    private int capacity;
    private boolean status;
    private List<String> signups;
    private List<String> waitlist;

    public EventBuilder() {
    }

    /**
     * Sets the title of the Event to be returned, and returns the updated EventBuilder.
     *
     * @param title title of the Event in a list of size 1, obtained from the database
     * @return the EventBuilder with updated title
     */
    public EventBuilder setTitle(List<String> title) {
        this.title = title.get(0);
        return this;
    }

    public EventBuilder  setTime(List<String> times) {
        LocalDateTime[] eventTimes = new LocalDateTime[2];
        int i = 0;
        for (String time : times) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime dateTime = LocalDateTime.parse(time, formatter);
            eventTimes[i] = dateTime;
            i++;
        }
        this.time = eventTimes;
        return this;
    }

    /**
     * Sets the speakers of the Event to be returned, and returns the updated EventBuilder.
     *
     * @param speakers list of Speakers of the Event, obtained from the database
     * @return the EventBuilder with updated speakers
     */
    public EventBuilder setSpeakers(List<String> speakers) {
        this.speakers = speakers.toArray(new String[0]);
        return this;
    }

    /**
     * Sets the location of the Event to be returned, and returns the updated EventBuilder.
     *
     * @param location location of the Event in a list, obtained from the database
     * @return the EventBuilder with updated location
     */
    public EventBuilder setLocation(List<String> location) {
        this.location = location.get(0);
        return this;
    }

    /**
     * Sets the eventID of the Event to be returned, and returns the updated EventBuilder.
     *
     * @param eventID eventID of the Event in a list of size 1, obtained from the database
     * @return the EventBuilder with updated eventID
     */
    public EventBuilder setEventID(List<String> eventID) {
        this.eventID = eventID.get(0);
        return this;
    }

    /**
     * Sets the capacity of the Event to be returned, and returns the updated EventBuilder.
     *
     * @param capacity capacity of the Event in a list of size 1, obtained from the database
     * @return the EventBuilder with updated capacity
     */
    public EventBuilder setCapacity(List<String> capacity) {
        this.capacity = Integer.parseInt(capacity.get(0));
        return this;
    }

    /**
     * Sets the status of the Event to be returned, and returns the updated EventBuilder.
     *
     * @param status status of the Event in a list of size 1, obtained from the database
     * @return the EventBuilder with updated status
     */
    public EventBuilder setStatus(List<String> status) {
        this.status = Boolean.parseBoolean(status.get(0));
        return this;
    }

    /**
     * Sets the list of signups of the Event to be returned, and returns the updated EventBuilder.
     *
     * @param signups list of sign ups of the Event, obtained from the database
     * @return the EventBuilder with updated list of signups
     */
    public EventBuilder setSignups(List<String> signups) {
        this.signups = signups;
        return this;
    }

    /**
     * Sets the waitlist of the Event to be returned, and returns the updated EventBuilder.
     *
     * @param waitlist waitlist of the Event, obtained from the database
     * @return the EventBuilder with updated waitlist
     */
    public EventBuilder setWaitlist(List<String> waitlist) {
        this.waitlist = waitlist;
        return this;
    }

    /**
     * Builds the Event from the components of the EventBuilder, and returns it.
     *
     * @return a Event with the parameters copied from this EventBuilder
     */
    public Event build() {
        Event newEvent = new Event(this.title, this.time, this.speakers, this.location, this.capacity, this.status);
        newEvent.setEventID(this.eventID);
        newEvent.setSignups(this.signups);
        newEvent.setWaitlist(this.waitlist);
        return newEvent;
    }
}
