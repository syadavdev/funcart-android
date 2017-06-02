/*
package com.example.funcart;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.funcart.DataClass.ItemData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

*/
/**
 * Created by quocnguyen on 03/08/2016.
 *//*

public class CustomListAdapter extends ArrayAdapter<ItemDatas> {

    ArrayList<ItemData> itemDataArrayList;
    Context context;
    int resource;

    public CustomListAdapter(Context context, int resource, ArrayList<ItemData> products) {
        super(context, resource, itemDataArrayList);
        this.items = products;
        this.context = context;
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) getContext()
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.custom_list_layout, null, true);

        }
        ItemData product = getItem(position);

        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageViewProduct);
        Picasso.with(context).load(product.getImage()).into(imageView);

        TextView txtName = (TextView) convertView.findViewById(R.id.txtName);
        txtName.setText(product.getName());

        TextView txtPrice = (TextView) convertView.findViewById(R.id.txtPrice);
        txtPrice.setText(product.getPrice());

        return convertView;
    }
}
*/
