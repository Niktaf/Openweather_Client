/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package openweather_based_weather_client;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        // write your code here

        String forecast = "http://api.openweathermap.org/data/2.5/group?id=735914,734077,264371&units=metric&appid=fd798713a90f9501121e8dc78d7d0a47";
        OW_Collector ow = new OW_Collector();
        String prognosis = null;
        try {
            prognosis = ow.HttpGet(forecast);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(prognosis);
    }
}