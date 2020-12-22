package Builders;

import Entities.Room;

import java.util.List;

public class RoomBuilder {

    private String roomName;
    private int capacity;

    public RoomBuilder() {
    }

    /**
     * Sets the roomName of the Room to be returned, and returns the updated RoomBuilder.
     *
     * @param roomName roomName of the Room in a list of size 1, obtained from the database
     * @return the RoomBuilder with updated roomName
     */
    public RoomBuilder setRoomName(List<String> roomName) {
        this.roomName = roomName.get(0);
        return this;
    }

    /**
     * Sets the capacity of the Room to be returned, and returns the updated RoomBuilder.
     *
     * @param capacity capacity of the Room in a list of size 1, obtained from the database
     * @return the RoomBuilder with updated capacity
     */
    public RoomBuilder setCapacity(List<String> capacity) {
        this.capacity = Integer.parseInt(capacity.get(0));
        return this;
    }

    /**
     * Builds the Room with the same parameters as this RoomBuilder, and returns it.
     *
     * @return a Room with the parameters copied from this RoomBuilder
     */
    public Room build() {
        return new Room(this.roomName,this.capacity);
    }
}
