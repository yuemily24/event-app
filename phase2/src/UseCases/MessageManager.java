package UseCases;

import Builders.MessageThreadBuilder;
import Entities.Message;
import Entities.MessageThread;
import org.bson.Document;

import java.text.ParseException;
import java.util.*;

public class MessageManager implements Manager {
    public Map<String, MessageThread> messageList = new HashMap<>(512);

    /**
     * Creates a message and adds it to a MessageThread.
     *
     * @param message         The message being created.
     * @param senderUsername  The username of the message sender.
     * @param messageThreadId The id of the MessageThread in which the new message is created.
     */
    public void createMessage(String message, String senderUsername, String messageThreadId) {
        Message newMessage = new Message(message, senderUsername);
        messageList.get(messageThreadId).addMessage(newMessage);
    }

    /**
     * Creates a MessageThread with a list of users.
     *
     * @param userList The list of users that will be able to interact with the MessageThread.
     * @return The ID of the new MessageThread.
     */
    public String createMessageThread(List<String> userList) {
        MessageThread newThread = new MessageThread(userList);
        messageList.put(newThread.getId(), newThread);
        return newThread.getId();
    }

    /**
     * Deletes a MessageThread.
     *
     * @param messageThreadId The name of the MessageThread.
     */
    public void deleteMessageThread(String messageThreadId) {
        messageList.remove(messageThreadId);
    }

    /**
     * Gets a Map of the ID, User List, Message List, and String Representation of a single MessageThread.
     * Stores each field as the key and a list of Strings for the value(s) corresponding to the field.
     *
     * @param messageThreadId The Name of the MessageThread.
     * @return A Map representing a single MessageThread.
     */
    @Override
    public Map<String, List<String>> getInfo(String messageThreadId) {
        Map<String, List<String>> messageInfo = new HashMap<>();
        MessageThread currentThread = messageList.get(messageThreadId);
        messageInfo.put("id", Collections.singletonList(currentThread.getId()));
        messageInfo.put("userList", currentThread.getUserList());
        List<String> messageList = new ArrayList<>();
        for (Message message : currentThread.getMessageList()) {
            messageList.add(message.toString());
        }
        messageInfo.put("messageList", messageList);
        messageInfo.put("ThreadToString", Collections.singletonList(currentThread.toString()));
        return messageInfo;
    }

    /**
     * Gets a list of Maps, each representing a MessageThread and containing the ID, User List, Message List, and String Representation of that MessageThread.
     * This list should represent all MessageThreads existing in the program at that moment.
     *
     * @return A list of Maps, which represent a MessageThread each.
     */
    @Override
    public List<Map<String, List<String>>> getInfoAsList() {
        List<Map<String, List<String>>> allMessageThreadInfo = new ArrayList<>();
        for (MessageThread singleMessageThread : messageList.values()) {
            allMessageThreadInfo.add(getInfo(singleMessageThread.getId()));
        }
        return allMessageThreadInfo;
    }

    /**
     * Create a new MessageThread from the imported Document from the database and add it to the MessageManager.
     *
     * @param thread The Document containing information about a single MessageThread.
     */
    @Override
    public void importEntity(Document thread) throws ParseException {
        MessageThread newMessageThread = new MessageThreadBuilder()
                .setId(thread.getList("id", String.class))
                .setUserList(thread.getList("userList", String.class))
                .setMessageList(thread.getList("messageList", String.class))
                .build();
        messageList.put(newMessageThread.getId(), newMessageThread);
    }

    /**
     * Returns a String representation of the MessageManager object.
     *
     * @return the String representation of the MessageManager object.
     */
    @Override
    public String toString() {
        return "Message Manager";
    }
}
