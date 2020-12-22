package UseCases;

import java.util.*;

import Builders.UserBuilder;
import Entities.User;
import org.bson.Document;

public class UserManager implements Manager {
    private final Map<String, User> userList = new HashMap<>(512);

    /**
     * Constructor for the UserManager class; takes in no objects.
     */
    public UserManager() {
    }

    /**
     * Creates a new user with the specified parameters. Returns true if it was successful, false otherwise.
     *
     * @param username    username of the Entities.User.
     * @param password    password of the Entities.User.
     * @param usertype    the type of Entities.User this Entities.User is; 0 is for attendee, 1 is for organizer, 2 is for speaker.
     * @param displayName the display name of the Entities.User.
     * @return true iff the Entities.User creation is successful
     */
    public boolean createUser(String username, String password, int usertype, String displayName) {
        if (userList.isEmpty() || !(userList.containsKey(username))) {
            User new_user = new User(username, password, usertype, displayName);
            userList.put(username, new_user);
            return true;
        }
        return false;
    }

    /**
     * Checks if the given user credentials matches an existing user's; returns an int based on the results.
     *
     * @param username username of the user
     * @param password password of the user
     * @return an int based on the different categories:
     * 0: credentials are correct, user is an Attendee
     * 1: credentials are correct, user is an Organizer
     * 2: credentials are correct, user is a Speaker
     * 3: credentials are correct, user is a VIP
     * 4: wrong username/password
     */
    public int matchUser(String username, String password) {
        if (userList.get(username) != null) {
            User temp_user = userList.get(username);
            if (!temp_user.getPassword().equals(password)) {
                return 4; // wrong password
            }
            if (temp_user.getUserType() == 0) {
                return 0; // attendee
            }
            if (temp_user.getUserType() == 1) {
                return 1; // organizer
            }
            if (temp_user.getUserType() == 2) {
                return 2; // speaker
            }
            return 3; // VIP
        }
        return 4; // wrong username
    }

    /**
     * Appends the given Entities.Event ID to the end of the specified Entities.User's list of events.
     *
     * @param username username of the specified Entities.User.
     * @param event_id ID of the Entities.Event.
     */
    public void addEvent(String username, String event_id) {
        userList.get(username).addEvent(event_id);
    }

    /**
     * Removes the given Entities.Event ID from the specified Entities.User's list of events.
     *
     * @param username username of the specified user.
     * @param event_id ID of the Entities.Event.
     */
    public void removeEvent(String username, String event_id) {
        userList.get(username).removeEvent(event_id);
    }

    /**
     * Adds the given Entities.User to a waitlist based their username to an Entities.Event based on its event_id
     *
     * @param username User's username
     * @param event_id id of the Event trying to be waitlisted for
     */
    public void addWaitlistedEvent(String username, String event_id) {
        userList.get(username).addWaitlistedEvent(event_id);
    }

    /**
     * Removes the given Entities.User from a waitlist based their username to an Entities.Event based on its event_id
     *
     * @param username User's username
     * @param event_id id of the Event trying to be waitlisted for
     */
    public void removeWaitlistedEvent(String username, String event_id) {
        userList.get(username).removeWaitlistedEvent(event_id);
    }

    /**
     * Appends the given Entities.MessageThread ID to the end of the specified Entities.User's unread chats.
     *
     * @param username  username of the specified Entities.User.
     * @param thread_id ID of the Entities.MessageThread.
     */
    public void addChat(String username, String thread_id) {
        userList.get(username).addUnread(thread_id);
    }

    /**
     * Appends the the target username (other_username) to the specified Entities.User's contact list.
     *
     * @param username       username of the Entities.User whose contact list is to be modified.
     * @param other_username username of the Entities.User to be appended to the contact list.
     */
    public void addContact(String username, String other_username) {
        userList.get(username).addContact(other_username);
    }

    /**
     * Removes the target username (otherUsername) from the specified Entities.User's contact list.
     *
     * @param username      username of the Entities.User whose contact list is to be modified.
     * @param otherUsername username of the Entities.User to be removed from the contact list.
     */
    public void removeContact(String username, String otherUsername) {
        userList.get(username).removeContact(otherUsername);
    }

    /**
     * Returns true if the input username exists, false otherwise.
     *
     * @param username username of Entities.User
     * @return true if username is in userList and false if it is not in userList as a key
     */
    public boolean existsUser(String username) {
        return userList.containsKey(username);
    }

    /**
     * Returns a List of all Attendees' usernames.
     *
     * @return List of all Attendee usernames
     */
    public List<String> getAllAttendees() {
        List<String> listOfAttendees = new ArrayList<>();
        for (Map.Entry<String, User> entry : userList.entrySet()) {
            if (entry.getValue().getUserType() == 0)
                listOfAttendees.add(entry.getKey());
        }
        return listOfAttendees;
    }

