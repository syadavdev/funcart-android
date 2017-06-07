package com.example.funcart.helperClass;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by mario on 03/06/17.
 */

public class CartUtil {

    public static String readCartItems(String theUrl,String token,String secret,String email) {

        StringBuilder content = new StringBuilder();
        int responseCode = 401;

        try {
            // create a url object
            URL url = new URL(theUrl);
            // create a urlconnection object
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(15000);
            connection.setConnectTimeout(15000);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type","application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("token",token);
            connection.setRequestProperty("secret",secret);

            // wrap the urlconnection in a bufferedreader
            OutputStream ostream = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(ostream,"UTF-8"));
            JSONObject emailObj = new JSONObject();
            emailObj.put("email",email);
            writer.write(emailObj.toString());
            writer.flush();
            writer.close();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            responseCode = connection.getResponseCode();
            String line;
            // read from the urlconnection via the bufferedreader
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line + "\n");
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(responseCode != 200){
            return "Your cart is empty ," + content.toString();
        }else{
            return content.toString();
        }
    }

    public static String updateCartItems(String theUrl,String token,String secret,String updateCart) {

        StringBuilder content = new StringBuilder();
        int responseCode = 401;

        try {
            // create a url object
            URL url = new URL(theUrl);
            // create a urlconnection object
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(15000);
            connection.setConnectTimeout(15000);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type","application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("token",token);
            connection.setRequestProperty("secret",secret);

            // wrap the urlconnection in a bufferedreader
            OutputStream ostream = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(ostream,"UTF-8"));
            writer.write(updateCart);
            writer.flush();
            writer.close();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            responseCode = connection.getResponseCode();
            String line;
            // read from the urlconnection via the bufferedreader
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line + "\n");
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(responseCode != 200){
            return "Error in Updating Cart" + content.toString();
        }else{
            return content.toString();
        }
    }

}
