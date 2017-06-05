package com.example.funcart;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

public class MycartActivity extends AppCompatActivity {

      Toolbar tblMyCart;
      ImageView picture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycart);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){

        }



    }
}
