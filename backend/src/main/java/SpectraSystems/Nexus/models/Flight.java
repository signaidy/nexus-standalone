package SpectraSystems.Nexus.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import lombok.Builder;
import lombok.Builder.Default;

@Entity
@Builder
@Table(name = "FLIGHT")
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "userid", nullable = false)
    private Long userid;

    @Column(nullable = false)
    private String flightNumber;

    @Column(name = "departureDate", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date departureDate;

    @Column(nullable = false)
    private String departureLocation;

    @Column(nullable = false)
    private String arrivalLocation;

    @Temporal(TemporalType.DATE)
    private Date returnDate;

    // Own the relation with a FK in TicketPurchase (flight_id)
    @OneToMany
    @JoinColumn(name = "flight_id")
    @Default
    private List<TicketPurchase> tickets = new ArrayList<>();

    @Column(name = "FLIGHT_TYPE")
    private String type;

    @Column(name = "purchase_date", nullable = false)
    private LocalDate purchaseDate;

    @Column(nullable = false)
    private Double price;

    @Column(name = "state")
    private String state;

    @Column(name = "bundle")
    private String bundle;

    private Long rating;

    private Long providerId;

    // --- Constructors required by JPA / your API ---

    public Flight() {
        // JPA
    }

    // Keep a full-args style constructor similar to yours, but honor parameters
    public Flight(
        Long id,
        Long user,
        String flightNumber,
        Date departureDate,
        String departureLocation,
        String arrivalLocation,
        Date returnDate,
        List<TicketPurchase> ticketPurchases,
        String type,
        LocalDate purchaseDate,
        Double price,
        String state,
        String bundle,
        Long rating,
        Long providerId
    ) {
        this.id = id;
        this.userid = user;
        this.flightNumber = flightNumber;
        this.departureDate = departureDate;
        this.departureLocation = departureLocation;
        this.arrivalLocation = arrivalLocation;
        this.returnDate = returnDate;
        this.tickets = (ticketPurchases != null) ? ticketPurchases : new ArrayList<>();
        this.type = type;
        this.purchaseDate = (purchaseDate != null) ? purchaseDate : LocalDate.now();
        this.price = price;
        this.state = (state != null) ? state : "active";
        this.bundle = bundle;
        this.rating = rating;
        this.providerId = providerId;
    }

    public Flight(
        Long id,
        Long user,
        String flightNumber,
        Date departureDate,
        String departureLocation,
        String arrivalLocation,
        Date returnDate,
        String type,
        Double price,
        String bundle
    ) {
        this.id = id;
        this.userid = user;
        this.flightNumber = flightNumber;
        this.departureDate = departureDate;
        this.departureLocation = departureLocation;
        this.arrivalLocation = arrivalLocation;
        this.returnDate = returnDate;
        this.type = type;
        this.price = price;
        this.state = "active";
        this.purchaseDate = LocalDate.now();
        this.bundle = bundle;
    }

    // --- Getters/Setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    // NOTE: field is 'userid' but getter/setter use 'User' per your current API
    public Long getUser() { return userid; }
    public void setUser(Long user) { this.userid = user; }

    public String getFlightNumber() { return flightNumber; }
    public void setFlightNumber(String flightNumber) { this.flightNumber = flightNumber; }

    public Date getDepartureDate() { return departureDate; }
    public void setDepartureDate(Date departureDate) { this.departureDate = departureDate; }

    public String getDepartureLocation() { return departureLocation; }
    public void setDepartureLocation(String departureLocation) { this.departureLocation = departureLocation; }

    public String getArrivalLocation() { return arrivalLocation; }
    public void setArrivalLocation(String arrivalLocation) { this.arrivalLocation = arrivalLocation; }

    public Date getReturnDate() { return returnDate; }
    public void setReturnDate(Date returnDate) { this.returnDate = returnDate; }

    public List<TicketPurchase> getTickets() { return tickets; }
    public void setTickets(List<TicketPurchase> tickets) { this.tickets = tickets; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public LocalDate getPurchaseDate() { return purchaseDate; }
    public void setPurchaseDate(LocalDate purchaseDate) { this.purchaseDate = purchaseDate; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public String getState(){ return state; }
    public void setState(String state){ this.state = state; }

    public Long getRating(){ return rating; }
    public void setRating(Long rating){ this.rating = rating; }

    public String getBundle(){ return bundle; }
    public void setBundle(String bundle){ this.bundle = bundle; }

    public Long getProviderId(){ return providerId; }
    public void setProviderId(Long providerId){ this.providerId = providerId; }
}