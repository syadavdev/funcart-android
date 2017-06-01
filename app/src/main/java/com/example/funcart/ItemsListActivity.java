package com.example.funcart;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.funcart.DataClass.ItemData;
import com.example.funcart.helperClass.ItemsUtil;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ItemsListActivity extends AppCompatActivity {

    private int count = 0;
    private final String itemUrl = "http://ec2-35-154-75-22.ap-south-1.compute.amazonaws.com/funcart/items";
    private List<ItemData> itemsList;
    private ItemsUtil itemsUtil;
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_list);
        lv = (ListView) findViewById(R.id.list_view);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new ItemsListActivity.ItemsTask().execute();
            }
        });
    }
    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater later = getMenuInflater();
        later.inflate(R.menu.menu_bar,menu);
        MenuItem item = menu.findItem(R.id.Action_Cart);
        return super.onCreateOptionsMenu(menu);
    }*/

    private class ItemsTask extends AsyncTask<Void,Void,Void> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(ItemsListActivity.this);
            dialog.setMessage("please wait .....");
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... param) {
            itemsList = new ArrayList<ItemData>();
            itemsUtil = new ItemsUtil();
            itemsUtil.getItems(itemUrl,getIntent().getExtras().getString("token"),
                    getIntent().getExtras().getString("secret"));
            try {
                if (itemsUtil.getResultArray() != null && !itemsUtil.getResultArray().toString().isEmpty()) {
                    if (itemsUtil.getResponseCode() == 201 || itemsUtil.getResponseCode() == 200) {
                        JSONArray resultArray = itemsUtil.getResultArray();
                        JSONObject jsonObj = null;
                        ItemData itemDataObj = null;
                        for (int i = 0; i < resultArray.length(); i++) {
                            jsonObj = resultArray.getJSONObject(i);

                            itemDataObj = new ItemData();

                            itemDataObj.setItemId(jsonObj.getInt("itemId"));
                            itemDataObj.setName(jsonObj.getString("name"));
                            itemDataObj.setPicName(jsonObj.getString("picName"));
                            itemDataObj.setPrice(jsonObj.getDouble("price"));
                        }
                        finish();
                    } else {
                        if (dialog.isShowing())
                            dialog.dismiss();
                        Toast.makeText(getApplicationContext(), itemsUtil.getResultArray().getString(0), Toast.LENGTH_LONG);
                        itemsList = null;
                    }
                }else{
                    if (dialog.isShowing())
                        dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Some unknown error occuured. Please try after some time or LOG IN again.", Toast.LENGTH_LONG).show();
                    itemsList = null;
                }
            }catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void param) {
            CustomListAdapter adapter = new CustomListAdapter(
                    getApplicationContext(), R.layout.custom_list_layout, (ArrayList<ItemData>) itemsList
            );
            lv.setAdapter(adapter);
        }
    }
}
