package com.example.funcart;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.funcart.HelperClass.Utill;
import com.example.funcart.HelperClass.Validator;

import java.util.HashMap;
import java.util.Map;


public class Signup extends Activity  {

    Button signup,Loginsign;
    EditText name,phoneNumber,email,password;
    Validator validator;
    SignUpPost signUpost =  null;
    private String url =" http://ec2-35-154-75-22.ap-south-1.compute.amazonaws.com/funcart/signup";

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        /*Button using*/
        signup = (Button) findViewById(R.id.signup);

        //Define Edit Text
        name = (EditText) findViewById(R.id.username);
        phoneNumber =(EditText) findViewById(R.id.phoneNumber);
        email =(EditText) findViewById(R.id.email);
        password =(EditText) findViewById(R.id.password);

        /*set on listener*/
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignUpPost signUpost = new SignUpPost(name.getText().toString(),password.getText().toString(),email.getText().toString(),phoneNumber.getText().toString());
                //  register();
                if(name.getText().toString().trim().isEmpty()   ){
                    name.setError("enter name");
                } if(password.getText().toString().trim().isEmpty() ||password.length() <=8 ){
                    password.setError("password");
                }if(phoneNumber.getText().toString().trim().isEmpty() || phoneNumber.length() ==9 ){
                    phoneNumber.setError("phoneNumber");
                } if( email.getText().toString().trim().isEmpty()  || !Patterns.EMAIL_ADDRESS.matcher(signUpost.getEmail()).matches()){
                    email.setError(" enter Valid  email");
                }else {
                    new MyTask().execute(signUpost);
                }
            }
        });
    }

    private class MyTask extends AsyncTask<SignUpPost,Integer,Map<String,String>> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(Signup.this);
            dialog.setMessage("please wait .....");
            dialog.setCancelable(false);
            dialog.show();
        }
        @Override
        protected Map doInBackground(SignUpPost... params) {
            Utill utillObj = new Utill();
            SignUpPost temp = params[0];
            HashMap<String,String> signpost = new HashMap<String, String>();
            signpost.put("name",temp.getname());
            signpost.put("phoneNumber",temp.getPhoneNumber());
            signpost.put("email",temp.getEmail());
            signpost.put("password",temp.getPassword());
            Map<String, String> result = utillObj.makeWebServiceCall(url,signpost);
            return result;
        }
        @Override
        protected void onPostExecute(Map<String,String> result) {
            if(dialog.isShowing()) dialog.dismiss();
            if(result != null){
                if(result.containsKey("201")){
                    Toast.makeText(getApplicationContext(), result.get("201"), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Signup.this,ItemsList.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    for(Map.Entry<String,String> entry: result.entrySet()){
                        Toast.makeText(getApplicationContext(), entry.getValue(), Toast.LENGTH_LONG).show();
                    }
                }
            }else{
                Toast.makeText(getApplicationContext(), "Some unknown error occuured. Please try after some time.", Toast.LENGTH_LONG).show();
            }

        }
    }
}