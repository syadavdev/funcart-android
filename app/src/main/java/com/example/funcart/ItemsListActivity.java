package com.example.funcart;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ListView;

import com.example.funcart.dataClass.ItemData;
import com.example.funcart.adapter.ItemsListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class ItemsListActivity extends AppCompatActivity {

    ArrayList<ItemData> itemDataList;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_list);
        itemDataList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.listView);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new ReadJSON().execute("http://ec2-35-154-75-22.ap-south-1.compute.amazonaws.com/funcart/items");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.items_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    class ReadJSON extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            return readURL(params[0],getIntent().getExtras().getString("token"),getIntent().getExtras().getString("secret"));
        }

        @Override
        protected void onPostExecute(String content) {
            try {
                JSONArray jsonArray =  new JSONArray(content);

                for(int i =0;i<jsonArray.length(); i++){
                    JSONObject itemObject = jsonArray.getJSONObject(i);
                    itemDataList.add(new ItemData(itemObject.getInt("itemId"),
                            itemObject.getString("name"),
                            itemObject.getString("picName"),
                            itemObject.getDouble("price")));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ItemsListAdapter adapter = new ItemsListAdapter(
                    getApplicationContext(), R.layout.customer_item_list_layout, itemDataList
            );
            lv.setAdapter(adapter);
        }
    }


    private static String readURL(String theUrl,String token,String secret) {

        StringBuilder content = new StringBuilder();
        try {
            // create a url object
            URL url = new URL(theUrl);
            // create a urlconnection object
            URLConnection urlConnection = url.openConnection();
            urlConnection.setReadTimeout(15000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setDoInput(true);
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestProperty("token",token);
            urlConnection.setRequestProperty("secret",secret);
            // wrap the urlconnection in a bufferedreader
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            // read from the urlconnection via the bufferedreader
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line + "\n");
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content.toString();
    }
}
