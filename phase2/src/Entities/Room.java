package Entities;

public class Room {
    private String roomName;
    private int capacity;

    /**
     * Create a room.
     *
     * @param name     The name of the room.
     * @param capacity The maximum amount of users allowed in the room.
     */
    public Room(String name, int capacity) {
        this.roomName = name;
        this.capacity = capacity;
    }

    /**
     * Get the name of a room.
     *
     * @return name of the room.
     */
    public String getRoomName() {
        return roomName;
    }

    /**
     * Get the capacity of a room.
     *
     * @return Capacity of the room.
     */
    public int getCapacity() {
        return capacity;
    }

    @Override
    public String toString() {
        return roomName;
    }
}
