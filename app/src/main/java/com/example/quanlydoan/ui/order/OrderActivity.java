package com.example.quanlydoan.ui.order;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.example.quanlydoan.R;
import com.example.quanlydoan.data.model.Order;
import com.example.quanlydoan.ui.BaseActivity;

import java.util.ArrayList;

public class OrderActivity extends BaseActivity {
    ListView listViewOrder;
    ArrayList<Order> orders = new ArrayList<>();
    OrderAdapter orderAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
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