package com.example.funcart;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.funcart.requestClass.SignUpPost;
import com.example.funcart.helperClass.CustomerUtil;
import com.example.funcart.helperClass.Validator;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;


public class SignupActivity extends Activity  {

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
                if(name.getText().toString().isEmpty()){
                    name.setError("enter name");
                }else if(phoneNumber.getText().toString().isEmpty()){
                    phoneNumber.setError("enter phoneNumber");
                }else if(email.getText().toString().isEmpty()){
                    email.setError(" enter email");
                }else if(password.getText().toString().isEmpty()){
                    password.setError("enter password");
                }else if(!Validator.nameValidate(name.getText().toString())){
                    name.setError("invalid name");
                }else if(!Validator.passwordValidate(password.getText().toString())){
                    password.setError("invalid password");
                }else if(!Validator.phoneNumberValidate(phoneNumber.getText().toString())){
                    phoneNumber.setError("invalid phoneNumber");
                }else if(!Validator.emailValidate(email.getText().toString())){
                    email.setError("invalid email");
                }else{
                    new MyTask().execute(signUpost);
                }
            }
        });
    }

    private class MyTask extends AsyncTask<SignUpPost,Integer,JSONObject> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(SignupActivity.this);
            dialog.setMessage("please wait .....");
            dialog.setCancelable(false);
            dialog.show();
        }
        @Override
        protected JSONObject doInBackground(SignUpPost... params) {
            CustomerUtil customerUtil = new CustomerUtil();
            SignUpPost temp = params[0];

            HashMap<String,String> signpost = new HashMap<String, String>();
            signpost.put("name",temp.getname());
            signpost.put("phoneNumber",temp.getPhoneNumber());
            signpost.put("email",temp.getEmail());
            signpost.put("password",temp.getPassword());
            JSONObject result = customerUtil.makeWebServiceCall(url,signpost);

            return result;
        }
        @Override
        protected void onPostExecute(JSONObject result) {
            if(dialog.isShowing())
                dialog.dismiss();

            try {
                if(result != null) {
                    if(result.getInt("responseCode") == 201 || result.getInt("responseCode") == 200) {
                        Toast.makeText(getApplicationContext(),"Account Created Please Login", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                        String fileName = "/SignupData.txt";
                        File file = new File(getApplicationContext().getCacheDir().getAbsolutePath() + fileName);
                        if(file.createNewFile()){
                            FileWriter fileWriter = new FileWriter(file);
                            result.remove("responseCode");
                            fileWriter.write(result.toString());
                            fileWriter.flush();
                            fileWriter.close();
                        }
                        startActivity(intent);
                        finish();
                    }else{
                        name.setError("errorMsg : "+result.getString("errorMsg")+" , errorCode : "+result.getInt("errorCode"));
                    }
                } else {
                    Toast.makeText(getApplicationContext(),"Some unknown error occuured. Please try after some time.", Toast.LENGTH_LONG).show();
                }
            }catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}