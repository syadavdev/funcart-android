package com.example.funcart;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.funcart.DataClass.ItemData;
import com.example.funcart.helperClass.CustomerUtil;
import com.example.funcart.helperClass.ItemsUtil;
import com.example.funcart.requestClass.LoginPost;
import com.example.funcart.requestClass.SignUpPost;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemsList extends AppCompatActivity {

    private Toolbar toolbar;
    private int count = 0;
    private TextView textView;
    private final String itemUrl = "http://ec2-35-154-75-22.ap-south-1.compute.amazonaws.com/funcart/items";
    private final String imageUrl = "http://ec2-35-154-75-22.ap-south-1.compute.amazonaws.com/images/";
    private List<ItemData> itemsList;
    private ItemsUtil itemsUtil;
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_list);
        imageView = (ImageView) findViewById(R.id.image);


        Picasso.with(this)
                .load(imageUrl+itemsList.get(0).getPicName())
                .error(R.mipmap.ic_launcher)
                .resize(250,250)
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        Log.d("TAG", "onSuccess");
                    }

                    @Override
                    public void onError() {Toast.makeText(getApplicationContext(), "An error occurred", Toast.LENGTH_SHORT).show();
                    }
                });

        new ItemsList.ItemsTask().execute();
    }

    @Override
    protected void onStart(){
        super.onStart();
        for(int i = 0;i < itemsList.size();i++) {
            Picasso.with(this)
                    .load(imageUrl+itemsList.get(i).getPicName())
                    .error(R.mipmap.ic_launcher)
                    .resize(250,250)
                    .into(imageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            Log.d("TAG", "onSuccess");
                        }

                        @Override
                        public void onError() {Toast.makeText(getApplicationContext(), "An error occurred", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater later = getMenuInflater();
        later.inflate(R.menu.menu_bar,menu);
        MenuItem item = menu.findItem(R.id.Action_Cart);
        return super.onCreateOptionsMenu(menu);
    }*/

    /**
     * Gets the state of Airplane Mode.
     *
     * @param context
     * @return true if enabled.
     */
    @SuppressWarnings("deprecation")
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static boolean isAirplaneModeOn(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return Settings.System.getInt(context.getContentResolver(),
                    Settings.System.AIRPLANE_MODE_ON, 0) != 0;
        } else {
            return Settings.Global.getInt(context.getContentResolver(),
                    Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
        }
    }

    private class ItemsTask extends AsyncTask<Void,Void,Void> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(ItemsList.this);
            dialog.setMessage("please wait .....");
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... param) {
            itemsList = new ArrayList<ItemData>();
            itemsUtil = new ItemsUtil();
            itemsUtil.getItems(itemUrl,getIntent().getExtras().getString("token"),
                    getIntent().getExtras().getString("secret"));

            return null;
        }

        @Override
        protected void onPostExecute(Void param) {
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

            /*if(!isAirplaneModeOn(getApplicationContext())){
                //Picasso Code
                Picasso.with(getApplicationContext())
                        .load("https://www.dropbox.com/s/w14riop1qi8vdms/motogplay.jpg?raw=1")
                        .resize(250, 250)
                        .into(imageView, new Callback() {
                            @Override
                            public void onSuccess() {
                                Log.e("profilepicsucess", "");
                            }

                            @Override
                            public void onError() {
                                Log.e("profilepicfalse :3", "");
                            }
                        });
            }else{
                System.out.println("Picaso again Fails");
            }*/
        }
    }
}
