package SpectraSystems.Nexus.models;
import java.util.Date;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.TemporalType;
import lombok.Builder;

@Entity
@Builder
@Table(name = "RESERVATION")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "hotel_id")
    private String hotelId;

    @Column(name = "userid", nullable = false)
    private Long userid;

    @Column(nullable = false)
    private String hotel;

    @Column(name = "dateStart", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dateStart;

    @Column(name = "dateEnd", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dateEnd;

    @Column(name = "roomType")
    private String roomType;

    @Column(nullable = false, unique = true)
    private String reservationNumber;

    @Column(name = "location")
    private String location;

    @Column(name = "rating")
    private Long rating;

    @Column(name = "bed_size")
    private String bedSize;

    @Column(name = "bed_amount")
    private Integer bedAmount;

    @Column(name = "price")
    private Double price;
    
    @Column(name = "total_days")
    private Integer totalDays;

    @Column(name = "total_price")
    private Double totalPrice;

    @Column(name = "guests")
    private Integer guests;
    // Getters, setters, constructors, and other methods...

    @Column(name="state")
    private String state;

    @Column(name="bundle")
    private String bundle;

    private Long providerId;

    public Reservation() {

    }

    public Reservation(Long id, String hotelId, Long user,  String hotel, Date dateStart, Date dateEnd, String roomType,
            String reservationNumber, String location, Long rating, String bedSize, Integer bedAmount, Double price, Integer totalDays, Double totalPrice, Integer guests, String state, String bundle, Long providerId) {
        this.userid = user;
        this.hotelId = hotelId;
        this.hotel = hotel;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.roomType = roomType;
        this.reservationNumber = reservationNumber;
        this.location = location;
        this.bedSize = bedSize;
        this.bedAmount = bedAmount;
        this.price = price;
        this.totalDays = totalDays;
        this.totalPrice = totalPrice;
        this.guests = guests;
        this.state = "active";
    }

    
    /** 
     * @return Long
     */
    public Long getId() {
        return id;
    }

    
    /** 
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    
    /** 
     * @return String
     */
    public String getState() {
        return state;
    }

    
    /** 
     * @param state
     */
    public void setState(String state) {
        this.state = state;
    }

    
    /** 
     * @return String
     */
    public String getHotelId(){
        return hotelId;
    }

    
    /** 
     * @param hotelId
     */
    public void setHotelId(String hotelId){
        this.hotelId = hotelId;
    }

    
    /** 
     * @param rating
     */
    public void setRating(Long rating) {
        this.rating = rating;
    }

    
    /** 
     * @return Long
     */
    public Long getRating( ) {
        return rating;
    }

    
    /** 
     * @return Long
     */
    public Long getUser() {
        return userid;
    }

    
    /** 
     * @param user
     */
    public void setUser(Long user) {
        this.userid = user;
    }

    
    /** 
     * @return String
     */
    public String getHotel() {
        return hotel;
    }

    
    /** 
     * @param hotel
     */
    public void setHotel(String hotel) {
        this.hotel = hotel;
    }

    
    /** 
     * @return Date
     */
    public Date getDateStart() {
        return dateStart;
    }

    
    /** 
     * @param dateStart
     */
    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    
    /** 
     * @return Date
     */
    public Date getDateEnd() {
        return dateEnd;
    }

    
    /** 
     * @param dateEnd
     */
    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }

    
    /** 
     * @return String
     */
    public String getRoomType() {
        return roomType;
    }

    
    /** 
     * @param roomType
     */
    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    
    /** 
     * @return String
     */
    public String getReservationNumber() {
        return reservationNumber;
    }

    
    /** 
     * @param reservationNumber
     */
    public void setReservationNumber(String reservationNumber) {
        this.reservationNumber = reservationNumber;
    }

    
    /** 
     * @return String
     */
    public String getLocation() {
        return location;
    }

    
    /** 
     * @param location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    
    /** 
     * @return String
     */
    public String getBedSize() {
        return bedSize;
    }
    
    
    /** 
     * @param bedSize
     */
    public void setBedSize(String bedSize) {
        this.bedSize = bedSize;
    }
    
    
    /** 
     * @return Integer
     */
    public Integer getBedAmount() {
        return bedAmount;
    }
    
    
    /** 
     * @param bedAmount
     */
    public void setBedAmount(Integer bedAmount) {
        this.bedAmount = bedAmount;
    }
    
    
    /** 
     * @return Double
     */
    public Double getPrice() {
        return price;
    }
    
    
    /** 
     * @param price
     */
    public void setPrice(Double price) {
        this.price = price;
    }

    
    /** 
     * @return Integer
     */
    public Integer getTotalDays() {
        return totalDays;
    }

    
    /** 
     * @param totalDays
     */
    public void setTotalDays(Integer totalDays) {
        this.totalDays = totalDays;
    }

    
    /** 
     * @return Double
     */
    public Double getTotalPrice() {
        return totalPrice;
    }

    
    /** 
     * @param totalPrice
     */
    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    
    /** 
     * @return Integer
     */
    public Integer getGuests() {
        return guests;
    }

    
    /** 
     * @param guests
     */
    public void setGuests(Integer guests) {
        this.guests = guests;
    }

    
    /** 
     * @param bundle
     */
    public void setBundle(String bundle){
        this.bundle = bundle;
    }

    
    /** 
     * @return String
     */
    public String getBundle(){
        return bundle;
    }

    
    /** 
     * @return Long
     */
    public Long getProviderId(){
        return providerId;
    }

    
    /** 
     * @param providerId
     */
    public void setProviderId(Long providerId){
        this.providerId = providerId;
    }
}

