/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package openweather_based_weather_client;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import org.json.JSONException;

/**
 *
 * @author Παυλίδης Αριστείδης
 * @author Ταφραλίδης Νικόλαος
 * @author Τριανταφυλλίδης Τρύφων
 * ΘΕΣ-2 (2017-2018)
 */ 
public class Provlepsi5Hmerwn {
    
    // Διάφορες μεταβλητές και αρχικοποίησή τους
    
    // θερμοκρασία, χιόνι, βροχή, ταχύτητα ανέμου, ελάχιστη θερμοκρασία, μέγιστη θερμοκρασία
    private double temp=0, snow=0, rain=0, wind=0, temp_min=0, temp_max=0; 
    private int clouds=0; // Νεφοκάλυψη ουρανού
    private String desc=""; // Περιγραφή καιρού
    private Timestamp dt; // Σφραγίδα χρονοσήμανσης
    private long cityID=0; // Κωδικός πόλης
    public ArrayList<KairosPolis> arKP_5hmerwn = new ArrayList<>();
    
    public void Provlepsi5Imerwn(String forecast) throws JSONException {
        
        /*
         * Παρακάτω πραγματοποιείται δημιουργία του URI με το οποίο θα ζητηθούν από το OpenWeather
         * όλες οι απαιτούμενες πληροφορίες, η λήψη αυτών με τη μορφή JSON string, η "αποσυναρμολόγηση"
         * του JSON string και η παράδοση των πληροφοριών του στον χρήστη προς περαιτέρω επεξεργασία.
         *
         * ΑΡΧΗ
         *
        */
        OW_Collector ow = new OW_Collector(); // Συλλέκτης δεδομένων
        String prognosis = null; // Πρόγνωση

        try {
            prognosis = ow.HttpGet(forecast); // Λήψη δεδομένων σε μορφή JSON από τον κεντρικό εξυπηρετητή
        } catch (IOException e) { // Διαχείριση εξαιρέσεων 
            e.printStackTrace();
        }

        JsonParser jp = new JsonParser(prognosis); // Αποσυνθέτης JSON string
        
        /* Λήψη πλήθους εγγραφών που αντιστοιχούν στις πόλεις οι οποίες περιέχονται
         * μέσα στο JSON string
        */
        int metritisEggrafwn = (int) jp.get_NumOfRecs(); 
                
        for (int i = 0; i < metritisEggrafwn; i++) {
            KairosPolis kp_5hmerwn = new KairosPolis(); // Δημιουργία κλάσης για κάθε πόλη
            try {
                /* Λήψη δεδομένων από το JSON string και τοποθέτησή 
                 * τους μέσα στην κλάση της κάθε πόλης
                */
                kp_5hmerwn.setTemp(jp.get_Temp(i));
                kp_5hmerwn.setTemp_max(jp.get_maxTemp(i));
                kp_5hmerwn.setTemp_min(jp.get_minTemp(i));
                kp_5hmerwn.setDesc(jp.get_WeatherDesc(i));
                kp_5hmerwn.setClouds(jp.get_CloudsAll(i));
                kp_5hmerwn.setDt(jp.get_DateTime(i));
                kp_5hmerwn.setCityID(jp.get_CityID_5Hmerwn());
                kp_5hmerwn.setWind(jp.get_WindSpeed(i));
                try {
                    kp_5hmerwn.setRain(jp.get_Rain(i));
                
                } catch (Exception ex) {
                    kp_5hmerwn.setRain(0);
                }
                try {
                    kp_5hmerwn.setSnow(jp.get_Snow(i));
                } catch (Exception ex) {
                    kp_5hmerwn.setSnow(0);
                }
                arKP_5hmerwn.add(kp_5hmerwn); // Εισαγωγή της κλάσης που ολοκληρώθηκε η εισαγωγή των δεδομένων, στην Arraylist
            } catch (JSONException e) { // Διαχείριση εξαιρέσεων
                e.printStackTrace();
            }
        }

        
        /*
         *
         * ΤΕΛΟΣ
         *
        */
    }
    
    /*
     * GETTERS
     * APXH
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

    public double getWind() {
        return wind;
    }

    public int getClouds() {
        return clouds;
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
      
    public double getTemp_min() {
        return temp_min;
    }

    public double getTemp_max() {
        return temp_max;
    }
    
    /*
     * GETTERS
     * ΤΕΛΟΣ
    */

}
