package com.example.quanlydoan.ui.order;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.quanlydoan.R;
import com.example.quanlydoan.data.model.CartFood;
import com.example.quanlydoan.data.model.Food;
import com.example.quanlydoan.data.model.Order;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderAdapter extends ArrayAdapter<Order> {
    Context context;
    int resource;
    ArrayList<Order> data;
    public OrderAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Order> data) {
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
        Order order = data.get(position);
        TextView tvOrderItemName, tvOrderItemTime, tvOrderItemPrice;
        tvOrderItemName = convertView.findViewById(R.id.tvOrderItemName);
        tvOrderItemTime = convertView.findViewById(R.id.tvOrderItemTime);
        tvOrderItemPrice = convertView.findViewById(R.id.tvOrderItemPrice);

        tvOrderItemName.setText(getNameString(order.getFoods()));
        Date date = new Date(order.getCreateDate());
        String formatDate = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss").format(date);
        tvOrderItemTime.setText(formatDate);
        tvOrderItemPrice.setText("$"+String.valueOf(order.getTotalPayment()));


        return convertView;
    }

    public String getNameString(List<CartFood> foods){
        String res ="";
        if(foods.size() >= 1){
            for(CartFood i: foods){
                res+=", ";
                res += i.getFood().getName();
            }
            res = res.substring(2);
            return res;
        }
        return res;
    }

}
