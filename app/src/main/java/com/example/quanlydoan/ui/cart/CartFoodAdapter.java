package com.example.quanlydoan.ui.cart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.quanlydoan.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CartFoodAdapter  extends ArrayAdapter<CartFood> {
    Context context;
    int resource;
    ArrayList<CartFood> data;
    public CartFoodAdapter(@NonNull Context context, int resource, @NonNull ArrayList<CartFood> data) {
        super(context, resource, data);
        this.context = context;
        this.resource = resource;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(resource, null);
        CartFood cartFood = data.get(position);
        TextView txtCartItemFoodName, txtCartItemFoodPrice, txtCartItemFoodAllPrice;
        ImageView imgCartItemFood;
        txtCartItemFoodName = convertView.findViewById(R.id.txtCartItemFoodName);
        txtCartItemFoodPrice = convertView.findViewById(R.id.txtCartItemFoodPrice);
        txtCartItemFoodAllPrice = convertView.findViewById(R.id.txtCartItemFoodAllPrice);
        imgCartItemFood = convertView.findViewById(R.id.imgCartItemFood);

        txtCartItemFoodName.setText(cartFood.getName());
        txtCartItemFoodPrice.setText("Price: "+ String.valueOf(cartFood.getPrice()));
        txtCartItemFoodAllPrice.setText("Total: "+String.valueOf(cartFood.getPrice()*cartFood.getAmount()));
        Picasso.get().load(cartFood.getImage()).into(imgCartItemFood);


        return convertView;
    }
}
