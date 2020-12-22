package Controllers;

import Gateways.Gateway;
import Gateways.ScheduleHTMLGateway;
import Presenters.Presenter;
import UseCases.*;

import java.util.*;

public abstract class UserController {
    protected String currentUserName;
    protected Scanner sc = new Scanner(System.in);
    protected Gateway newGateway = new Gateway();
    protected EventManager eventManager = (EventManager) newGateway.importManager("Events");
    protected MessageManager messageManager = (MessageManager) newGateway.importManager("Messages");
    protected UserManager userManager = (UserManager) newGateway.importManager("Users");
    protected RoomManager roomManager = (RoomManager) newGateway.importManager("Rooms");
    protected Presenter presenter = new Presenter();

    abstract boolean homepage();

    /**
     * Constructor for the Controllers.UserController class.
     *
     * @param currentUserName The username of the current user.
     */
    public UserController(String currentUserName) {
        this.currentUserName = currentUserName;
    }

    /**
     * Set the UserManager instance of the UserController to the inputted UserController.
     *
     * @param userManager the UserManager instance to be used in this class
     */
    void setUserManager(UserManager userManager) {
        // we want to call this method from Controllers.LoginController to set our Users.UserManager in Controllers.UserController (aliasing)
        this.userManager = userManager;
    }

    // GLOBAL MODULES

    /**
     * Prints out all the option that a user has, then prompts the user to select a valid option. Only returns the input
     * when it's valid; otherwise the user keeps being prompted to input a valid option.
     *
     * @param options options for the users
     * @return user's choice in integer
     */
    int getInput(List<String> options, boolean home) {
        presenter.print("Please enter the number corresponding to the option you would like to select.");
        for (int i = 1; i < (options.size() + 1); i++) {
            presenter.print(i + " - " + options.get(i - 1));
        }
        if (!home) {
            presenter.print("-1" + " - " + "Return to the previous page.");
        }

        String newInput = sc.nextLine();
        String intRegex = "(-1)|[1-9]+";
        if (!home) {
            while (!newInput.matches(intRegex) || -1 > Integer.parseInt(newInput) || options.size() < Integer.parseInt(newInput)) {
                presenter.print("Please enter a valid choice.");
                newInput = sc.nextLine();
            }
        } else {
            while (!newInput.matches(intRegex) || 0 > Integer.parseInt(newInput) || options.size() < Integer.parseInt(newInput)) {
                presenter.print("Please enter a valid choice.");
                newInput = sc.nextLine();
            }
        }
        return Integer.parseInt(newInput);
    }

    /**
     * Pauses the program until the user presses Enter; used to prevent instant return to a menu after an action.
     */
    void pause() { // a pause method, so we don't kick user to main page right after printing out stuff; ignore the warning
        presenter.print("Press Enter to continue.");
        try {
            System.in.read();
        } catch (Exception error) {
            error.printStackTrace();
        }
    }

    // SYSTEM MODULES

    /**
     * Homepage for the user's own settings and info.
     */
    void homepageSettings() {
        boolean looper = true;
        do {
            presenter.print("Account Settings:");
            List<String> options = Arrays.asList("View account info", "Change account info");

            int input = getInput(options, false);
            if (input == 1) {
                viewAccountInfo();
                pause();
            } else if (input == 2) {
                changeAccountInfo();
            } else {
                presenter.print("Returning to the previous menu.");
                looper = false;
            }
        } while (looper);
    }

    /**
     * Displays the user's account info.
     */
    private void viewAccountInfo() {
        Map<String, List<String>> currentUserInfo = userManager.getInfo(currentUserName);
        String username = "Username: " + currentUserName;
        String password = "Password: " + currentUserInfo.get("password").get(0);
        String displayName = "Display name:" + currentUserInfo.get("displayName").get(0);
        int currentUserType = Integer.parseInt(currentUserInfo.get("userType").get(0));
        String userTypeString;

        if (currentUserType == 0) {
            userTypeString = "Attendee";
        } else if (currentUserType == 1) {
            userTypeString = "Organizer";
        } else if (currentUserType == 2) {
            userTypeString = "Speaker";
        } else {
            userTypeString = "VIP Attendee";
        }
        String userType = "Account type: " + userTypeString;

        presenter.print(username);
        presenter.print(password);
        presenter.print(displayName);
        presenter.print(userType);
    }

