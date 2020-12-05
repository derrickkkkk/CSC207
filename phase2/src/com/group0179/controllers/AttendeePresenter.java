package com.group0179.controllers;

import com.group0179.presenters.AttendeePresenterEN;
import com.group0179.presenters.IAttendeePresenter;
import com.group0179.presenters.Presenter;
import com.group0179.use_cases.MessageManager;
import com.group0179.use_cases.RoomManager;
import com.group0179.use_cases.UserManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class AttendeePresenter extends Presenter {
    private final UserManager userMan;
    private final MessageManager msgMan;
    private final RoomManager roomMan;
    private final UUID currUserID;
    private IAttendeePresenter langPresneter= new AttendeePresenterEN();


    /**
     * Allows attendees to do attendee things.
     * @param userMan the user manager use case class
     * @param msgMan the user manager use case class
     * @param roomMan the user manager use case class
     */
    public AttendeePresenter(UserManager userMan, RoomManager roomMan, MessageManager msgMan) {
        this.userMan = userMan;
        this.msgMan = msgMan;
        this.roomMan = roomMan;
        this.currUserID = this.userMan.getCurrentUser();
    }

    /**
     * Changes the language presenter so the returned texts will be in a different language.
     * @param newPresenter The new type of language presenter.
     */
    public void changeLangPresenter(IAttendeePresenter newPresenter){
        this.langPresneter = newPresenter;
    }

    /**
     * @param username The username of the person the user wants to message
     * @param content The content of the message.
     * @return Returns a string saying the user either does not exist or message sent success.
     */
    public String message(String username, String content){
        if (!userExists(username)) {
            return langPresneter.recipientDNE();
        }
        UUID recipient = this.userMan.getUserID(username);
        this.msgMan.sendMessage(this.userMan, currUserID, recipient, content);
        return langPresneter.sentSuccess();
    }

    /**
     * Prints the messages from a specific user or all users who sent him at least one message.
     * @param input The username of the user we want to get all messages of.
     * @return Returns the messages from that user, all users, or No users found.
     */
    public String viewMessages(String input){
        ArrayList<UUID> allUserIds = new ArrayList<>(this.userMan.getAttendeeUUIDs());
        // if want all messages, get a list of messages from each user and
        // write the sender name and message contents if user received at least 1 message from them
        if (input.equals("all")){
            StringBuilder allMsgs = new StringBuilder(langPresneter.messageWord() + "\n");
            for (UUID uuid : allUserIds){
                List<String> msgsFromPerson = this.msgMan.getMessageContentsFromUser(this.userMan, currUserID, uuid);
                if (msgsFromPerson.size()!=0){
                    String senderName = this.userMan.getUsername(uuid);
                    allMsgs.append(senderName).append(": ");
                    for (String msg : msgsFromPerson) {
                        allMsgs.append(msg).append(", ");
                    }
                    allMsgs.append("\n");
                }
            }
            return allMsgs.toString();
        }
        // if user wants message from specific user
        UUID recipient = this.userMan.getUserID(input);
        if (recipient == null) {return langPresneter.userNotFound();}
        List<String> msgContent = this.msgMan.getMessageContentsFromUser(this.userMan, currUserID, recipient);
        return String.join(",", msgContent);
    }

    /**
     * @return Returns a string on all existing events and the rooms they're in.
     * If there are no events, return "No events available".
     */
    public String viewAllEvents(){
        String allEvents = roomMan.stringEventInfoAll();
        if (allEvents.equals("")){
            return langPresneter.noEventsAvailiable();
        }
        return allEvents;
    }

    /**
     * @return A string of all events the user signed up for
     */
    public String viewSignedUpEvents(){
        return roomMan.stringEventInfoAttending(currUserID);
    }

    /**
     * Takes an eventID and the corrisponding roomID, and whether the user wants to join
     * or leave the event, returns a success message string or string explaining why action could
     * not be preformed.
     * @param roomNum The room number of the event that user wants to join.
     * @param eventName The event name
     * @param joinOrLeave Whether the he wants to try 'joining' or 'leaving'. (Must be in that format
     *                    or else invalid)
     * @return Whether the action was a success.
     */
    public String joinLeaveEvent(String joinOrLeave, String roomNum, String eventName){
        // Check if joinOrLeave is formatted right
        if (!(joinOrLeave.equals("j")||joinOrLeave.equals("l"))){
            return langPresneter.joinOrLeave();
        }
        // Check if roomnNum valid and event can be found
        if (!roomNum.matches("^[0-9]+$")){
            return langPresneter.invalidRoom();}
        int intRoomNum = Integer.parseInt(roomNum);
        UUID eventID = this.roomMan.getEventUUIDfromNameandRoom(eventName, intRoomNum);
        if (eventID==null){return langPresneter.noEventsFound();}

        if (joinOrLeave.equals("j") && this.roomMan.addEventAttendee(currUserID, eventID,
                this.userMan, this.userMan.isUserVip(currUserID))){
            //this.ap.displayJoinLeaveSuccess(joinOrLeave);
            return langPresneter.joinEventSuccess();
        } else if (joinOrLeave.equals("l") && this.roomMan.removeEventAttendee(currUserID, eventID, userMan)){
            //this.ap.displayJoinLeaveSuccess(joinOrLeave);
            return langPresneter.leaveEventSuccess();
        }
        //this.ap.displayJoinLeaveError(joinOrLeave);
       return joinOrLeave.equals("j") ? langPresneter.joinEventFail() : langPresneter.leaveEventFail();
    }

    /**
     * @return a string containing user stats.
     */
    public String getUserStats(){
        return String.valueOf(userMan.getLastLoggedInForCurrentUser());
                //userMan.getTotalMinutesLoggedInForCurrentUser() +
                //Arrays.toString(userMan.getMaximumAndMinimumMinutesLoggedInForCurrentUser());
    }

    /**
     * Takes a username and returns true iff username is from an existing user
     * also prints a message telling user if username does not exist.
     * @param username the given username as a string
     * @return Whether the username is from an existing user
     */
    private boolean userExists(String username) {
        return this.userMan.getUsernames().contains(username);
    }
}