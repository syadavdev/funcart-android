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

import com.example.funcart.dataClass.cart.Cart;
import com.example.funcart.dataClass.cart.CartItem;
import com.example.funcart.helperClass.CartUtil;
import com.example.funcart.helperClass.OrderUtil;
import com.example.funcart.order.OrderCustomerDto;
import com.example.funcart.order.OrderDto;
import com.example.funcart.order.OrderItemDto;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OrderActivity extends AppCompatActivity {

    Cart cart;

    OrderDto orderDto;

    ListView orderListView;
    TextView username,phonenumber,email,shipping,billing,paymentMode,orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        username = (TextView) findViewById(R.id.orderUserName);
        phonenumber = (TextView) findViewById(R.id.orderPhonenumber);
        email = (TextView) findViewById(R.id.orderEmail);
        shipping = (TextView) findViewById(R.id.orderShipping);
        billing = (TextView) findViewById(R.id.orderBilling);
        paymentMode = (TextView) findViewById(R.id.orderPayment);
        orderId = (TextView) findViewById(R.id.orderId);

        orderListView = (ListView) findViewById(R.id.orderItemsList);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new GettingCart().execute("http://ec2-35-154-75-22.ap-south-1.compute.amazonaws.com/funcart/getCart");
                new CreateOrder().execute("http://ec2-35-154-75-22.ap-south-1.compute.amazonaws.com/funcart/createOrder");
            }
        });
    }

    class CreateOrder extends AsyncTask<String, Integer, String> {

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

            return OrderUtil.getOrderItems(params[0],getIntent().getExtras().getString("token")
                    ,getIntent().getExtras().getString("secret"),email);
        }

        @Override
        protected void onPostExecute(String content) {
            if(!content.contains("Error in Creating Order : ")) {
                Gson gson = new Gson();
                orderDto = gson.fromJson(content,OrderDto.class);

                orderId.setText("Order Id : "+orderDto.getOrderId());
                paymentMode.setText("Payment By : "+getIntent().getExtras().getString("paymentBy"));
                username.setText("Customer Name : "+orderDto.getOrdercustomerDtoList().getName());
                phonenumber.setText("Phonenumber : "+Long.toString(orderDto.getOrdercustomerDtoList().getPhoneNumber()));
                email.setText("Email : "+orderDto.getEmail());
                shipping.setText("Ship To : "+orderDto.getOrdercustomerDtoList().getShippingAddress());
                billing.setText("Bill To : "+orderDto.getOrdercustomerDtoList().getBillingaddress());


            }else{
                Toast.makeText(getApplicationContext(),content,Toast.LENGTH_SHORT);
            }
        }
    }

    //getting cart items
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
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return CartUtil.readCartItems(params[0], getIntent().getExtras().getString("token"),
                    getIntent().getExtras().getString("secret"), email);
        }

        @Override
        protected void onPostExecute(String content) {
            Gson gson = new Gson();
            cart = gson.fromJson(content, Cart.class);
            if (!cart.getItemDtoList().isEmpty() && cart.getItemDtoList() != null) {

                OrderActivity.CartItemsAdapter adapter = new CartItemsAdapter(
                        getApplicationContext(), R.layout.activity_order_adpater, (ArrayList<CartItem>) cart.getItemDtoList()
                );

                orderListView.setAdapter(adapter);
            } else {
                Toast.makeText(getApplicationContext(), content, Toast.LENGTH_SHORT);
            }
        }
    }

    //Adapter for main OrderItems
    public class CartItemsAdapter extends ArrayAdapter<CartItem> {
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
                convertView = layoutInflater.inflate(R.layout.activity_order_adpater, null, true);

            }

            CartItem cartItemData = getItem(position);

            ImageView imageView = (ImageView) convertView.findViewById(R.id.orderItemsImages);
            Picasso.with(context).load(imageUrl + cartItemData.getItemPicName()).into(imageView);

            TextView txtName = (TextView) convertView.findViewById(R.id.orderItemName);
            txtName.setText(cartItemData.getItemName());

            TextView txtQty = (TextView) convertView.findViewById(R.id.orderItemQty);
            txtQty.setText("Qty : "+Integer.toString(cartItemData.getItemQty()));

            TextView txtPrice = (TextView) convertView.findViewById(R.id.orderItemPrice);
            txtPrice.setText("Totol price : " + Double.toString(cartItemData.getItemTotalPrice()) + " ₹");

            return convertView;
        }
    }

}
