/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package openweather_based_weather_client;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 * @author Παυλίδης Αριστείδης
 * @author Ταφραλίδης Νικόλαος
 * @author Τριανταφυλλίδης Τρύφων
 * ΘΕΣ-2 (2017-2018)
 */ 
public class OW_Collector {

   public String HttpGet(String url) throws IOException {


       URL request = new URL(url);
       HttpURLConnection con = (HttpURLConnection) request.openConnection();
       con.setRequestMethod("GET");
       con.setDoInput(true);
       con.setDoOutput(true);
       con.connect();

       
       if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
           StringBuffer sb = new StringBuffer();
           try (InputStream iS = con.getInputStream()) {
               BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(iS));
               String line=null;
               while ((line=bufferedReader.readLine()) !=null){
                   sb.append(line+ "\r\n");
                   iS.close();
                   con.disconnect();

                   return sb.toString();
               }
           }
       }
      return "error";

   }
}
