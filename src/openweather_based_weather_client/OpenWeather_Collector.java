/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package openweather_based_weather_client;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;
/**
 *
 * @author tafra
 */
public class OpenWeather_Collector {
    
    public String get_forecast(String apiid,String cityid ){
        
        String forecast="api.openweathermap.org/data/2.5/forecast?id="+apiid+"&APPID="+cityid;
        return httpGET(forecast);
    }
    
}
