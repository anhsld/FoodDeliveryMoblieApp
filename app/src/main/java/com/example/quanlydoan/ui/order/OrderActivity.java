package com.example.quanlydoan.ui.order;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.lights.LightState;
import android.os.Bundle;
import android.widget.ListView;

import com.example.quanlydoan.R;

import java.util.ArrayList;

public class OrderActivity extends AppCompatActivity {
    ListView listViewOrder;
    ArrayList<Order> orders = new ArrayList<>();
    OrderAdapter orderAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        getSupportActionBar().hide();
        getWindow().setStatusBarColor(getResources().getColor(R.color.primary));
        setControl();
        setEvent();
    }

    private void setEvent() {
        initOrders();
        orderAdapter = new OrderAdapter(this, R.layout.layout_item_order, orders);
        listViewOrder.setAdapter(orderAdapter);

    }

    private void setControl() {
        listViewOrder = findViewById(R.id.listViewOrder);
    }

    private void initOrders(){
        Order order = new Order();
        orders.add(order);
        orders.add(order);
        orders.add(order);
        orders.add(order);
        orders.add(order);
        orders.add(order);
        orders.add(order);
    }
}