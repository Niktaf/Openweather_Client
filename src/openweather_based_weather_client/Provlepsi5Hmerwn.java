/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package openweather_based_weather_client;

import java.io.IOException;
import org.json.JSONException;

/**
 *
 * @author pavlidis_aris
 */
public class Provlepsi5Hmerwn {
    
    // Διάφορες μεταβλητές και αρχικοποίησή τους
    private double temp=0, snow=0, rain=0; // θερμοκρασία
    private int clouds=0; // Νεφοκάλυψη ουρανού
    private String name="", desc="", dt=""; // Όνομα πόλης, περιγραφή καιρού, σφραγίδα χρονοσήμανσης
    private long cityID=0; // Κωδικός πόλης
   
    public void Provlepsi5Imerwn() throws JSONException {
        
        /*
         * Παρακάτω πραγματοποιείται δημιουργία του URI με το οποίο θα ζητηθούν από το OpenWeather
         * όλες οι απαιτούμενες πληροφορίες, η λήψη αυτών με τη μορφή JSON string, η "αποσυναρμολόγηση"
         * του JSON string και η παράδοση των πληροφοριών του στον χρήστη προς περαιτέρω επεξεργασία.
         *
         * ΑΡΧΗ
         *
        */

        // Καθορισμός URI πρόβλεψης καιρικών συνθηκών για τις επόμενες 5 ημέρες
        String forecast = "http://api.openweathermap.org/data/2.5/forecast?id=658225&appid=fd798713a90f9501121e8dc78d7d0a47";
        
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
        } catch (JSONException e) { // Διαχείριση εξαιρέσεων
            e.printStackTrace();
        }
        
        /*
         *
         * ΤΕΛΟΣ
         *
        */
    }
}
