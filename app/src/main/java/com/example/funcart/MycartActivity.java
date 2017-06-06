package com.example.funcart;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
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

public class MycartActivity extends AppCompatActivity implements View.OnClickListener{

    Cart cartData;
    List<CartItem> cartItemList;

    int resource;
    private final String imageUrl = "http://ec2-35-154-75-22.ap-south-1.compute.amazonaws.com/images/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycart);

    }

    @Override
    public void onClick(View view){

    }

    //getting items of main list
    class updateCartItems extends AsyncTask<String, Integer, String> {

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
                ItemsListActivity.ItemsListAdapter adapter = new ItemsListActivity.ItemsListAdapter(
                        getApplicationContext(), R.layout.items_list_adapter, itemDataList
                );
                itemsList.setAdapter(adapter);
            }else{
                Toast.makeText(getApplicationContext(),content,Toast.LENGTH_SHORT);
            }
        }
    }

    //Adapter for main itemList
    public CartItemsAdapter(Context context, int resource, ArrayList<ItemData> itemDataList) {
        super(context, resource, itemDataList);
        this.cartDataList = itemDataList;
        this.context = context;
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) getContext()
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.cart_items_adapter, null, true);

        }

        ItemData itemData = getItem(position);

        ImageView imageView = (ImageView) convertView.findViewById(R.id.cart);
        Picasso.with(context).load(imageUrl+itemData.getPicName()).into(imageView);

        TextView txtName = (TextView) convertView.findViewById(R.id.ItemName);
        txtName.setText(itemData.getName());

        TextView txtPrice = (TextView) convertView.findViewById(R.id.ItemPrice);
        txtPrice.setText(Double.toString(itemData.getPrice())+" ₹");

        return convertView;
    }

    //Adding items to cart
    public void addingItemsToCart(int i){
        selectItemList = itemDataList.get(i);
        count++;
        if(count <= 20) {
            cartButton.setText(Integer.toString(count));
            List<CartItem> cartItemsList = cartItems.getItemDtoList();

            int quantity = 0,index = 0;
            for(CartItem cartItem : cartItemsList){
                if(cartItem.getItemId() == selectItemList.getItemId()){
                    quantity = updateCart.getUpdateCartItem().get(index).getItemQty();
                    quantity += 1;
                    updateCart.getUpdateCartItem().get(index).setItemQty(quantity);
                }
                index++;
            }
            cartItems.getItemDtoList();
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

            return CartUtil.readCartItems(params[0],getIntent().getExtras().getString("token"),
                    getIntent().getExtras().getString("secret"),email);
        }

        @Override
        protected void onPostExecute(String content){
            updateCart = new UpdateCart();

            count = 0;
            Gson gson = new Gson();
            CartItem cartItem;
            UpdateCartItem updateCartItem;
            if(!content.contains("Your cart is empty")) {
                cartItems = gson.fromJson(content, Cart.class);

                for(int i = 0;i < cartItems.getItemDtoList().size();i++) {
                    cartItem = cartItems.getItemDtoList().get(i);

                    updateCartItem = new UpdateCartItem();
                    updateCartItem.setItemQty(cartItem.getItemQty());
                    updateCartItem.setItemId(cartItem.getItemId());

                    updateCart.getUpdateCartItem().add(updateCartItem);
                    count += cartItems.getItemDtoList().get(i).getItemQty();
                }
            }

            cartButton.setText(Integer.toString(count));
            updateCart.setEmail(cartItems.getEmail());
        }
    }
}
