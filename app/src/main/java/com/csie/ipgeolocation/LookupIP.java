package com.csie.ipgeolocation;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LookupIP extends AsyncTask<URL,Void,String> {

    private String result="";
    IP ip = new IP();

    @Override
    protected String doInBackground(URL... urls) {
        try {
            HttpURLConnection connection = (HttpURLConnection)urls[0].openConnection();
            connection.setRequestMethod("GET");

            InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            while((line=bufferedReader.readLine())!=null){
                result = line;
            }

            connection.disconnect();

            ParseJSON(result);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public void ParseJSON(String result){
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONObject json_Location=jsonObject.getJSONObject("location");
            ip.setIpAddress(jsonObject.getString("ip"));
            ip.setType(jsonObject.getString("type"));
            ip.setContinent_Name(jsonObject.getString("continent_name"));
            ip.setCountry_Name(jsonObject.getString("country_name"));
            ip.setCountry_Code(jsonObject.getString("country_code"));
            ip.setRegion_Name(jsonObject.getString("region_name"));
            ip.setCity(jsonObject.getString("city"));
            ip.setZip(jsonObject.getString("zip"));
            ip.setLatitude(jsonObject.getDouble("latitude"));
            ip.setLongitude(jsonObject.getDouble("longitude"));
            ip.setCountry_FlagEmoji(json_Location.getString("country_flag_emoji"));
            ip.setIs_Eu(json_Location.getBoolean("is_eu"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
