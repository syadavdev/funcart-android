package com.example.funcart;

import android.content.Intent;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.funcart.R;
import com.example.funcart.dataClass.cart.Cart;
import com.example.funcart.dataClass.cart.CartItem;
import com.example.funcart.dataClass.cart.CheckoutDto;
import com.example.funcart.helperClass.CartUtil;
import com.example.funcart.helperClass.CheckoutUtill;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CheckoutActivity extends AppCompatActivity implements View.OnClickListener {

    CheckoutDto checkoutDto;

    Button createOrder;
    EditText shipping, billing;
    RadioGroup radioGroup;
    RadioButton byCash, byCard,radioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        shipping = (EditText) findViewById(R.id.ShippingAddress);
        billing = (EditText) findViewById(R.id.billingAddress);
        radioGroup = (RadioGroup) findViewById(R.id.checkoutRadioGroup);
        byCash = (RadioButton) findViewById(R.id.byCash);
        byCard = (RadioButton) findViewById(R.id.byCard);

        createOrder = (Button) findViewById(R.id.nextToOrder);
        createOrder.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (!shipping.getText().toString().isEmpty() && !billing.getText().toString().isEmpty()) {
            Intent intent = new Intent(this, OrderActivity.class);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    checkoutDto = new CheckoutDto();

                    checkoutDto.setBillingaddress(billing.getText().toString());
                    checkoutDto.setShippingaddress(shipping.getText().toString());

                    int selectedId =radioGroup.getCheckedRadioButtonId();
                    radioButton = (RadioButton)findViewById(selectedId);

                    checkoutDto.setPaymentBy(radioButton.getText().toString());
                    new Checkout().execute("http://ec2-35-154-75-22.ap-south-1.compute.amazonaws.com/funcart/checkout");
                }
            });

            intent.putExtra("paymentBy",radioButton.getText().toString());
            intent.putExtra("token", getIntent().getExtras().getString("token"));
            intent.putExtra("secret", getIntent().getExtras().getString("secret"));
            startActivity(intent);
        }else{
            shipping.setError("Enter Details Properly");
        }
    }

    class Checkout extends AsyncTask<String, Integer, String> {

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

            checkoutDto.setEmail(email);
            return CheckoutUtill.checkOut(params[0], getIntent().getExtras().getString("token"),
                    getIntent().getExtras().getString("secret"),checkoutDto);
        }

        @Override
        protected void onPostExecute(String content) {
            if (content.contains("Error In Checkout : "))
                Toast.makeText(getApplicationContext(), content, Toast.LENGTH_SHORT);
        }
    }
}
