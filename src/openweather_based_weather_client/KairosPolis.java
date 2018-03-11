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
    private double temp=0, snow=0, rain=0, wind=0; // θερμοκρασία, χιόνι, βροχή, ταχύτητα ανέμου
    private int clouds=0; // Νεφοκάλυψη ουρανού
    private String name="", desc=""; // Όνομα πόλης, περιγραφή καιρού, σφραγίδα χρονοσήμανσης
    private Timestamp dt;
    private long cityID=0; // Κωδικός πόλης
    
    
}
