package com.example.quanlydoan.ui.home;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.quanlydoan.data.model.Food;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.quanlydoan.R;

import java.util.ArrayList;
import java.util.Locale;

public class FoodAdapter extends ArrayAdapter<Food> {
    Context context;
    int resource;
    ArrayList<Food> data;

    public FoodAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Food> data) {
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
        Food food = data.get(position);
        ImageView imgHomeFood;
        TextView txtHomeFoodName, txtHomeFoodPrice;
        Button btnHomeFoodAdd;
        imgHomeFood = convertView.findViewById(R.id.imgHomeFood);
        txtHomeFoodName = convertView.findViewById(R.id.txtHomeFoodName);
        txtHomeFoodPrice = convertView.findViewById(R.id.txtHomeFoodPrice);

        Picasso.get().load(food.getImage()).into(imgHomeFood);
        txtHomeFoodName.setText(food.getName());
        txtHomeFoodPrice.setText(String.valueOf(food.getPrice()));

        return convertView;
    }
}
