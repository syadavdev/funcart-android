package com.example.funcart.helperClass;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mario on 30/05/17.
 */

public class ItemsUtil{

    private JSONArray resultArray;
    private int responseCode;

    public void getItems(String urladdress, String token,String secret) {

        try {
            URL url = new URL(urladdress);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(15000);
            connection.setConnectTimeout(15000);
            connection.setDoInput(true);
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("token",token);
            connection.setRequestProperty("secret",secret);

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
                    resultArray = new JSONArray(response);
                    setResponseCode(reqResponseCode);
                }else{
                    resultArray = null;
                }
            }else{
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                while ((line = reader.readLine()) != null) {
                    response += line;
                }
                if(!response.isEmpty() && response != null) {
                    resultArray.put(response);
                    setResponseCode(reqResponseCode);
                }else{
                    resultArray = null;
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
    }
/*    public String getPicLink(String urladdress,String picName) {
        JSONObject result = null;
        String picLink = "";

        try {
            URL url = new URL(urladdress);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(15000);
            connection.setConnectTimeout(15000);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type","application/json");
            connection.setRequestProperty("Accept", "application/json");

            OutputStream ostream = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(ostream,"UTF-8"));

            JSONObject picNameObj = new JSONObject();
            picNameObj.put("picName",picName);
            writer.write(picNameObj.toString());

            writer.flush();
            writer.close();
            ostream.close();

            int reqResponseCode = connection.getResponseCode();
            String line;
            String response = "";
            BufferedReader reader;

            if(reqResponseCode == 200) {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while ((line = reader.readLine()) != null) {
                    response += line;
                }
                if(!response.isEmpty() && response != null) {
                    result = new JSONObject(response);
                    setResponseCode(reqResponseCode);
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
                    setResponseCode(reqResponseCode);
                }else{
                    result = null;
                }
            }
            picLink = result.getString("picLink");
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

        return picLink;
    }

    public Bitmap getImage(String picLink){
        Bitmap bmImg = null;
        URL imageUrl = null;

        try {
            imageUrl = new URL(picLink);
            InputStream is = new BufferedInputStream(imageUrl.openStream());
            bmImg = BitmapFactory.decodeStream(is);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bmImg;
    }*/

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public JSONArray getResultArray() {
        return resultArray;
    }

    public void setResultArray(JSONArray resultArray) {
        this.resultArray = resultArray;
    }
}
