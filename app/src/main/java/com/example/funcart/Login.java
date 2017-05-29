package com.example.funcart;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.funcart.HelperClass.Utill;

import java.util.HashMap;
import java.util.Map;


public class Login extends AppCompatActivity  implements View.OnClickListener {

  private Button login, createAccount;
  private TextView  UserName,Password;
  private EditText  name,pass;
  private SignUpPost signpst = null;
  private String url="http://ec2-35-154-75-22.ap-south-1.compute.amazonaws.com/funcart/login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_in);

       //define to Button
        login = (Button) findViewById(R.id.Login);
        createAccount = (Button) findViewById(R.id.createAccount);

        //define TextView
        UserName = (TextView) findViewById(R.id.textUser);
        Password =(TextView) findViewById(R.id.txtPass);

      //Define Edittext
        name =(EditText) findViewById(R.id.username);
        pass =(EditText) findViewById(R.id.password);

          /*set on listener*/
        createAccount.setOnClickListener(this);
        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case  R.id.Login:
                signpst = new SignUpPost(name.getText().toString(),pass.getText().toString(),null,null);
                   if (name.getText().toString().trim().isEmpty() || name.equals(" ")  ){
                       name.setError("enter name");
                   }  if(pass.getText().toString().trim().isEmpty() || pass.equals(" ")){
                       pass.setError("enter password");
                   }else {
                      new Task().execute(signpst);
            }
            break;
            case R.id.createAccount:
                startActivity(new Intent(Login.this, Signup.class));
                Toast.makeText(getApplicationContext(),"Create new account",Toast.LENGTH_LONG).show();
                break;
        }
    }

    private class Task  extends AsyncTask<SignUpPost,Integer,Map<String,String>>{
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(Login.this);
            dialog.setMessage("please wait... ");
            dialog.show();
        }
        @Override
        protected Map<String, String> doInBackground(SignUpPost... params) {
            Utill uObj = new Utill();
            SignUpPost log  =params[0];
            HashMap<String,String> login = new HashMap<String,String>();
            login.put("emailOrPhoneNumber",log.getname());
            login.put("password",log.getPassword());
            Map<String,String> result  = uObj.makeWebServiceCall(url,login);
            return result;
        }
        @Override
        protected void onPostExecute(Map<String, String> result) {
            if ( dialog.isShowing()) dialog.dismiss();

            if(result != null){
                if(result.containsKey(200)){
                    Toast.makeText(getApplicationContext(),result.get("200"),Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Login.this,Signup.class);
                    startActivity(intent);
                    finish();
                }else {
                    for (Map.Entry<String, String> entry : result.entrySet()) {
                        Toast.makeText(Login.this, entry.getValue(), Toast.LENGTH_SHORT).show();
                    }
                }
                 } else{
                        Toast.makeText(getApplicationContext(), "Some unknown error occuured. Please try after some time.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }