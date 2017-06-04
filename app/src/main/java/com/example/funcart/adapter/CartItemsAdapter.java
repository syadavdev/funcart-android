package com.example.funcart.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.funcart.R;
import com.example.funcart.dataClass.ItemData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static java.security.AccessController.getContext;

public class CartItemsAdapter extends ArrayList<ItemData> {

/*    ArrayList<ItemData> cartDataList;
    Context context;
    int resource;
    private final String imageUrl = "http://ec2-35-154-75-22.ap-south-1.compute.amazonaws.com/images/";

    public CartItemsAdapter(Context context, int resource, ArrayList<ItemData> itemDataList) {
        super(context, resource, itemDataList);
        this.cartDataList = itemDataList;
        this.context = context;
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) getContext()
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.cart_items_adapter, null, true);

        }

        ItemData itemData = getItem(position);

        ImageView imageView = (ImageView) convertView.findViewById(R.id.cart);
        Picasso.with(context).load(imageUrl+itemData.getPicName()).into(imageView);

        TextView txtName = (TextView) convertView.findViewById(R.id.ItemName);
        txtName.setText(itemData.getName());

        TextView txtPrice = (TextView) convertView.findViewById(R.id.ItemPrice);
        txtPrice.setText(Double.toString(itemData.getPrice())+" ₹");

        return convertView;
    }*/
}
