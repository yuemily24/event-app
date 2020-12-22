package Builders;

import Entities.Message;
import Entities.MessageThread;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class MessageThreadBuilder {

    private String id;
    private List<String> userList;
    private List<Message> messageList;

    public MessageThreadBuilder() {
    }

    /**
     * Sets the id of a MessageThread, and returns the updated MessageThreadBuilder.
     *
     * @param id the id of the MessageThread, obtained from the database, in a list of size 1
     * @return the MessageThreadBuilder with the updated id
     */
    public MessageThreadBuilder setId(List<String> id) {
        this.id = id.get(0);
        return this;
    }

    /**
     * Sets the list of Users in the MessageThread, and returns the updated MessageThreadBuilder.
     *
     * @param userList the list of Users in the MessageThread from the database
     * @return the MessageThreadBuilder with the updated list of users
     */
    public MessageThreadBuilder setUserList(List<String> userList) {
        this.userList = userList;
        return this;
    }

    /**
     * Sets the messageList of the MessageThread, and returns the updated MessageThreadBuilder.
     *
     * @param messageInfoList the list of Messages from the database
     * @return the MessageThreadBuilder with the updated list of Messages
     * @throws ParseException if there is a parse error
     */
    public MessageThreadBuilder setMessageList(List<String> messageInfoList) throws ParseException {
        List<Message> messageList = new ArrayList<>();
        for (String messageInfo : messageInfoList) { // every messageInfo is senderName + " said at " + timeStamp + ": \n" + message + "\n"
            String[] tokens = messageInfo.split(" said at |: \n|\n"); // tokens = {username, timestamp, message, blank line}
            SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
            Date timeStamp = formatter.parse(tokens[1]);
            // create new Message entity from strings
            Message newMessage = new Message(tokens[2], tokens[0]);
            newMessage.setTimeStamp(timeStamp);
            // add Message to messageList in MessageThread
            messageList.add(newMessage);
        }
        this.messageList = messageList;
        return this;
    }

    /**
     * Builds a MessageThread, and returns it.
     *
     * @return a MessageThread with the components set in this class
     */
    public MessageThread build() {
        MessageThread newMessageThread = new MessageThread(this.userList);
        newMessageThread.setId(this.id);
        newMessageThread.setMessageList(this.messageList);
        return newMessageThread;
    }
}
