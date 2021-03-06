/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package openweather_based_weather_client;

import java.sql.Timestamp;

/**
 *
 * @author Παυλίδης Αριστείδης
 * @author Ταφραλίδης Νικόλαος
 * @author Τριανταφυλλίδης Τρύφων
 * ΘΕΣ-2 (2017-2018)
 */ 
public class KairosPolis {
    
    // Διάφορες μεταβλητές και αρχικοποίησή τους
    
    // θερμοκρασία, χιόνι, βροχή, ταχύτητα ανέμου, ελάχιστη θερμοκρασία, μέγιστη θερμοκρασία
    private double temp=0, snow=0, rain=0, wind=0, temp_min=0, temp_max=0; 
    private int clouds=0; // Νεφοκάλυψη ουρανού
    private String desc=""; // Περιγραφή καιρού
    private Timestamp dt; // Χρονοσφραγίδα πληροφορίας
    private long cityID=0; // Κωδικός πόλης
    
    //Κατασκευαστής κλάσης
    public KairosPolis(){
    }
    
    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public double getSnow() {
        return snow;
    }

    public void setSnow(double snow) {
        this.snow = snow;
    }

    public double getRain() {
        return rain;
    }

    public void setRain(double rain) {
        this.rain = rain;
    }

    public double getWind() {
        return wind;
    }

    public void setWind(double wind) {
        this.wind = wind;
    }

    public int getClouds() {
        return clouds;
    }

    public void setClouds(int clouds) {
        this.clouds = clouds;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Timestamp getDt() {
        return dt;
    }

    public void setDt(Timestamp dt) {
        this.dt = dt;
    }

    public long getCityID() {
        return cityID;
    }

    public void setCityID(long cityID) {
        this.cityID = cityID;
    }

    public double getTemp_min() {
        return temp_min;
    }

    public void setTemp_min(double temp_min) {
        this.temp_min = temp_min;
    }

    public double getTemp_max() {
        return temp_max;
    }

    public void setTemp_max(double temp_max) {
        this.temp_max = temp_max;
    }
    
    
}
