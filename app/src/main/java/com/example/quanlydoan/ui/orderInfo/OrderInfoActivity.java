package com.example.quanlydoan.ui.orderInfo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.example.quanlydoan.R;
import com.example.quanlydoan.data.model.CartFood;
import com.example.quanlydoan.data.model.Order;
import com.example.quanlydoan.ui.cart.CartFoodAdapter;
import com.example.quanlydoan.ui.order.OrderAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class OrderInfoActivity extends AppCompatActivity {
    ListView lvOrderInfo;
    TextView tvFoodInfoStatus, tvFoodInfoDate, tvFoodInfoShipCost, tvFoodInfoTotalPrice;

    Order order;
    private final String TAG = "Test";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_info);
        order = (Order) getIntent().getSerializableExtra("order");
        setControl();
        setEvent();
        setData();
    }

    private void setEvent() {
    }

    private void setControl() {
        lvOrderInfo = findViewById(R.id.lvOrderInfo);
        tvFoodInfoStatus = findViewById(R.id.tvFoodInfoStatus);
        tvFoodInfoDate = findViewById(R.id.tvFoodInfoDate);
        tvFoodInfoShipCost = findViewById(R.id.tvFoodInfoShipCost);
        tvFoodInfoTotalPrice = findViewById(R.id.tvFoodInfoTotalPrice);
    }

    private void setData() {
        ArrayList<CartFood> foods = new ArrayList<>();
        foods.addAll(order.getFoods());
        lvOrderInfo.setAdapter(new CartFoodAdapter(this, R.layout.layout_item_cartfood, foods));
        tvFoodInfoStatus.setText(order.getStatus());
        Date date = new Date(order.getCreateDate());
        String formatDate = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss").format(date);
        tvFoodInfoDate.setText(formatDate);
        tvFoodInfoShipCost.setText("$"+order.getShipCost());
        tvFoodInfoTotalPrice.setText("$"+order.getTotalPayment());
    }
}