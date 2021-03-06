package com.group0179.use_cases;

import com.group0179.entities.Message;

import java.io.Serializable;
import java.util.*;

/**
 * Manages message entities.
 * @author Zachariah Vincze
 */
public class MessageManager implements Serializable {
    private final Map<UUID, Message> messages;

    /**
     * Creates a new MessageManager.
     */
    public MessageManager() {
        this.messages = new HashMap<>();
    }

    /**
     * Sends a single message from a user to another user.
     * @param userManager the UserManager where the users are stored.
     * @param senderID the UUID of the message sender.
     * @param recipientID the UUID of the message receiver.
     * @param messageContent the string content of the message.
     * @return Return the id of the sent message. If the message was not sent, return null.
     */
    public UUID sendMessage(UserManager userManager, UUID senderID, UUID recipientID, String messageContent) {
        if (!userManager.userExists(senderID) || !userManager.userExists(recipientID)) {
            return null;
        }
        Message message = new Message(messageContent);
        messages.put(message.getMessageID(), message);
        userManager.addMessage(recipientID, senderID, message.getMessageID());
        return message.getMessageID();
    }

    /**
     * Sends messages from a single user to a list of recipients.
     * @param userManager the UserManager where the users are stored.
     * @param messageContent the string content of the message.
     * @param recipientIDs a list of users that are receiving the message.
     * @return Return the id of the sent message. If the message was not sent, return null.
     *
     * TODO: Review UM fixes made
     */
    private UUID sendMessages(UserManager userManager, UUID senderID, List<UUID> recipientIDs, String messageContent) {
        if (!userManager.userExists(senderID) || recipientIDs.isEmpty() || !userManager.usersExist(recipientIDs)) {
            return null;
        }
        Message message = new Message(messageContent);
        messages.put(message.getMessageID(), message);
        for (UUID id : recipientIDs) {
            userManager.addMessage(id, senderID, message.getMessageID());
        }
        return message.getMessageID();
    }

    /**
     * Sends a message from an organizer to all Attendees in the system.
     *
     * Precondition: the senderID must belong to an Organizer.
     *
     * @param userManager the UserManager where the users are stored.
     * @param senderID the ID of the message sender.
     * @param messageContent the string content of the message.
     * @return Return the id of the sent message. If the message was not sent, return null.
     */
    public UUID sendMessageToAllAttendees(UserManager userManager, UUID senderID, String messageContent) {
        return sendMessages(userManager, senderID, userManager.getAttendeeUUIDs(), messageContent);
    }

    /**
     * Sends a message from an organizer to all Speakers in the system.
     *
     * Precondition: the senderID must belong to an Organizer.
     *
     * @param userManager the UserManager where the users are stored.
     * @param senderID the ID of the message sender.
     * @param messageContent the string content of the message.
     * @return the UUID of the sent message, or null if the message was not sent.
     */
    public UUID sendMessageToAllSpeakers(UserManager userManager, UUID senderID, String messageContent) {
        return sendMessages(userManager, senderID, userManager.getSpeakerUUIDs(), messageContent);
    }

    /**
     * Return a list of messages that were sent by a specific user to
     * the current user. If no messages were sent to this user from senderID,
     * then an empty list is returned.
     *
     * @param userManager the UserManager where the users are stored.
     * @param recipientID the UUID of the user who has received these messages.
     * @param senderID the UUID of the user who sent this message.
     * @return a list of message entities from another user.
     *
     * TODO: Check userManager related error fix
     */
    private ArrayList<Message> getMessagesFromUser(UserManager userManager, UUID recipientID, UUID senderID) {
        ArrayList<Message> messageContents = new ArrayList<>();
        List<UUID> messageIDs = userManager.getMessagesFromUser(recipientID, senderID);
        for (UUID id : messageIDs) {
            messageContents.add(messages.get(id));
        }
        return messageContents;
    }

    /**
     * Return a list of messages that were sent by a specific user to
     * the current user. If no messages were sent to this user from senderID,
     * then an empty list is returned.
     *
     * @param userManager the UserManager where the users are stored.
     * @param recipientID the UUID of the user who has received these messages.
     * @param senderID the UUID of the user who sent this message.
     * @return a list of message entities from another user.
     */
    public List<String> getMessageContentsFromUser(UserManager userManager, UUID recipientID, UUID senderID) {
        ArrayList<String> messageContents = new ArrayList<>();
        for (UUID id : userManager.getMessagesFromUser(recipientID, senderID)) {
            messageContents.add(messages.get(id).getMessageContent());
        }
        return messageContents;
    }

    /**
     * Sends a single message from a speaker to attendees of an event.
     *
     * Precondition: senderID is the UUID of a speaker only.
     *
     * @param userManager the UserManager where the users are stored.
     * @param roomManager the RoomManager where the events and rooms are stored.
     * @param senderID the UUID of the sender of this message.
     * @param eventID the UUID of the event that the users are in.
     * @param messageContent the content of the message to send.
     * @return Return the id of the sent message. If the message was not sent, return null.
     */
    public UUID sendMessageToEventAttendees(UserManager userManager, RoomManager roomManager,
                                            UUID senderID, UUID eventID, String messageContent) {
        ArrayList<UUID> attendeeIDs = roomManager.getEventAttendeeIDs(eventID);
        return sendMessages(userManager, senderID, attendeeIDs, messageContent);
    }
}
