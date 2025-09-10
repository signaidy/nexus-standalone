package SpectraSystems.Nexus.models;

public class Rating {
    private double average;
    private int count;

    public Rating() {
    }

    public Rating(double average, int count) {
        this.average = average;
        this.count = count;
    }

    
    /** 
     * @return double
     */
    public double getAverage() {
        return average;
    }

    
    /** 
     * @param average
     */
    public void setAverage(double average) {
        this.average = average;
    }

    
    /** 
     * @return int
     */
    public int getCount() {
        return count;
    }

    
    /** 
     * @param count
     */
    public void setCount(int count) {
        this.count = count;
    }
}

