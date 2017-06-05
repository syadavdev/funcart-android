package com.example.funcart.helperClass;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by AnandSharma on 5/2/2017.
 */

public class CustomerUtil {

    private JSONObject result = null;

    public JSONObject makeWebServiceCall(String urladdress, HashMap<String,String> params) {

        try {
            URL url = new URL(urladdress);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(15000);
            connection.setConnectTimeout(15000);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json");

            if (params != null) {
                OutputStream ostream = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(ostream,"UTF-8"));
                JSONObject jobj = new JSONObject();
                for (Map.Entry<String, String> entry : params.entrySet()) {

                    if(entry.getKey().equalsIgnoreCase("phonenumber")){
                        jobj.put(entry.getKey(), Long.valueOf(entry.getValue()));
                    }
                    else{
                        jobj.put(entry.getKey(), entry.getValue());
                    }
                }
                writer.write(jobj.toString());
                writer.flush();
                writer.close();
                ostream.close();
            }

            int reqResponseCode = connection.getResponseCode();
            String line;
            String response = "";
            BufferedReader reader;

            if(reqResponseCode == 200 || reqResponseCode == 201) {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while ((line = reader.readLine()) != null) {
                    response += line;
                }
                if(!response.isEmpty() && response != null) {
                    result = new JSONObject(response);
                    if(urladdress.contains("login")){
                        result.put("token", connection.getHeaderField("token"));
                        result.put("secret", connection.getHeaderField("secret"));
                    }
                    result.put("responseCode",reqResponseCode);
                }else{
                    result = null;
                }
            }else{
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                while ((line = reader.readLine()) != null) {
                    response += line;
                }
                if(!response.isEmpty() && response != null) {
                    result = new JSONObject(response);
                    result.put("responseCode",reqResponseCode);
                }else{
                    result = null;
                }
            }


        }catch(JSONException e){
            e.printStackTrace();
        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }catch (ProtocolException e) {
            e.printStackTrace();
        }catch (MalformedURLException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}