package UseCases;


import Builders.RoomBuilder;
import Entities.Room;
import org.bson.Document;

import java.util.*;

public class RoomManager implements Manager {

    private final Map<String, Room> roomList;

    /**
     * Constructs a RoomManager object.
     */
    public RoomManager() {
        this.roomList = new HashMap<>();
    }

    /**
     * Creates and stores a Room object in the roomList if the Room name does not already exist.
     *
     * @param name     The name of the Room.
     * @param capacity The capacity of the Room.
     */
    public void createRoomInSystem(String name, int capacity) { // Organizer Only
        Room newRoom = new Room(name, capacity); // create new room
        if (!(roomList.containsKey(newRoom.getRoomName()))) { // check if room is already in roomList
            roomList.put(newRoom.getRoomName(), newRoom);
        }
    }

    /**
     * Returns true if the input roomName exists.
     *
     * @param roomName The Name of the Room.
     * @return Whether or not the Room is in the roomList.
     */
    public boolean existsRoom(String roomName) {
        return roomList.containsKey(roomName);
    }

    /**
     * Returns a list of all room names.
     *
     * @return List of all rooms listed by their names.
     */
    public List<String> getAllRooms() {
        List<String> allRoomList = new ArrayList<>();
        for (Room val : roomList.values()) {
            allRoomList.add(val.getRoomName());
        }
        return allRoomList;
    }

    /**
     * Gets a Map of the Name and Capacity for a single Room. Stores each field as the key and a list of Strings for the
     * value(s) corresponding to the field.
     *
     * @param roomName The Name of the Room.
     * @return A Map representing a single Room.
     */
    @Override
    public Map<String, List<String>> getInfo(String roomName) {
        Map<String, List<String>> roomInfo = new HashMap<>();
        Room currentRoom = roomList.get(roomName);
        roomInfo.put("roomName", Collections.singletonList(roomName));
        roomInfo.put("capacity", Collections.singletonList(Integer.toString(currentRoom.getCapacity())));
        return roomInfo;
    }

    /**
     * Gets a list of Maps, each representing a Room and containing the Name and Capacity of that Room.
     * This list should represent all Rooms existing in the program at that moment.
     *
     * @return A list of Maps, which represent a Room each.
     */
    @Override
    public List<Map<String, List<String>>> getInfoAsList() {
        List<Map<String, List<String>>> allRoomInfo = new ArrayList<>();
        for (Room singleRoom : roomList.values()) {
            allRoomInfo.add(getInfo(singleRoom.getRoomName()));
        }
        return allRoomInfo;
    }

    /**
     * Create a new Room from the imported Document from the database and add it to the RoomManager.
     *
     * @param room The Document containing information about a single room.
     */
    @Override
    public void importEntity(Document room) {
        Room newRoom = new RoomBuilder()
                .setRoomName(room.getList("roomName", String.class))
                .setCapacity(room.getList("capacity", String.class))
                .build();
        roomList.put(newRoom.getRoomName(), newRoom);
    }

    /**
     * Returns a String representation of the RoomManager object.
     *
     * @return the String representation of the RoomManager object.
     */
    @Override
    public String toString() {
        return "Room Manager";
    }

}


























