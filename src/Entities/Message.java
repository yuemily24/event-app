package Entities;

import java.util.*;

public class Message {

    private final String message;
    private Date timeStamp = new Date();
    private final String senderName;

    /**
     * Create a message.
     *
     * @param message        The message being sent.
     * @param senderUsername The user that is sending the message.
     */
    public Message(String message, String senderUsername) {
        this.message = message;
        this.senderName = senderUsername;
    }

    /**
     * Get the message being sent.
     *
     * @return Message being sent.
     */
    public String getMessage() {
        return message;
    }

    /**
     * sets timestamp of a message
     * @param timeStamp timestamp of the message
     */
    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    /**
     * Returns a simplified string version for a message object.
     *
     * @return string for a message object.
     */
    @Override
    public String toString() {
        return senderName + " said at " + timeStamp + ": \n" + message + "\n";
    }
}
