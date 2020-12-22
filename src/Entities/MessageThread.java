package Entities;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MessageThread {

    private String id = UUID.randomUUID().toString();
    private final List<String> userList = new ArrayList<>();
    private List<Message> messageList = new ArrayList<>();

    /**
     * Constructor for the MessageThread class; adds list of users into it's own userList.
     *
     * @param userList List of users (by username) to be added to this MessageThread's userList.
     */
    public MessageThread(List<String> userList) {
        this.userList.addAll(userList);
    }

    /**
     * Gets the ID of a MessageThread.
     *
     * @return This MessageThread's ID.
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the list of users that can interact with a MessageThread.
     *
     * @return The userList of the MessageThread.
     */
    public List<String> getUserList() {
        return userList;
    }

    /**
     * Gets the list of messages of a MessageThread.
     *
     * @return The MessageThread's list of messages.
     */
    public List<Message> getMessageList() {
        return messageList;
    }

    /**
     * Adds a message to the messageList of a MessageThread.
     *
     * @param reply The message to be added to this MessageThread's messageList.
     */
    public void addMessage(Message reply) {
        messageList.add(reply);
    }


    /**
     * Sets messageThread's id to id
     *
     * @param id id of a message thread
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Sets a List of Messages
     *
     * @param messageList a List of Messages
     */
    public void setMessageList(List<Message> messageList) {
        this.messageList = messageList;
    }

    /**
     * Returns MessageThread in the format of "UserName said at Time: \n Message" for each message.
     *
     * @return the resulting string for the message thread
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Message msg : this.messageList) {
            sb.append(msg.toString());
            sb.append("\n");
        }
        return sb.toString();
    }
}
