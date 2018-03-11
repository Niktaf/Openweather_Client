/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package openweather_based_weather_client;

/**
 *
 * @author Παυλίδης Αριστείδης
 * @author Ταφραλίδης Νικόλαος
 * @author Τριανταφυλλίδης Τρύφων
 * ΘΕΣ-2 (2017-2018)
 */ 
import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonParser {
   private String json; //json string

    // Κατασκευαστής
    public JsonParser(String json) throws JSONException{
        this.json=json;
    }

    // Εξόρυξη θερμοκρασίας από το JSON string
    public double get_Temp()throws JSONException{

        JSONObject obj=new JSONObject(this.json); // Δημιουργία νέου αντικειμένου JSON
        JSONArray list=(JSONArray)obj.get("list"); // Επιλογή λίστας δεδομένων
        JSONObject city=(JSONObject)list.get(0); // Επιλογή πόλης
        JSONObject main=(JSONObject)city.get("main"); // Επιλογή πυρήνα καιρικών πληροφοριών
        double temp=main.getDouble("temp"); // Ανάγνωση πεδίου θερμοκρασίας επιλεγμένης πόλης
        
        return temp; // Επιστροφή δεδομένων στον χρήστη
    }
    
    // Εξόρυξη ονόματος πόλης από το JSON string
    public String get_City()throws JSONException{

        JSONObject obj=new JSONObject(this.json); // Δημιουργία νέου αντικειμένου JSON
        JSONArray list=(JSONArray)obj.get("list"); // Επιλογή λίστας δεδομένων
        JSONObject city=(JSONObject)list.get(0); // Επιλογή πόλης
        String name=city.getString("name"); // Ανάγνωση πεδίου ονόματος επιλεγμένης πόλης
               
        return name; // Επιστροφή δεδομένων στον χρήστη
    }

    // Εξόρυξη περιγραφής καιρού από το JSON string    
    public String get_WeatherDesc()throws JSONException{

        JSONObject obj=new JSONObject(this.json); // Δημιουργία νέου αντικειμένου JSON
        JSONArray list=(JSONArray)obj.get("list"); // Επιλογή λίστας δεδομένων
        JSONObject city=(JSONObject)list.get(0); // Επιλογή πόλης
        JSONArray main=(JSONArray)city.get("weather"); // Επιλογή αντικειμένου καιρού
        JSONObject wd=(JSONObject)main.get(0); // Επικρατούσες καιρικές συνθήκες
        String desc=wd.getString("description"); // Ανάγνωση πεδίου περιγραφής καιρικών συνθηκών
               
        return desc; // Επιστροφή δεδομένων στον χρήστη
    }
    
    // Εξόρυξη νεφοκάλυψης από το JSON string
    public int get_CloudsAll()throws JSONException{

        JSONObject obj=new JSONObject(this.json); // Δημιουργία νέου αντικειμένου JSON
        JSONArray list=(JSONArray)obj.get("list"); // Επιλογή λίστας δεδομένων
        JSONObject city=(JSONObject)list.get(0); // Επιλογή πόλης
        JSONObject main=(JSONObject)city.get("clouds"); // Επιλογή αντικειμένου νεφοκάλυψης
        int clouds=main.getInt("all"); // Ανάνγωση πεδίου ποσοστιαίας νεφοκάλυψης
               
        return clouds; // Επιστροφή δεδομένων στον χρήστη
    }
    
    // Εξόρυξη ταχύτητας ανέμου από το JSON string
    public double get_WindSpeed()throws JSONException{

        JSONObject obj=new JSONObject(this.json); // Δημιουργία νέου αντικειμένου JSON
        JSONArray list=(JSONArray)obj.get("list"); // Επιλογή λίστας δεδομένων
        JSONObject city=(JSONObject)list.get(0); // Επιλογή πόλης
        JSONObject main=(JSONObject)city.get("wind"); // Επιλογή αντικειμένου ανέμου
        double wind=main.getDouble("speed"); // Ανάνγωση πεδίου ταχύτητας του ανέμου
               
        return wind; // Επιστροφή δεδομένων στον χρήστη
    }
    
    // Εξόρυξη ημερομηνίας από το JSON string
    public String get_DateTime()throws JSONException{

        JSONObject obj=new JSONObject(this.json); // Δημιουργία νέου αντικειμένου JSON
        JSONArray list=(JSONArray)obj.get("list"); // Επιλογή λίστας δεδομένων
        JSONObject city=(JSONObject)list.get(0); // Επιλογή πόλης
        long dt=city.getLong("dt"); // Ανάγνωση σφραγίδας χρονοσήμανσης
        Calendar cal = Calendar.getInstance(); // Δημιουργία νέου ημερολογίου
        cal.setTimeInMillis(dt*1000); // Ρύθμιση του ημερολογίου σύμφωνα με τη χρονοσήμανση που λήφθηκε
        
        // Καθορισμός μορφής εμφανιζόμενης ημερομηνίας και ώρας
        String timeString = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z").format(cal.getTime());

        return timeString; // Επιστροφή δεδομένων στον χρήστη
    }
    
    // Εξόρυξη αναγνωριστικού πόλεως από το JSON string
    public long get_CityID()throws JSONException{

        JSONObject obj=new JSONObject(this.json); // Δημιουργία νέου αντικειμένου JSON
        JSONArray list=(JSONArray)obj.get("list"); // Επιλογή λίστας δεδομένων
        JSONObject city=(JSONObject)list.get(0); // Επιλογή πόλης
        long id=city.getLong("id"); // Ανάγνωση κωδικού αριθμού επιλεγμένης πόλης
               
        return id; // Επιστροφή δεδομένων στον χρήστη
    }
    
    // Εξόρυξη ύψους βροχής από το JSON string
    public double get_Rain()throws JSONException{

        JSONObject obj=new JSONObject(this.json); // Δημιουργία νέου αντικειμένου JSON
        JSONArray list=(JSONArray)obj.get("list"); // Επιλογή λίστας δεδομένων
        JSONObject city=(JSONObject)list.get(0); // Επιλογή πόλης
        JSONObject main=(JSONObject)city.get("rain"); // Επιλογή αντικειμένου βροχής
        double rain=main.getDouble("3h"); // Ανάνγωση πεδίου ύψους βροχής

        return rain; // Επιστροφή δεδομένων στον χρήστη
    }
    
    // Εξόρυξη ύψους χιονιού από το JSON string
    public double get_Snow()throws JSONException{

        JSONObject obj=new JSONObject(this.json); // Δημιουργία νέου αντικειμένου JSON
        JSONArray list=(JSONArray)obj.get("list"); // Επιλογή λίστας δεδομένων
        JSONObject city=(JSONObject)list.get(0); // Επιλογή πόλης
        JSONObject main=(JSONObject)city.get("snow"); // Επιλογή αντικειμένου χιονιού
        double snow=main.getDouble("3h"); // Ανάνγωση πεδίου ύψους χιονιού
               
        return snow; // Επιστροφή δεδομένων στον χρήστη
    }
}
