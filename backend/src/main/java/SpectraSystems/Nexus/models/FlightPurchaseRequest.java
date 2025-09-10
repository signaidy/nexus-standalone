package SpectraSystems.Nexus.models;

import java.util.Date;

public class FlightPurchaseRequest {
    private Long user_id;
    private Long flightId;
    private String state;
    private String type;
    private Long userId;
    private Date departureDate;
    private String departureLocation;
    private String arrivalLocation;
    private Date returnDate;
    private Date purchaseDate;
    private Double price;
    private Long rating;
    private String bundle;

    public FlightPurchaseRequest() {
        // Default constructor
    }

    public FlightPurchaseRequest(Long userId, Long flightId, String state, String type, Date departureDate, String departureLocation, String arrivalLocation, Date returnDate, Long rating, Date purchaseDate, String bundle, Double price) {
        this.userId = userId;
        this.flightId = flightId;
        this.state = state;
        this.type = type;
        this.departureDate = departureDate;
        this.departureLocation = departureLocation;
        this.arrivalLocation = arrivalLocation;
        this.returnDate = returnDate;
        this.rating = rating;
        this.purchaseDate = purchaseDate;
        this.bundle = bundle;
        this.price = price;
    }

    
    /** 
     * @return Long
     */
    public Long getUser_id() {
        return user_id;
    }

    
    /** 
     * @param user_id
     */
    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    
    /** 
     * @return Long
     */
    public Long getFlightId() {
        return flightId;
    }

    
    /** 
     * @param flightId
     */
    public void setFlightId(Long flightId) {
        this.flightId = flightId;
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
    public String getType() {
        return type;
    }

    
    /** 
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }
    
    /** 
     * @return Long
     */
    public Long getUserId() {
        return userId;
    }

    
    /** 
     * @param userId
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    
    /** 
     * @return Date
     */
    public Date getDepartureDate() {
        return departureDate;
    }

    
    /** 
     * @param departureDate
     */
    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }

    
    /** 
     * @return String
     */
    public String getDepartureLocation() {
        return departureLocation;
    }

    
    /** 
     * @param departureLocation
     */
    public void setDepartureLocation(String departureLocation) {
        this.departureLocation = departureLocation;
    }

    
    /** 
     * @return String
     */
    public String getArrivalLocation() {
        return arrivalLocation;
    }

    
    /** 
     * @param arrivalLocation
     */
    public void setArrivalLocation(String arrivalLocation) {
        this.arrivalLocation = arrivalLocation;
    }

    
    /** 
     * @return Date
     */
    public Date getReturnDate() {
        return returnDate;
    }

    
    /** 
     * @param returnDate
     */
    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    
    /** 
     * @return Long
     */
    public Long getRating() {
        return rating;
    }

    
    /** 
     * @param rating
     */
    public void setRating(Long rating) {
        this.rating = rating;
    }
    
    
    /** 
     * @return Date
     */
    public Date getPurchaseDate() {
        return purchaseDate;
    }

    
    /** 
     * @param purchaseDate
     */
    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
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
}
