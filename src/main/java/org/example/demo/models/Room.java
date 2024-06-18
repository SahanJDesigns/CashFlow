package org.example.demo.models;

import javax.persistence.*;

@Entity
@Table(name = "Room")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private RoomCategory category;
    @JoinColumn(name = "room_number")
    private String roomNumber;
    private String RoomCost;
    private String status;

    public Room(String roomNumber,RoomCategory category, String roomCost, String status) {
        this.category = category;
        this.roomNumber = roomNumber;
        RoomCost = roomCost;
        this.status = status;
    }
    public Room(){
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public RoomCategory getCategory() {
        return category;
    }

    public void setCategory(RoomCategory category) {
        this.category = category;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getRoomCost() {
        return RoomCost;
    }

    public void setRoomCost(String roomCost) {
        RoomCost = roomCost;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}