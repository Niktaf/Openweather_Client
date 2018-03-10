/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package openweather_based_weather_client;
import openweather_based_weather_client.OW_Collector;
import openweather_based_weather_client.JsonParser;
import java.io.IOException;
import org.json.JSONException;

/**
 *
 * @author Παυλίδης Αριστείδης
 * @author Ταφραλίδης Νικόλαος
 * @author Τριανταφυλλίδης Τρύφων
 * ΘΕΣ-2 (2017-2018)
 */ 
public class Main {

    public static void main(String[] args) throws JSONException {
        
        // Καθορισμός URI
        
        // URI τρεχουσών καιρικών συνθηκών
        //String forecast = "http://api.openweathermap.org/data/2.5/group?id=2650225&units=metric&appid=fd798713a90f9501121e8dc78d7d0a47";
        
        // URI πρόβλεψης καιρικών συνθηκών για τις επόμενες 5 ημέρες
        String forecast = "http://api.openweathermap.org/data/2.5/forecast?id=658225&appid=fd798713a90f9501121e8dc78d7d0a47";
        
        OW_Collector ow = new OW_Collector(); // Συλλέκτης δεδομένων
        String prognosis = null; // Πρόγνωση
        
        // Διάφορες μεταβλητές και αρχικοποίησή τους
        double temp=0; // θερμοκρασία
        int clouds=0; // Νεφοκάλυψη ουρανού
        String name="", desc="", dt=""; // Όνομα πόλης, περιγραφή καιρού, σφραγίδα χρονοσήμανσης
        long cityID=0; // Κωδικός πόλης
        
        try {
            prognosis = ow.HttpGet(forecast); // Λήψη δεδομένων σε μορφή JSON από τον κεντρικό εξυπηρετητή
        } catch (IOException e) { // Διαχείριση εξαιρέσεων 
            e.printStackTrace();
        }
        
        System.out.println(prognosis); // Εμφάνιση JSON string στην οθόνη
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
        
        // Εμφάνιση τελικού μηνύματος στο χρήστη
        System.out.println("Η θερμοκρασία στην "+name+" με κωδικό: "+ cityID +" είναι τώρα : "+temp+" Βαθμοί Κελσίου και ο καιρός είναι "+desc + "\r\nμε νεφοκάλυψη ουρανού "+clouds+"%. Τα δεδομένα ελήφθησαν ώρα: "+dt+".");
    }
}