    /**
     * Homepage for changing account info.
     */
    private void changeAccountInfo() { // no loop here, we assume user won't want to change both at once
        List<String> options = Arrays.asList("Change display name", "Change password"); //
        int input = getInput(options, false);
        if (input == 1) {
            infoChangeUserProperty("display name");
            pause();
        } else if (input == 2) {
            infoChangeUserProperty("password");
            pause();
        } else {
            presenter.print("-1  - Return to the previous page.");
        }
    }

    /**
     * Method for user to search through their chat history.
     */
    void searchChatHistory() {
        presenter.print("Please enter the key word or phrase:");
        String keyword = sc.nextLine();
        Map<String, List<String>> result = searchHistoryKeyword(keyword);

        boolean looper = true;
        List<String> list = new ArrayList<>(result.keySet());
        List<String> nonEmptyList = new ArrayList<>();
        for (String username : list) {
            if (result.get(username).size() > 0) {
                nonEmptyList.add(username);
            }
        }
        List<String> historyInfo = new ArrayList<>();
        for (String username : nonEmptyList) {
            int numHistory = result.get(username).size();
            historyInfo.add(numHistory + " related messages with " + userManager.getInfo(username).get("displayName"));
        }
        List<String> options = new ArrayList<>(historyInfo); //changed to List
        if (options.size() == 0) {
            presenter.print("Looks like there is no message containing the key word you just entered!");
        } else {
            presenter.print("Select the message thread you want to view");
            do {
                int input = getInput(options, false);
                for (int i = 1; i <= nonEmptyList.size(); i++) {
                    if (input == i) {
                        searchInOne(keyword, nonEmptyList.get(i - 1));
                        pause();
                    }
                }
                if (input == -1) {
                    presenter.print("Returning to the previous menu.");
                    looper = false;
                }
            } while (looper);
        }
    }

    /**
     * Changes user property; either display name or password depending on input.
     *
     * @param type parameter to decide whether to change password or display name
     */
    private void infoChangeUserProperty(String type) {
        presenter.print("Enter your new " + type + ".");
        String newProperty = sc.nextLine();
        if (type.equals("password")) {
            userManager.setPassword(currentUserName, newProperty);
            presenter.print("Your password has been changed. It is now " + newProperty);
        } else {
            userManager.setDisplayName(currentUserName, newProperty);
            presenter.print("Your display name has been changed. It is now " + newProperty);
        }
    }

    /**
     * Logs out the user, and exports the data in the Managers to the database.
     */
    void logout() {
        newGateway.exportManager(eventManager);
        newGateway.exportManager(messageManager);
        newGateway.exportManager(roomManager);
    }

    // MESSAGE MODULES

