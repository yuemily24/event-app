package Builders;

import Entities.User;

import java.util.ArrayList;
import java.util.List;

public class UserBuilder {

    private String username;
    private String password;
    private String displayName;
    private List<String> readList;
    private List<String> unreadList;
    private List<String> contactList;
    private List<String> eventList;
    private List<String> eventWaitlist;
    private List<String> archiveList;
    private int userType;

    public UserBuilder() {
    }

    /**
     * Sets the username of the User to be returned, and returns the updated UserBuilder.
     *
     * @param username username of the User in a list of size 1, obtained from the database
     * @return the UserBuilder with updated username
     */
    public UserBuilder setUsername(List<String> username) {
        this.username = username.get(0);
        return this;
    }

    /**
     * Sets the password of the User to be returned, and returns the updated UserBuilder.
     *
     * @param password password of the User in a list of size 1, obtained from the database
     * @return the UserBuilder with updated password
     */
    public UserBuilder setPassword(List<String> password) {
        this.password = password.get(0);
        return this;
    }

    /**
     * Sets the display name of the User to be returned, and returns the updated UserBuilder.
     *
     * @param displayName display name of the User in a list of size 1, obtained from the database
     * @return the UserBuilder with updated display name
     */
    public UserBuilder setDisplayName(List<String> displayName) {
        this.displayName = displayName.get(0);
        return this;
    }

    /**
     * Sets the read MessageThread IDs of the User to be returned, and returns the updated UserBuilder.
     *
     * @param chats read MessageThread IDs of the User in a list, obtained from the database
     * @return the UserBuilder with updated read MessageThread IDs
     */
    public UserBuilder setReadList(List<String> chats) {
        this.readList = chats;
        return this;
    }

    /**
     * Sets the unread MessageThread IDs of the User to be returned, and returns the updated UserBuilder.
     *
     * @param chats unread MessageThread IDs of the User in a list, obtained from the database
     * @return the UserBuilder with updated unread MessageThread IDs
     */
    public UserBuilder setUnreadList(List<String> chats) {
        this.unreadList = chats;
        return this;
    }

    /**
     * Sets the contact list of the User to be returned, and returns the updated UserBuilder.
     *
     * @param contactList contact list of the User, obtained from the database
     * @return the UserBuilder with updated contact list
     */
    public UserBuilder setContactList(List<String> contactList) {
        this.contactList = contactList;
        return this;
    }

    /**
     * Sets the list of Events the User is in, and returns the updated UserBuilder.
     *
     * @param eventList list of Events of the User, obtained from the database
     * @return the UserBuilder with updated list of Events that the User is in
     */
    public UserBuilder setEventList(List<String> eventList) { // User stores eventList as List<Integer>
        this.eventList = new ArrayList<>(eventList);
        return this;
    }

    /**
     * Sets the list of Events the User is waitlisted for, and returns the updated UserBuilder.
     *
     * @param eventWaitlist list of Events the User is waitlisted for, obtained from the database
     * @return the UserBuilder with updated list of waitlisted Events
     */
    public UserBuilder setEventWaitlist(List<String> eventWaitlist) { // User stores eventWaitlist as List<Integer>
        this.eventWaitlist = new ArrayList<>(eventWaitlist);
        return this;
    }

    /**
     * Sets the list of archived MessageThread IDs of the User to be returned, and returns the updated UserBuilder.
     *
     * @param archiveList list of archived MessageThread IDs of the User, obtained from the database
     * @return the UserBuilder with updated list of archived MessageThread IDs
     */
    public UserBuilder setArchiveList(List<String> archiveList) {
        this.archiveList = archiveList;
        return this;
    }

    /**
     * Sets the user type of the User to be returned, and returns the updated UserBuilder.
     *
     * @param userType user type of the User in a list of size 1, obtained from the database
     * @return the UserBuilder with updated user type
     */
    public UserBuilder setUserType(List<String> userType) {
        this.userType = Integer.parseInt(userType.get(0));
        return this;
    }

    /**
     * Builds the user from the components of the UserBuilder, and returns it.
     *
     * @return a User with the parameters copied from this UserBuilder
     */
    public User build() {
        User newUser = new User(username, password, userType, displayName);
        newUser.setReadList(this.readList);
        newUser.setUnreadList(this.unreadList);
        newUser.setContactList(this.contactList);
        newUser.setEventList(this.eventList);
        newUser.setEventWaitlist(this.eventWaitlist);
        newUser.setArchiveList(this.archiveList);
        return newUser;
    }

}
