package com.example.funcart;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class ItemsList extends AppCompatActivity {

    private Toolbar tbl;
    private int Count = 0;
     private TextView txtCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_list);

        tbl = (Toolbar) findViewById(R.id.home_tool);
        txtCount = (TextView) findViewById(R.id.textCount);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater later = getMenuInflater();
        later.inflate(R.menu.funcart_home,menu);
        MenuItem item = menu.findItem(R.id.Action_Cart);
        return super.onCreateOptionsMenu(menu);
    }

    public void addBike(View view) {
     Count ++;
        txtCount.setText(String.valueOf(Count));

    }
}
