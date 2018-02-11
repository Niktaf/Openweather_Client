/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package openweather_based_weather_client;

/**
 *
 * @author tafra
 */
public class Openweather_Based_Weather_Client {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
         String  apiid ="fd798713a90f9501121e8dc78d7d0a47";
         String  cityid="733776";
        OpenWeather_Collector oc =new OpenWeather_Collector();
       // oc.get_forecast(apiid, cityid);
       //Αυτό είναι δοκιμαστικό για να δω αν δουλεύει η μέθοδος. Μου βγάζει έτσι όπως είναι Error: no protocol: . Αν κάνω επικόλληση το στρινγκ
       //σε browser μου στέλνει κανονικά τα στοιχεία
       oc.httpGET( "api.openweathermap.org/data/2.5/group?id=735914,734077,264371&units=metric&appid=fd798713a90f9501121e8dc78d7d0a47");
    }
    
}
