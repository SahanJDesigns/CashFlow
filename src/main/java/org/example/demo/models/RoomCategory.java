package org.example.demo.models;

import javax.persistence.*;

@Entity
@Table(name = "RoomCategory")
public class RoomCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String description;

    public RoomCategory(String name, String description) {
        this.name = name;
        this.description = description;
    }
    public RoomCategory(){

    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

