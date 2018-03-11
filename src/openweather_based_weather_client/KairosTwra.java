/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package openweather_based_weather_client;

import java.io.IOException;
import java.sql.Timestamp;
import org.json.JSONException;

/**
 *
 * @author Παυλίδης Αριστείδης
 * @author Ταφραλίδης Νικόλαος
 * @author Τριανταφυλλίδης Τρύφων
 * ΘΕΣ-2 (2017-2018)
 */ 

public class KairosTwra {
    
    // Διάφορες μεταβλητές και αρχικοποίησή τους
    private double temp=0, snow=0, rain=0, wind=0; // θερμοκρασία, χιόνι, βροχή, ταχύτητα ανέμου
    private int clouds=0; // Νεφοκάλυψη ουρανού
    private String name="", desc=""; // Όνομα πόλης, περιγραφή καιρού, σφραγίδα χρονοσήμανσης
    private Timestamp dt;
    private long cityID=0; // Κωδικός πόλης
    
    public void TrexwnKairos() throws JSONException {
        
        /*
         * Παρακάτω πραγματοποιείται δημιουργία του URI με το οποίο θα ζητηθούν από το OpenWeather
         * όλες οι απαιτούμενες πληροφορίες, η λήψη αυτών με τη μορφή JSON string, η "αποσυναρμολόγηση"
         * του JSON string και η παράδοση των πληροφοριών του στον χρήστη προς περαιτέρω επεξεργασία.
         *
         * ΑΡΧΗ
         *
        */

        // Καθορισμός URI τρεχουσών καιρικών συνθηκών
        String forecast = "http://api.openweathermap.org/data/2.5/group?id=734077&units=metric&appid=fd798713a90f9501121e8dc78d7d0a47";
        
        OW_Collector ow = new OW_Collector(); // Συλλέκτης δεδομένων
        String prognosis = null; // Πρόγνωση
     
        try {
            prognosis = ow.HttpGet(forecast); // Λήψη δεδομένων σε μορφή JSON από τον κεντρικό εξυπηρετητή
        } catch (IOException e) { // Διαχείριση εξαιρέσεων 
            e.printStackTrace();
        }
        
        JsonParser jp = new JsonParser(prognosis); // Αποσυνθέτης JSON string
               
        try {
            // Λήψη δεδομένων από το JSON string
            temp = jp.get_Temp();
            name = jp.get_City();
            desc = jp.get_WeatherDesc();
            clouds = jp.get_CloudsAll();
            dt = jp.get_DateTime();
            cityID = jp.get_CityID();
            wind = jp.get_WindSpeed();
        } catch (JSONException e) { // Διαχείριση εξαιρέσεων
            e.printStackTrace();
        }
        
        /*
         *
         * ΤΕΛΟΣ
         *
        */
    }
    
    /*
     * GETTERS KAI SETTERS
     * ΑΡΧΗ
    */
    public double getTemp() {
        return temp;
    }

    public double getSnow() {
        return snow;
    }

    public double getRain() {
        return rain;
    }

    public int getClouds() {
        return clouds;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public Timestamp getDt() {
        return dt;
    }

    public long getCityID() {
        return cityID;
    }
    
    public double getWindSpeed() {
        return wind;
    }
    /*
     * GETTERS KAI SETTERS
     * ΤΕΛΟΣ
    */
}
