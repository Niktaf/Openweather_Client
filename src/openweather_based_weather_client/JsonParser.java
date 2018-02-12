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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class JsonParser {
   private String json; //json string

    //Constructor
    public JsonParser(String json){
        this.json=json;
    }

 
    public double get_temp()throws JSONException{

        JSONObject obj=new JSONObject(this.json);
        JSONArray list=(JSONArray)obj.get("list");
        JSONObject city=(JSONObject)list.get(0);
        JSONObject main=(JSONObject)city.get("main");
        double temp=(double)main.get("temp");
        
        return temp;

    }
}
