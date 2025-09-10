package SpectraSystems.Nexus.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "TICKET")
public class TicketPurchase {

    @Id
    @Column(name = "ticket_id")
    private int ticketId;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "flight_id")
    private int flightId;

    private String type;
    private String state;

    // Constructors, getters, and setters

    public TicketPurchase() {
    }

    public TicketPurchase(int ticketId, int userId, int flightId, String type, String state) {
        this.ticketId = ticketId;
        this.userId = userId;
        this.flightId = flightId;
        this.type = type;
        this.state = state;
    }

    
    /** 
     * @return int
     */
    public int getTicketId() {
        return ticketId;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getFlightId() {
        return flightId;
    }

    public void setFlightId(int flightId) {
        this.flightId = flightId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