    /**
     * Allows the user to manage messages across different message lists.
     *
     * @param listType the type of message list being accessed; "readList", "unreadList", or "archiveList"
     */
    void messageManage(String listType) {
        Map<String, List<String>> currentUserInfo = userManager.getInfo(currentUserName);
        List<String> chatList = userManager.getInfo(currentUserName).get(listType);
        String type = listType.equals("readList") ? "read" : listType.equals("unreadList") ? "unread" : "archived";
        if (chatList.isEmpty()) {
            presenter.print("Looks like you have no " + type + " messages!");
        } else {
            presenter.print("Your " + type + "message threads:");
        }
        int userSelectedChat = messageManageHelper(chatList);

        // view message; with other actions
        if (userSelectedChat != -1) {

            List<String> options = listType.equals("unreadList")
                    ? Arrays.asList("Send a message to this thread", "Archive this thread", "Delete this message thread") // unread
                    : listType.equals("readList")
                    ? Arrays.asList("Send a message to this thread", "Archive this thread", "Delete this message thread", "Mark this thread as unread") // read
                    : Arrays.asList("Unarchive this message thread", "Delete this message thread"); // archived
            presenter.print(messageManager.getInfo(chatList.get(userSelectedChat - 1)).get("ThreadToString").get(0));
            int userSelectedOption = getInput(options, false);
            String currentThreadID = messageManager.getInfo(chatList.get(userSelectedChat - 1)).get("id").get(0);

            // check if a message was deleted; used for deciding whether to move a thread to read list from unread
            boolean deleted = false;

            // send message; only applicable to read/unread
            if ((listType.equals("unreadList") || listType.equals("readList")) && userSelectedOption == 1) {
                String msg = messageSelectPrompt();
                messageManager.createMessage(msg, currentUserName, currentThreadID);
                presenter.print("Success - message sent!");
            }

            // archive message; applicable to read/unread only
            if ((listType.equals("unreadList") || listType.equals("readList")) && userSelectedOption == 2) {
                currentUserInfo.get(listType).remove(currentThreadID);
                currentUserInfo.get("archiveList").add(currentThreadID);
                presenter.print("Success - message thread archived!");
            }

            // delete message; applicable to read/unread only
            if (
                    ((listType.equals("unreadList") || listType.equals("readList")) && userSelectedOption == 3) || // read/unread
                            (listType.equals("archiveList") && userSelectedOption == 2) // archive
            ) {
                messageManager.getInfo(currentThreadID).get("userList").remove(currentUserName);
                if (messageManager.getInfo(currentThreadID).get("userList").isEmpty()) {
                    currentUserInfo.get(listType).remove(currentThreadID);
                    messageManager.deleteMessageThread(currentThreadID);
                } else {
                    currentUserInfo.get(listType).remove(currentThreadID);
                }
                deleted = true;
                presenter.print("Success - message thread deleted!");
            }

            // move to unread; read list only
            if (listType.equals("readList") && userSelectedOption == 4) {
                currentUserInfo.get("readList").remove(currentThreadID);
                currentUserInfo.get("unreadList").add(currentThreadID);
                presenter.print("Success - message has been marked as unread!");
            }

            // unarchive message, archive list only
            else if (listType.equals("archiveList") && userSelectedOption == 1) {
                currentUserInfo.get("readList").add(currentThreadID);
                currentUserInfo.get("archiveList").remove(currentThreadID);
                presenter.print("Success - message thread unarchived!");
            }

            // moves thread to read if this was in unread list
            if (listType.equals("unreadList") && !deleted) {
                currentUserInfo.get("unreadList").remove(currentThreadID);
                currentUserInfo.get("readList").add(currentThreadID);
            }
        }
    }

    /**
     * Helper method for messageManage. Returns the index of the input messageList selected by the user.
     *
     * @param messageList list of MessageThreads to be selected by the user
     * @return the integer selected by the user
     */
    private int messageManageHelper(List<String> messageList) {
        List<String> chats = new ArrayList<>();
        for (String chat : messageList) {
            Map<String, List<String>> currentThreadInfo = messageManager.getInfo(chat);
            List<String> listOfUsers = currentThreadInfo.get("userList");
            chats.add(String.join(", ", listOfUsers));
        }

        return getInput(chats, false);
    }

    /**
     * Prompts the user to enter a message they would like to send.
     *
     * @return a message the user wants to send
     */
    String messageSelectPrompt() {
        presenter.print("Enter the message you want to send:");
        return sc.nextLine();
    }

