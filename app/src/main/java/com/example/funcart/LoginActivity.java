package com.example.funcart;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.funcart.requestClass.LoginPost;
import com.example.funcart.helperClass.CustomerUtil;
import com.example.funcart.helperClass.Validator;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class LoginActivity extends AppCompatActivity  implements View.OnClickListener {

  private Button login, createAccount;
  private EditText  eEmailOrPhonenumber,ePassword;

  private String url="http://ec2-35-154-75-22.ap-south-1.compute.amazonaws.com/funcart/login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_in);

        //define to Button
        login = (Button) findViewById(R.id.Login);
        createAccount = (Button) findViewById(R.id.createAccount);

        //Define Edittext
        eEmailOrPhonenumber =(EditText) findViewById(R.id.ShippingAddress);
        ePassword =(EditText) findViewById(R.id.editPassword);

        /*set on listener*/
        createAccount.setOnClickListener(this);
        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case  R.id.Login:

                if (eEmailOrPhonenumber.getText().toString().isEmpty() || eEmailOrPhonenumber.getText().toString() == null){
                    eEmailOrPhonenumber.setError("enter name");
                }else if(ePassword.getText().toString().isEmpty() || ePassword.getText().toString() == null){
                    ePassword.setError("enter password");
                }else if(!Validator.passwordValidate(ePassword.getText().toString())){
                    ePassword.setError("Invalid Password");
                }else if(!Validator.emailValidate(eEmailOrPhonenumber.getText().toString())){
                    if(Validator.phoneNumberValidate(eEmailOrPhonenumber.getText().toString())){
                        new Task().execute(new LoginPost(eEmailOrPhonenumber.getText().toString(),ePassword.getText().toString()));
                    }else{
                        eEmailOrPhonenumber.setError("Invalid Email or Phonenumber");
                    }
                }else if(!Validator.phoneNumberValidate(eEmailOrPhonenumber.getText().toString())){
                    if(Validator.emailValidate(eEmailOrPhonenumber.getText().toString())){
                        new Task().execute(new LoginPost(eEmailOrPhonenumber.getText().toString(),ePassword.getText().toString()));
                    }else{
                        eEmailOrPhonenumber.setError("Invalid Email or Phonenumber");
                    }
                }else{
                    new Task().execute(new LoginPost(eEmailOrPhonenumber.getText().toString(),ePassword.getText().toString()));
                }
                break;
            case R.id.createAccount:
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
                Toast.makeText(getApplicationContext(),"Enter Your Details",Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private class Task  extends AsyncTask<LoginPost,Integer,JSONObject>{
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(LoginActivity.this);
            dialog.setMessage("please wait... ");
            dialog.show();
        }
        @Override
        protected JSONObject doInBackground(LoginPost... params) {
            CustomerUtil customerUtil = new CustomerUtil();
            LoginPost log = params[0];

            HashMap<String,String> loginMap = new HashMap<String,String>();
            loginMap.put("emailOrPhoneNumber", log.getEmailOrPhonenumber());
            loginMap.put("password", log.getPassword());

            JSONObject result  = customerUtil.makeWebServiceCall(url,loginMap);
            return result;
        }
        @Override
        protected void onPostExecute(JSONObject result) {
            boolean flag = false;
            if (dialog.isShowing())
                dialog.dismiss();

            if(result != null) {
                try {
                    if(result.getInt("responseCode") == 200 || result.getInt("responseCode") == 201) {
                        Intent i = new Intent(LoginActivity.this, ItemsListActivity.class);
                        i.putExtra("token",result.getString("token"));
                        i.putExtra("secret",result.getString("secret"));

                        String fileName = "/CustomerData.txt";
                        File file = new File(getApplicationContext().getCacheDir().getAbsolutePath() + fileName);
                        if(file.exists()) {
                           file.delete();
                        }else {
                            file = new File(getApplicationContext().getCacheDir().getAbsolutePath() + fileName);
                        }
                        FileWriter fileWriter = new FileWriter(file,false);
                        result.put("password",ePassword.getText().toString());
                        result.remove("responseCode");
                        result.remove("token");
                        result.remove("secret");
                        fileWriter.write(result.toString());
                        fileWriter.flush();
                        fileWriter.close();

                        startActivity(new Intent(i));
                        finish();
                    } else {
                        eEmailOrPhonenumber.setError("errorMsg : "+result.getString("errorMsg")+" , errorCode : "+result.getInt("errorCode"));
                    }
                }catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else{
                Toast.makeText(getApplicationContext(), "Some unknown error occuured. Please try after some time.", Toast.LENGTH_LONG).show();
            }
        }
    }
}