package com.example.quanlydoan.ui.order;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.quanlydoan.R;
import com.example.quanlydoan.data.model.Order;

import java.util.ArrayList;

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
        TextView textViewOrderDate = convertView.findViewById(R.id.textViewOrderDate);
        TextView textViewOrderName = convertView.findViewById(R.id.textViewOrderName);
        TextView textViewOrderTotal = convertView.findViewById(R.id.textViewOrderTotal);
        TextView btnOrderShow = convertView.findViewById(R.id.btnOrderShow);



        return convertView;
    }
}