    /**
     * Sends a message from user matching senderUsername to the user matching receiverUsername
     *
     * @param senderUsername   sender's username
     * @param receiverUsername receiver's username
     * @param message          message that is sent
     */
    void sendSingleMessage(String senderUsername, String receiverUsername, String message) {
        if (userManager.existsUser(receiverUsername) && !senderUsername.equals(receiverUsername)) {
            Map<String, List<String>> senderInfo = userManager.getInfo(senderUsername);
            int senderUserType = Integer.parseInt(senderInfo.get("userType").get(0));
            Map<String, List<String>> receiverInfo = userManager.getInfo(receiverUsername);
            int receiverUserType = Integer.parseInt(receiverInfo.get("userType").get(0));
            if (
                // If the sender is an attendee (VIP or regular), either the receiver has to be a speaker or an
                // attendee (VIP or regular) that sender has in its contactList and receiver also has to have the
                // sender in its contactList.
                    ((senderUserType == 0 || senderUserType == 3) &&
                            (((receiverUserType == 0 || receiverUserType == 3) &&
                                    senderInfo.get("contactList").contains(receiverUsername)) && receiverInfo.get("contactList").contains(senderUsername)) ||
                            receiverUserType == 2) ||
                            // If the sender is an organizer, send that message to whoever without checking for contactList.
                            (senderUserType == 1) ||
                            // If the sender is a speaker, the receiver has to be an Attendee or VIPAttendee. No need to check for contactList here.
                            (senderUserType == 2 && (receiverUserType == 0 || receiverUserType == 3))) {
                boolean messageThreadBetweenTwoPeopleExists = false;
                //if the messageThread between two people already exists
                List<String> receiverMessageThreads = new ArrayList<>();
                receiverMessageThreads.addAll(receiverInfo.get("readList"));
                receiverMessageThreads.addAll(receiverInfo.get("unreadList"));
                List<String> senderMessageThreads = new ArrayList<>();
                senderMessageThreads.addAll(senderInfo.get("readList"));
                senderMessageThreads.addAll(senderInfo.get("unreadList"));
                for (String messageThreadID : senderMessageThreads) {
                    if (receiverMessageThreads.contains(messageThreadID)) {
                        //append to existing messageThread
                        messageManager.createMessage(message, senderUsername, messageThreadID);
                        messageThreadBetweenTwoPeopleExists = true;
                        break;
                    }
                }
                //otherwise create new messageThread and append message to the thread
                if (!messageThreadBetweenTwoPeopleExists) {
                    String newThreadID = messageManager.createMessageThread(new ArrayList<>(Arrays.asList(senderUsername, receiverUsername)));
                    messageManager.createMessage(message, senderUsername, newThreadID);
                    userManager.addChat(senderUsername, newThreadID);
                    userManager.addChat(receiverUsername, newThreadID);
                }
                presenter.print("Success - message sent!");
            } else {
                presenter.print("Failure - message not sent.");
            }
        } else {
            presenter.print("Failure - message not sent.");
        }
    }

    /**
     * Sends the inputted user a message, with the sender being "System".
     *
     * @param receiver the username of the user to be sent a message
     * @param message  the message
     */
    void sendSystemMessage(String receiver, String message) {
        String newThreadID = messageManager.createMessageThread(new ArrayList<>(Arrays.asList("System", receiver)));
        messageManager.createMessage(message, "System", newThreadID);
        userManager.addChat(receiver, newThreadID);
    }

    //export to HTML file

    /**
     * Creates an HTML file containing the schedule of all events.
     *
     * @param eventManager the eventManager being used
     */
    void createHTMLEvents(EventManager eventManager) {
        ScheduleHTMLGateway scheduleHTMLExporter = new ScheduleHTMLGateway(eventManager);
        scheduleHTMLExporter.createHTML();
        presenter.print("Finished exporting. Please check Conference_schedule.html.");
    }

    /**
     * Searches through the user's MessageThreads for the given keyword, and displays the MessageThreads with the keyword,
     * with number of matching messages for each thread, and lets the user to choose which message thread to view.
     *
     * @param keyword the keyword to search for
     * @return a Map with usernames as keys, and lists of messages containing the specified keyword from the
     * corresponding user as values.
     */
    Map<String, List<String>> searchHistoryKeyword(String keyword) {
        List<String> chatHistory = new ArrayList<>();
        chatHistory.addAll(userManager.getInfo(currentUserName).get("readList"));
        chatHistory.addAll(userManager.getInfo(currentUserName).get("unreadList"));
        chatHistory.addAll(userManager.getInfo(currentUserName).get("archiveList"));
        Map<String, List<String>> searchResult = new HashMap<>();
        for (String chat : chatHistory) {
            List<String> messageContainingKeyword = new ArrayList<>();
            List<String> receiverName = new ArrayList<>();
            Map<String, List<String>> messageInfo = messageManager.getInfo(chat);
            List<String> messageList = messageInfo.get("messageList");
            for (String user : messageInfo.get("userList")) {
                if (!user.equals(currentUserName)) {
                    receiverName.add(user);
                }
            }
            for (String message : messageList) {
                String[] splitMessage = message.split("\n");
                String mess = Arrays.asList(splitMessage).get(1);
                if (mess.contains(keyword)) {
                    messageContainingKeyword.add(message);
                }
            }
            searchResult.put(receiverName.get(0), messageContainingKeyword);
        }
        return searchResult;
    }

    /**
     * Searches for the specified keyword said from the specified user.
     *
     * @param keyword  the keyword to search for
     * @param username the username to search for
     */
    private void searchInOne(String keyword, String username) {
        List<String> result = searchHistoryKeyword(keyword).get(username);
        presenter.print(result);
    }

}