    /**
     * Returns a List of all Speakers' usernames.
     *
     * @return List of all Speaker usernames
     */
    public List<String> getAllSpeakers() {
        List<String> listOfSpeakers = new ArrayList<>();
        for (Map.Entry<String, User> entry : userList.entrySet()) {
            if (entry.getValue().getUserType() == 2)
                listOfSpeakers.add(entry.getKey());
        }
        return listOfSpeakers;
    }

    /**
     * Returns a List of all VIPAttendees' usernames.
     *
     * @return List of all VIPAttendee usernames
     */
    public List<String> getAllVIPAttendees() {
        List<String> listOfVIPAttendees = new ArrayList<>();
        for (Map.Entry<String, User> entry : userList.entrySet()) {
            if (entry.getValue().getUserType() == 3)
                listOfVIPAttendees.add(entry.getKey());
        }
        return listOfVIPAttendees;
    }

    /**
     * Returns a List of all Organizers' usernames.
     *
     * @return List of all Organizer usernames
     */
    public List<String> getAllOrganizer() { //changed into List
        List<String> listOfOrganizers = new ArrayList<>();
        for (Map.Entry<String, User> entry : userList.entrySet()) {
            if (entry.getValue().getUserType() == 1)
                listOfOrganizers.add(entry.getKey());
        }
        return listOfOrganizers;
    }

    /**
     * Sets a Entities.User's password given their username
     *
     * @param username specified Entities.User's username
     * @param password desired password to be set
     */
    public void setPassword(String username, String password) {
        userList.get(username).setPassword(password);
    }

    /**
     * Sets a Entities.User's displayName given their username
     *
     * @param username    specified Entities.User's username
     * @param displayName desired displayName to be set
     */
    public void setDisplayName(String username, String displayName) {
        userList.get(username).setDisplayName(displayName);
    }

    /**
     * Returns a Map with keys of Entities.User's userName with value List of Strings: the User's info (username, password, displayName, etc.)
     *
     * @param userName given Entities.User's username
     * @return Map with key of Entities.User's userName with values of List of Strings, of their userInfo.
     */
    @Override
    public Map<String, List<String>> getInfo(String userName) {
        Map<String, List<String>> userInfo = new HashMap<>();
        User currentUser = userList.get(userName);
        userInfo.put("username", Collections.singletonList(userName));
        userInfo.put("password", Collections.singletonList(currentUser.getPassword()));
        userInfo.put("displayName", Collections.singletonList(currentUser.getDisplayName()));
        userInfo.put("readList", currentUser.getReadList());
        userInfo.put("unreadList", currentUser.getUnreadList());
        userInfo.put("contactList", currentUser.getContactList());
        userInfo.put("eventList", currentUser.getEvents());
        userInfo.put("eventWaitlist", currentUser.getEventWaitlist());
        userInfo.put("archiveList", currentUser.getArchiveList());
        userInfo.put("userType", Collections.singletonList(Integer.toString(currentUser.getUserType())));
        userInfo.put("userToString", Collections.singletonList(currentUser.toString()));
        return userInfo;
    }

    /**
     * Returns List of Maps with String keys of userName and values of List of user's info; used for exporting to database
     *
     * @return List of Maps with String keys of userName and values of List of user's info
     */
    @Override
    public List<Map<String, List<String>>> getInfoAsList() {
        List<Map<String, List<String>>> allUserInfo = new ArrayList<>();
        for (User singleUser : userList.values()) {
            allUserInfo.add(getInfo(singleUser.getUsername()));
        }
        return allUserInfo;
    }

    /**
     * Create a new User from the imported Document from the database and add it to the UserManager.
     *
     * @param user The Document containing information about a single user.
     */
    @Override
    public void importEntity(Document user) {
        User newUser = new UserBuilder()
                .setUsername(user.getList("username", String.class))
                .setPassword(user.getList("password", String.class))
                .setDisplayName(user.getList("displayName", String.class))
                .setContactList(user.getList("contactList", String.class))
                .setEventList(user.getList("eventList", String.class))
                .setEventWaitlist(user.getList("eventWaitlist", String.class))
                .setArchiveList(user.getList("archiveList", String.class))
                .setReadList(user.getList("readList", String.class))
                .setUnreadList(user.getList("unreadList", String.class))
                .setUserType(user.getList("userType", String.class))
                .build();
        userList.put(newUser.getUsername(), newUser);
    }

    /**
     * Returns "User Manager"
     *
     * @return "User Manager"
     */
    @Override
    public String toString() {
        return "User Manager";
    }
}




