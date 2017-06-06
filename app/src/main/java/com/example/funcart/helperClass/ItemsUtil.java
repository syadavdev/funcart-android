package com.example.funcart.helperClass;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by mario on 03/06/17.
 */

public class ItemsUtil{

    public static String readItems(String theUrl,String token,String secret) {

        StringBuilder content = new StringBuilder();
        int responseCode = 401;
        try {

            URL url = new URL(theUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(15000);
            connection.setConnectTimeout(15000);
            connection.setDoInput(true);
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("token",token);
            connection.setRequestProperty("secret",secret);

            // wrap the urlconnection in a bufferedreader
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            // read from the urlconnection via the bufferedreader
            responseCode = connection.getResponseCode();

            while ((line = bufferedReader.readLine()) != null) {
                content.append(line + "\n");
            }
            bufferedReader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        if(responseCode != 200){
            return "Items loading Error" + content.toString();
        }else{
            return content.toString();
        }
    }
}
