package SpectraSystems.Nexus.models;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class externalFlight {
    private Long flightId;
    private Long originCityId;
    private String originCityName;
    private Long destinationCityId;
    private String destinationCityName;
    private Long providerId;

    @JsonProperty("commentaries")
    private List<Comment> commentaries;

    private Rating rating;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date departureDate;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date arrivalDate;
    private double touristPrice;
    private double businessPrice;
    private String detail;
    private int touristQuantity;
    private int businessQuantity;
    private int touristCapacity;
    private int businessCapacity;
    private int state;
    private externalFlight scale;
    
    private externalFlight returnFlight;

    // Constructors, getters, and setters
    public externalFlight() {
        this.scale = null;
        this.returnFlight = null;
        
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
     * @return Long
     */
    public Long getOriginCityId() {
        return originCityId;
    }

    
    /** 
     * @param originCityId
     */
    public void setOriginCityId(Long originCityId) {
        this.originCityId = originCityId;
    }

    
    /** 
     * @return String
     */
    public String getOriginCityName() {
        return originCityName;
    }

    
    /** 
     * @param originCityName
     */
    public void setOriginCityName(String originCityName) {
        this.originCityName = originCityName;
    }

    
    /** 
     * @return Long
     */
    public Long getDestinationCityId() {
        return destinationCityId;
    }

    
    /** 
     * @param destinationCityId
     */
    public void setDestinationCityId(Long destinationCityId) {
        this.destinationCityId = destinationCityId;
    }

    
    /** 
     * @return String
     */
    public String getDestinationCityName() {
        return destinationCityName;
    }

    
    /** 
     * @param destinationCityName
     */
    public void setDestinationCityName(String destinationCityName) {
        this.destinationCityName = destinationCityName;
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
     * @return Date
     */
    public Date getArrivalDate() {
        return arrivalDate;
    }

    
    /** 
     * @param arrivalDate
     */
    public void setArrivalDate(Date arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    
    /** 
     * @return double
     */
    public double getTouristPrice() {
        return touristPrice;
    }

    
    /** 
     * @param touristPrice
     */
    public void setTouristPrice(double touristPrice) {
        this.touristPrice = touristPrice;
    }

    
    /** 
     * @return double
     */
    public double getBusinessPrice() {
        return businessPrice;
    }

    
    /** 
     * @param businessPrice
     */
    public void setBusinessPrice(double businessPrice) {
        this.businessPrice = businessPrice;
    }

    
    /** 
     * @return String
     */
    public String getDetail() {
        return detail;
    }

    
    /** 
     * @param detail
     */
    public void setDetail(String detail) {
        this.detail = detail;
    }

    
    /** 
     * @return int
     */
    public int getTouristQuantity() {
        return touristQuantity;
    }

    
    /** 
     * @param touristQuantity
     */
    public void setTouristQuantity(int touristQuantity) {
        this.touristQuantity = touristQuantity;
    }

    
    /** 
     * @return int
     */
    public int getBusinessQuantity() {
        return businessQuantity;
    }

    
    /** 
     * @param businessQuantity
     */
    public void setBusinessQuantity(int businessQuantity) {
        this.businessQuantity = businessQuantity;
    }

    
    /** 
     * @return int
     */
    public int getTouristCapacity() {
        return touristCapacity;
    }

    
    /** 
     * @param touristCapacity
     */
    public void setTouristCapacity(int touristCapacity) {
        this.touristCapacity = touristCapacity;
    }

    
    /** 
     * @return int
     */
    public int getBusinessCapacity() {
        return businessCapacity;
    }

    
    /** 
     * @param businessCapacity
     */
    public void setBusinessCapacity(int businessCapacity) {
        this.businessCapacity = businessCapacity;
    }

    
    /** 
     * @return int
     */
    public int getState() {
        return state;
    }

    
    /** 
     * @param state
     */
    public void setState(int state) {
        this.state = state;
    }

    
    /** 
     * @param commentaries
     */
    public void setCommentaries(List<Comment> commentaries) {
        if (commentaries == null) {
            this.commentaries = Collections.emptyList(); // Initialize with an empty list
        } else {
            this.commentaries = commentaries;
        }
    }

    
    /** 
     * @return List<Comment>
     */
    public List<Comment> getComments(){
        return commentaries;
    }

    
    /** 
     * @return Rating
     */
    public Rating getRating() {
        return rating;
    }

    
    /** 
     * @param rating
     */
    public void setRating(Rating rating) {
        this.rating = rating;
    }

    
    /** 
     * @return externalFlight
     */
    public externalFlight getScale() {
        return scale;
    }

    
    /** 
     * @param scale
     */
    public void setScale(externalFlight scale) {
        this.scale = scale;
    }

    
    /** 
     * @return externalFlight
     */
    public externalFlight getReturnFlight() {
        return returnFlight;
    }

    
    /** 
     * @param returnFlight
     */
    public void setReturnFlight(externalFlight returnFlight) {
        this.returnFlight = returnFlight;
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

