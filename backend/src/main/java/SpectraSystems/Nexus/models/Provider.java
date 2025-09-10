package SpectraSystems.Nexus.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;

@Entity
@Builder
@Table(name = "PROVIDERS")
public class Provider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "provider_name", nullable = false)
    private String providerName;

    @Column(name = "provider_url", nullable = false)
    private String providerUrl;

    @Column(name = "type", nullable = false)
    private Type type;

    @Column(name = "gains_flights", nullable = true)
    private Double gainsFlights;

    @Column(name = "gains_hotel",nullable = true)
    private Double gainsHotel;

    @Column(name = "discount", nullable = true)
    private Double percentageDiscount;

    public Provider() {
    }

    public Provider(Long id, String providerName, String providerUrl, Type type, Double gainsFlights, Double gainsHotel, Double percentageDiscount) {
        this.id = id;
        this.providerName = providerName;
        this.providerUrl = providerUrl;
        this.type = type;
        this.gainsFlights = gainsFlights;
        this.gainsHotel = gainsHotel; 
        this.percentageDiscount = percentageDiscount;
    }

    public Provider(String providerName, String providerUrl, Double gainsFlights, Type type, Double gainsHotel, Double percentageDiscount) {
        this.providerName = providerName;
        this.providerUrl = providerUrl;
        this.type = type;
        this.gainsFlights = gainsFlights;
        this.gainsHotel = gainsHotel; 
        this.percentageDiscount = percentageDiscount;
    }

    
    /** 
     * @return Long
     */
    // Getters and Setters

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
    public String getProviderName() {
        return providerName;
    }

    
    /** 
     * @param providerName
     */
    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    
    /** 
     * @return String
     */
    public String getProviderUrl() {
        return providerUrl;
    }

    
    /** 
     * @param providerUrl
     */
    public void setProviderUrl(String providerUrl) {
        this.providerUrl = providerUrl;
    }

    
    /** 
     * @return Type
     */
    public Type getType() {
        return type;
    }

    
    /** 
     * @param type
     */
    public void setType(Type type) {
        this.type = type;
    }

    
    /** 
     * @return Double
     */
    public Double getGainsFlights() {
        return gainsFlights != null ? gainsFlights : 0.0;
    }

    
    /** 
     * @param gainsFlights
     */
    public void setGainsFlights(double gainsFlights) {
        this.gainsFlights = gainsFlights;
    }

    
    /** 
     * @return Double
     */
    public Double getGainsHotel() {
        return gainsHotel != null ? gainsHotel : 0.0;
    }

    
    /** 
     * @param gainsHotel
     */
    public void setGainsHotel(double gainsHotel) {
        this.gainsHotel = gainsHotel;
    }

    
    /** 
     * @return Double
     */
    public Double getPercentageDiscount() {
        return percentageDiscount != null ? percentageDiscount : 0.0;
    }

    
    /** 
     * @param percentageDiscount
     */
    public void setPercentageDiscount(double percentageDiscount) {
        this.percentageDiscount = percentageDiscount;
    }
}

