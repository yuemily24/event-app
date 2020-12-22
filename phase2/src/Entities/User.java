package Entities;

import java.util.ArrayList;
import java.util.List;

public class User {

    private String username;
    private String password;
    private String displayName;
    private List<String> contactList;
    private List<String> eventList;
    private List<String> eventWaitlist;
    private List<String> archiveList;
    private List<String> readList;
    private List<String> unreadList;
    private int userType;

    /**
     * @param username    username of the user
     * @param password    password of the user
     * @param usertype    type of the user
     * @param displayname name displayed in UI
     */
    public User(String username, String password, int usertype, String displayname) {
        this.username = username;
        this.password = password;
        this.userType = usertype;
        this.displayName = displayname;
        this.contactList = new ArrayList<>();
        this.eventList = new ArrayList<>();
        this.eventWaitlist = new ArrayList<>();
        this.archiveList = new ArrayList<>();
        this.readList = new ArrayList<>();
        this.unreadList = new ArrayList<>();
    }

    /**
     * Get the username
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Get the password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Get the display name.
     *
     * @return the display name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Get the type of the user.
     *
     * @return the type of the user.
     */
    public int getUserType() {
        return userType;
    }

    /**
     * Get the list of events
     *
     * @return the list of events
     */
    public List<String> getEvents() {
        return eventList;
    }

    /**
     * Get list of archived messages
     *
     * @return list of archived messages (by MessageThread ID)
     */
    public List<String> getArchiveList() {
        return archiveList;
    }

    /**
     * Get list of read messages
     *
     * @return list of read messages (by MessageThread ID)
     */
    public List<String> getReadList() {
        return readList;
    }

    /**
     * Get list of unread messages
     *
     * @return list of unread messages (by MessageThread ID)
     */
    public List<String> getUnreadList() {
        return unreadList;
    }

    /**
     * Add an event id to the list of events
     *
     * @param event_id the ID of an event
     */
    public void addEvent(String event_id) {
        this.eventList.add(event_id);
    }

    /**
     * Remove an event id from the list of the events
     *
     * @param event_id the ID of an event
     */
    public void removeEvent(String event_id) {
        this.eventList.remove(event_id);
    }

    /**
     * Add a thread id to the list of unread chats
     *
     * @param thread_id ID of the message thread
     */
    public void addUnread(String thread_id) {
        this.unreadList.add(thread_id);
    }

    /**
     * Get the list of contacts
     *
     * @return the list of contacts
     */
    public List<String> getContactList() {
        return contactList;
    }

    /**
     * Add a username to the contact list
     *
     * @param username username of the person added as contact
     */
    public void addContact(String username) {
        this.contactList.add(username);
    }

    /**
     * Remove a username from the contact list
     *
     * @param username username of the person removed from contacts
     */
    public void removeContact(String username) {
        this.contactList.remove(username);
    }

    /**
     * Set a new password
     *
     * @param newPassword new password for the user account
     */
    public void setPassword(String newPassword) {
        this.password = newPassword;
    }

    /**
     * Set a new display name
     *
     * @param newDisplayname new display name for the user
     */
    public void setDisplayName(String newDisplayname) {
        this.displayName = newDisplayname;
    }

    /**
     * set a new user type
     *
     * @param newUsertype the new type of account for the user
     */
    public void setUserType(int newUsertype) {
        this.userType = newUsertype;
    }

    /**
     * set a read list
     *
     * @param newChats chats to be added to the read list
     */
    public void setReadList(List<String> newChats) {
        this.readList = newChats;
    }

    /**
     * set an unread list
     *
     * @param newChats chats to be added to the unread list
     */
    public void setUnreadList(List<String> newChats) {
        this.unreadList = newChats;
    }

    /**
     * set a contact list
     *
     * @param contactList list of contacts
     */
    public void setContactList(List<String> contactList) {
        this.contactList = contactList;
    }

    /**
     * set a list of events
     *
     * @param eventList the list of events
     */
    public void setEventList(List<String> eventList) {
        this.eventList = eventList;
    }

    /**
     * set a waitlist for an event
     *
     * @param eventWaitlist waitlist for the event
     */
    public void setEventWaitlist(List<String> eventWaitlist) {
        this.eventWaitlist = eventWaitlist;
    }

    /**
     * set a list of archived messages
     *
     * @param archiveList list of archived messages
     */
    public void setArchiveList(List<String> archiveList) {
        this.archiveList = archiveList;
    }

    /**
     * returns username as a string
     *
     * @return String representation of the display name
     */
    @Override
    public String toString() {
        return "Name: " + displayName;
    }

    /**
     * get the waitlist of an event
     *
     * @return waitlist for the event
     */
    public List<String> getEventWaitlist() {
        return this.eventWaitlist;
    }

    /**
     * adds an event to the event waitlist
     *
     * @param eventID id of the event being added
     */
    public void addWaitlistedEvent(String eventID) {
        eventWaitlist.add(eventID);
    }

    /**
     * removes an event from the event waitlist
     *
     * @param eventID id of the event being removed
     */
    public void removeWaitlistedEvent(String eventID) {
        eventWaitlist.remove(eventID);
    }

}
