package com.example.funcart.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.funcart.dataClass.ItemData;
import com.example.funcart.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ItemsListAdapter extends ArrayAdapter<ItemData> {

    ArrayList<ItemData> itemDataArrayList;
    Context context;
    int resource;
    private final String imageUrl = "http://ec2-35-154-75-22.ap-south-1.compute.amazonaws.com/images/";

    public ItemsListAdapter(Context context, int resource, ArrayList<ItemData> itemDataList) {
        super(context, resource, itemDataList);
        this.itemDataArrayList = itemDataList;
        this.context = context;
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) getContext()
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.customer_item_list_layout, null, true);

        }
        ItemData itemData = getItem(position);

        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageViewProduct);
        Picasso.with(context).load(imageUrl+itemData.getPicName()).into(imageView);

        TextView txtName = (TextView) convertView.findViewById(R.id.txtName);
        txtName.setText(itemData.getName());

        TextView txtPrice = (TextView) convertView.findViewById(R.id.txtPrice);
        txtPrice.setText(Double.toString(itemData.getPrice())+" ₹");

        return convertView;
    }
}
