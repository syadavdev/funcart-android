package com.example.funcart;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.funcart.dataClass.ItemData;
import com.example.funcart.dataClass.cart.CartDto;
import com.example.funcart.dataClass.cart.CartItemDto;
import com.example.funcart.dataClass.cart.UpdateCartDto;
import com.example.funcart.helperClass.CartUtil;
import com.example.funcart.helperClass.ItemsUtil;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ItemsListActivity extends AppCompatActivity implements View.OnClickListener{

    ArrayList<ItemData> itemDataList,itemSelected = new ArrayList<ItemData>();
    CartDto cartItems;
    UpdateCartDto updateCartitems;
    ListView itemsList;
    Button cartButton,addToCart;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_list);
        itemDataList = new ArrayList<>();

        itemsList = (ListView) findViewById(R.id.listView);
        cartButton = (Button) findViewById(R.id.cartItemsButton);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new GetCartItems().execute("http://ec2-35-154-75-22.ap-south-1.compute.amazonaws.com/funcart/getCart");
                new GetItemsJson().execute("http://ec2-35-154-75-22.ap-south-1.compute.amazonaws.com/funcart/items");
            }
        });
    }

    @Override
    public void onClick(View view) {
        count++;
        cartButton.setText(count);
        /*Intent intent = new Intent(ItemsListActivity.this, MycartActivity.class);
        if(!updateCartItems.isEmpty()) {
            intent.putExtra("updateCartItems", updateCartItems);
        }*/
    }

    //getting items of main list
    class GetItemsJson extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            return ItemsUtil.readItems(params[0],getIntent().getExtras().getString("token")
                    ,getIntent().getExtras().getString("secret"));
        }

        @Override
        protected void onPostExecute(String content) {
            if(!content.contains("Items loading Error")) {
                try {
                    JSONArray jsonArray = new JSONArray(content);

                    for (int i = 0; i < jsonArray.length(); i++) {
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
                        getApplicationContext(), R.layout.items_list_adapter, itemDataList
                );
                itemsList.setAdapter(adapter);
            }else{
                Toast.makeText(getApplicationContext(),content,Toast.LENGTH_SHORT);
            }
        }
    }

    //Adapter for main itemList
    public class ItemsListAdapter extends ArrayAdapter<ItemData> {

        ArrayList<ItemData> itemDataArrayList;
        Context context;
        int resource;
        private final String imageUrl = "http://ec2-35-154-75-22.ap-south-1.compute.amazonaws.com/images/";

        public ItemsListAdapter(Context context, int resource, ArrayList<ItemData> itemDataList) {
            super(context, resource, itemDataList);
            this.itemDataArrayList = itemDataList;
            this.context = context;
            this.resource = resource;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            if (convertView == null){
                LayoutInflater layoutInflater = (LayoutInflater) getContext()
                        .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.items_list_adapter, null, true);

            }
            ItemData itemData = getItem(position);

            ImageView imageView = (ImageView) convertView.findViewById(R.id.imageViewProduct);
            Picasso.with(context).load(imageUrl+itemData.getPicName()).into(imageView);

            TextView txtName = (TextView) convertView.findViewById(R.id.ItemName);
            txtName.setText(itemData.getName());

            TextView txtPrice = (TextView) convertView.findViewById(R.id.ItemPrice);
            txtPrice.setText(Double.toString(itemData.getPrice())+" ₹");

            Button button = (Button) convertView.findViewById(R.id.addToCart);
            button.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    addingITemsToCart(position);
                }
            });

            return convertView;
        }
    }

    //Adding items to cart
    public void addingITemsToCart(int i){
        itemSelected.add(itemDataList.get(i));
        count++;
        if(count < 20)
            cartButton.setText(Integer.toString(count));
        else{
            Toast.makeText(getApplicationContext(),"Max 20 Items Allowed To Cart",Toast.LENGTH_LONG);
        }
        System.out.print(itemDataList.get(i).getItemId());
    }

    //getting cart items of customer
    class GetCartItems extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            String email = null;
            String fileName = "/CustomerData.txt";
            StringBuilder customerDetails = new StringBuilder();
            try {
                File file = new File(getApplicationContext().getCacheDir().getAbsolutePath() + fileName);
                if (file.exists()) {
                    FileReader fileReader = new FileReader(file);
                    BufferedReader bReader = new BufferedReader(fileReader);

                    /** Reading the contents of the file , line by line */
                    String line = "";
                    while ((line = bReader.readLine()) != null) {
                        customerDetails.append(line + "\n");
                    }

                    JSONObject jsonObject = new JSONObject(customerDetails.toString());
                    email = jsonObject.getString("email");
                    fileReader.close();
                }
            }catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return CartUtil.readCartItems(params[0],getIntent().getExtras().getString("token"),
                    getIntent().getExtras().getString("secret"),email);
        }

        @Override
        protected void onPostExecute(String content) {
            cartItems = new CartDto();
            Gson gson = new Gson();
            if(!content.contains("Your cart is empty"))
                cartItems = gson.fromJson(content,CartDto.class);

            System.out.println(cartItems.getItemDtoList().get(0).getItemId());
        }
    }
}
