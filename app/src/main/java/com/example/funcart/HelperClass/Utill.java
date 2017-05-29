package com.example.funcart.HelperClass;

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

public class Utill  {

      static  String response = null;
      public Utill() {
      }

      public Map<String,String> makeWebServiceCall(String urladdress, HashMap<String,String> params) {
            Map<String, String> result = new HashMap<>();
          URL url;
          String response = " ";
          try {
              url = new URL(urladdress);
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
                 StringBuilder requestresult =new StringBuilder();
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

              int reqresponseCode = connection.getResponseCode();
              String line;
              if(reqresponseCode >= 200 && reqresponseCode <= 210){
                  BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                  while ((line = reader.readLine()) != null) {
                      response += line;
                  }
              }else{
                  BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                  while ((line = reader.readLine()) != null) {
                      response += line;
                  }
              }

              result.put(String.valueOf(reqresponseCode), response);
          } catch(JSONException e){
              e.printStackTrace();
          }
          catch (UnsupportedEncodingException e) {
              e.printStackTrace();
          } catch (ProtocolException e) {
              e.printStackTrace();
          } catch (MalformedURLException e) {
              e.printStackTrace();
          } catch (IOException e) {
              e.printStackTrace();
          }
            return result;
      }
}