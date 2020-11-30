package com.group0179.presenters;

import com.group0179.use_cases.MessageManager;
import com.group0179.use_cases.RoomManager;
import com.group0179.use_cases.UserManager;

import java.util.ArrayList;

public class OrganizerPresenter extends Presenter {
    private final UserManager um;
    private final RoomManager rm;
    private final MessageManager mm;

    public OrganizerPresenter(UserManager um, RoomManager rm, MessageManager mm) {
        this.um = um;
        this.rm = rm;
        this.mm = mm;
    }

    public ArrayList<String> getSpeakerNames() {
        return um.getSpeakerNames();
    }

    public ArrayList<String> getRoomListArray() {
        ArrayList<String> rooms = new ArrayList<>();
        for (int n = 1; n <= rm.getNumRooms(); n++) {
            rooms.add("Room " + n + " (Capacity: " + rm.getRoomCapacity(n - 1) + ")");
        } return rooms;
    }

    public int getRoomNumber(String roomsListSelection) {
        return Integer.parseInt(roomsListSelection.split(" ")[1]) - 1;
    }

    public ArrayList<String> getEvents(String roomsListSelection) {
        return rm.getEventsOfRoom(getRoomNumber(roomsListSelection));
    }

    public String reMenuButtonText() {
        return "Rooms/Events";
    }

    public String speakerManagementButtonText() {
        return "Speakers";
    }

    public String logoutButtonText() {
        return "Log out";
    }

    public String createSpeakerFormButtonText() {
        return "Create Speaker";
    }

    public String speakerManagerButtonText() {
        return "Speakers";
    }

    public String createButtonText() {
        return "Create";
    }

    public String reManagerButton() {
        return "Rooms/Events";
    }

    public String createRoomFormButtonText() {
        return "Create Room";
    }

    public String viewEventListButtonText() {
        return "View Events";
    }

    public String createEventFormButtonText() {
        return "Create Event";
    }

    public String confirmButtonText() {
        return "Confirm";
    }

    public String reMenuTitle() {
        return "Organizer Panel: Rooms and Events";
    }

    public String speakerManagementMenuTitle() {
        return "Organizer Panel: Speakers";
    }

    public String createSpeakerFormTitle() {
        return "Organizer Panel: Create Speakers";
    }

    public String speakerManagerTitle() {
        return "Organizer Panel: Speaker Management";
    }

    public String mainSceneTitle() {
        return "Organizer Panel: Main Menu";
    }

    public String reManagerTitle() {
        return "Organizer Panel: Room & Event Management";
    }

    public String createRoomFormTitle() {
        return "Organizer Panel: Create Room";
    }

    public String viewEventListTitle() {
        return "Organizer Panel: View Events";
    }

    public String createEventFormTitle() {
        return "Organizer Panel: Create Event";
    }

    public String usernamePrompt() {
        return "Username:";
    }

    public String roomCapacityPrompt() {
        return "Room Capacity:";
    }

    public String createEventTitlePrompt() {
        return "Event Title:";
    }

    public String createEventSpeakerPrompt() {
        return "Event Speaker:";
    }

    public String createEventDatePrompt() {
        return "Date [yyyy/mm/dd]:";
    }

    public String createEventTimePrompt() {
        return "Time [hh:mm]:";
    }

    public String createEventCapacityPrompt() {
        return "Event Capacity:";
    }

    public String createSpeakerStatus(boolean status) {
        if (status) {
            return "Speaker created successfully.";
        } return "Username exists/invalid.";
    }

    public String createRoomStatus(boolean status) {
        if (status) {
            return "Room " + rm.getNumRooms() + " created successfully.";
        } return "Invalid room capacity.";
    }

    public String createEventTitleStatus() {
        return "Invalid event title.";
    }

    public String createEventSpeakerStatus() {
        return "Speaker does not exist.";
    }

    public String createEventDateTimeStatus() {
        return "Invalid date or time - may be in conflict.";
    }

    public String createEventCapacityStatus() {
        return "Invalid event capacity.";
    }

    public String createEventStatus() {
        return "Event created successfully.";
    }
}
