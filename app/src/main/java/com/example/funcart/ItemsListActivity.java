package com.example.funcart;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.example.funcart.dataClass.cart.Cart;
import com.example.funcart.dataClass.cart.CartItem;
import com.example.funcart.dataClass.cart.UpdateCart;
import com.example.funcart.dataClass.cart.UpdateCartItem;
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
import java.util.List;

import static java.security.AccessController.getContext;

public class ItemsListActivity extends AppCompatActivity implements View.OnClickListener{

    ArrayList<ItemData> mainItemsList;
    List<ItemData> selectItemList = new ArrayList<ItemData>();

    Cart itemsAlreadyInCart = new Cart();
    UpdateCart updateCart = new UpdateCart();

    ListView itemsList;
    Button cartButton;

    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_list);
        mainItemsList = new ArrayList<>();

        itemsList = (ListView) findViewById(R.id.listView);
        cartButton = (Button) findViewById(R.id.cartItemsButton);
        cartButton.setOnClickListener(this);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new GetCartItems().execute("http://ec2-35-154-75-22.ap-south-1.compute.amazonaws.com/funcart/getCart");
                new GetItemsJson().execute("http://ec2-35-154-75-22.ap-south-1.compute.amazonaws.com/funcart/items");
            }
        });
    }

    @Override
    public void onClick(View view){
        boolean flag;
        int quantity;
        UpdateCartItem updateCartItemObj;

        Intent intent = new Intent(this,MycartActivity.class);
        Gson gson = new Gson();

        if(!itemsAlreadyInCart.getItemDtoList().isEmpty()) {
            if (!selectItemList.isEmpty() && selectItemList != null) {
                for (int i = 0; i < selectItemList.size(); i++) {
                    flag = false;
                    for (int j = 0; j < itemsAlreadyInCart.getItemDtoList().size(); j++) {
                        if (flag = (selectItemList.get(i).getItemId()) == updateCart.getUpdateCartItem().get(j).getItemId()) {

                            quantity = updateCart.getUpdateCartItem().get(j).getItemQty();
                            ++quantity;
                            updateCart.getUpdateCartItem().get(j).setItemQty(quantity);

                            quantity = 0;
                            break;
                        }
                    }
                    if (!flag) {
                        updateCartItemObj = new UpdateCartItem();

                        updateCartItemObj.setItemId(selectItemList.get(i).getItemId());
                        updateCartItemObj.setItemQty(1);

                        updateCart.getUpdateCartItem().add(updateCartItemObj);
                    }
                    String updateJson = gson.toJson(updateCart);
                    intent.putExtra("updateCart", updateJson);
                }
            }
        }else{
            if (!selectItemList.isEmpty() && selectItemList != null) {
                updateCart.setUpdateCartItem(new ArrayList<UpdateCartItem>());
                for (int i = 0; i < selectItemList.size(); i++) {
                    flag = false;
                    for (int j = 0; j < updateCart.getUpdateCartItem().size(); j++) {
                        if (flag = (selectItemList.get(i).getItemId()) == updateCart.getUpdateCartItem().get(j).getItemId()) {

                            quantity = updateCart.getUpdateCartItem().get(j).getItemQty();
                            ++quantity;
                            updateCart.getUpdateCartItem().get(j).setItemQty(quantity);

                            quantity = 0;
                            break;
                        }
                    }
                    if (!flag) {
                        updateCartItemObj = new UpdateCartItem();

                        updateCartItemObj.setItemId(selectItemList.get(i).getItemId());
                        updateCartItemObj.setItemQty(1);

                        updateCart.getUpdateCartItem().add(updateCartItemObj);
                    }
                }
                String updateJson = gson.toJson(updateCart);
                intent.putExtra("updateCart", updateJson);
            }
            else{
                Toast.makeText(getApplicationContext(),"Your cart is Empty",Toast.LENGTH_SHORT);
            }
        }
        intent.putExtra("token",getIntent().getExtras().getString("token"));
        intent.putExtra("secret",getIntent().getExtras().getString("secret"));
        startActivity(intent);
    }

    //Adding items to cart
    public void selectItemsFromList(int i){
        count++;
        if(count <= 20) {
            selectItemList.add(mainItemsList.get(i));
            cartButton.setText(Integer.toString(count));
        }
        else{
            Toast.makeText(getApplicationContext(),"Max 20 Items Allowed To Cart",Toast.LENGTH_LONG);
        }
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

            updateCart.setEmail(email);
            return CartUtil.readCartItems(params[0],getIntent().getExtras().getString("token"),
                    getIntent().getExtras().getString("secret"),email);
        }

        @Override
        protected void onPostExecute(String content){
            count = 0;
            Gson gson = new Gson();
            CartItem cartItem;
            UpdateCartItem updateCartItem;
            itemsAlreadyInCart.setItemDtoList(new ArrayList<CartItem>());

            if(!content.contains("Your cart is empty")) {

                itemsAlreadyInCart = gson.fromJson(content, Cart.class);
                List<UpdateCartItem> updateItemsListObj = new ArrayList<UpdateCartItem>();

                for(int i = 0; i < itemsAlreadyInCart.getItemDtoList().size(); i++) {
                    cartItem = itemsAlreadyInCart.getItemDtoList().get(i);

                    updateCartItem = new UpdateCartItem();
                    updateCartItem.setItemQty(cartItem.getItemQty());
                    updateCartItem.setItemId(cartItem.getItemId());


                    updateItemsListObj.add(updateCartItem);
                    count += itemsAlreadyInCart.getItemDtoList().get(i).getItemQty();
                }
                updateCart.setUpdateCartItem(updateItemsListObj);
            }

            cartButton.setText(Integer.toString(count));
        }
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
                        mainItemsList.add(new ItemData(itemObject.getInt("itemId"),
                                itemObject.getString("name"),
                                itemObject.getString("picName"),
                                itemObject.getDouble("price")));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ItemsListAdapter adapter = new ItemsListAdapter(
                        getApplicationContext(), R.layout.items_list_adapter, mainItemsList
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

            Button addToCart = (Button) convertView.findViewById(R.id.addToCart);
            addToCart.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    selectItemsFromList(position);
                }
            });

            return convertView;
        }
    }
}