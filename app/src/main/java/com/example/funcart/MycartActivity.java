package com.example.funcart;

<<<<<<< HEAD
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

import com.example.funcart.dataClass.CartItemData;
import com.example.funcart.dataClass.cart.Cart;
import com.example.funcart.dataClass.cart.CartItem;
import com.example.funcart.dataClass.cart.UpdateCart;
import com.example.funcart.helperClass.CartUtil;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MycartActivity extends AppCompatActivity implements View.OnClickListener {

    Cart cartData;
    Button checkout;
    ListView cartListView;
    List<CartItem> cartItemList;
    UpdateCart updateCart;

    int index;
=======
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

public class MycartActivity extends AppCompatActivity {

      Toolbar tblMyCart;
      ImageView picture;
>>>>>>> 01223937ad61fd0a5bb832a6882181420c5135f5

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycart);

<<<<<<< HEAD
        checkout = (Button) findViewById(R.id.checkout);
        checkout.setOnClickListener(this);
        cartListView = (ListView) findViewById(R.id.cartlistView);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateCartItems("ec2-35-154-75-22.ap-south-1.compute.amazonaws.com/funcart/updateCart");
                new GettingCart().execute("ec2-35-154-75-22.ap-south-1.compute.amazonaws.com/funcart/getCart");
            }
        });

    }

    @Override
    public void onClick(View view) {

    }

    //update customer cart
    public void updateCartItems(String url) {
        if (!getIntent().getExtras().getString("updateCart").toString().isEmpty() && getIntent().getExtras().getString("updateCart").toString() != null) {
            CartUtil.updateCartItems(url, getIntent().getExtras().getString("token"),
                    getIntent().getExtras().getString("secret"),
                    getIntent().getExtras().getString("updateCart").toString());
        }
    }

    //getting items of Cart list
    class GettingCart extends AsyncTask<String, Integer, String> {

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
            Gson gson = new Gson();
            Cart cart = gson.fromJson(content,Cart.class);
            if(!cart.getItemDtoList().isEmpty()) {

                MycartActivity.CartItemsAdapter adapter = new CartItemsAdapter(
                        getApplicationContext(),R.layout.cart_items_adapter, (ArrayList<CartItem>) cart.getItemDtoList()
                );

                cartListView.setAdapter(adapter);
            }else{
                Toast.makeText(getApplicationContext(),content,Toast.LENGTH_SHORT);
            }
        }
    }

    //Adapter for main CartItems
    public class CartItemsAdapter extends ArrayAdapter<CartItemData> {
        ArrayList<CartItem> cartItems;
        Context context;
        int resource;
        private final String imageUrl = "http://ec2-35-154-75-22.ap-south-1.compute.amazonaws.com/images/";

        public CartItemsAdapter(Context context, int resource, ArrayList<CartItem> cartItems) {
            super(context, resource, cartItems);
            this.cartItems = cartItems;
            this.context = context;
            this.resource = resource;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                LayoutInflater layoutInflater = (LayoutInflater) getContext()
                        .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.cart_items_adapter, null, true);

            }

            CartItemData cartItemData = getItem(position);

            ImageView imageView = (ImageView) convertView.findViewById(R.id.cartItemImage);
            Picasso.with(context).load(imageUrl + cartItemData.getPicName()).into(imageView);

            TextView txtName = (TextView) convertView.findViewById(R.id.cartItemName);
            txtName.setText(cartItemData.getName());

            TextView txtQuantity = (TextView) convertView.findViewById(R.id.cartItemQty);
            txtQuantity.setText("Quantity : "+cartItemData.getItemQty());

            TextView txtPrice = (TextView) convertView.findViewById(R.id.cartItemTotalPrice);
            txtPrice.setText("Totol price : "+Double.toString(cartItemData.getTotalPrice()) + " ₹");

            Button removeItem = (Button) convertView.findViewById(R.id.removeFromCart);
            removeItem.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    System.out.println("ITem removed");
                }
            });
            return convertView;
        }
=======
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){

        }



>>>>>>> 01223937ad61fd0a5bb832a6882181420c5135f5
    }

    public void remove(int position){

    }
}
