package org.example.demo.models;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "Reservation")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;
    private String packageType;
    private LocalDate reservationDate;
    private LocalDate checkInDate;
    private double amount;

    public Reservation(Customer customer, Room room, String packageType, LocalDate reservationDate, LocalDate checkInDate, double amount) {
        this.customer = customer;
        this.room = room;
        this.packageType = packageType;
        this.reservationDate = reservationDate;
        this.checkInDate = checkInDate;
        this.amount = amount;
    }

    public Reservation() {
    }

    public int getId() {
        return id;
    }
    public String getIdAsString() {
        return String.valueOf(id);
    }
    public void setId(int id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public String getPackageType() {
        return packageType;
    }

    public void setPackageType(String packageType) {
        this.packageType = packageType;
    }

    public LocalDate getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(LocalDate reservationDate) {
        this.reservationDate = reservationDate;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

}