package com.example.quanlydoan.ui.order;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.quanlydoan.R;
import com.example.quanlydoan.data.PrefsHelper;
import com.example.quanlydoan.data.model.Order;
import com.example.quanlydoan.ui.BaseActivity;
import com.example.quanlydoan.ui.home.FoodAdapter;
import com.example.quanlydoan.ui.orderInfo.OrderInfoActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrderActivity extends BaseActivity {
    ListView listViewOrder;
    ImageView ivOrderEmpty;

    ArrayList<Order> orders = new ArrayList<>();
    private final String TAG = "Test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        setControl();
        setEvent();
        setData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setData();
    }

    private void setEvent() {
        listViewOrder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Order order = (Order) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(OrderActivity.this, OrderInfoActivity.class);
                intent.putExtra("order", order);
                startActivity(intent);
            }
        });

    }

    private void setControl() {
        listViewOrder = findViewById(R.id.listViewOrder);
        ivOrderEmpty = findViewById(R.id.ivOrderEmpty);
    }

    private void setData() {
        listViewOrder.setAdapter(new OrderAdapter(this, R.layout.layout_item_order, orders));
        getOrdersOfUser();
    }

    private void showEmptyOrderImg() {
        ivOrderEmpty.setVisibility(View.VISIBLE);
        listViewOrder.setVisibility(View.GONE);
        ((OrderAdapter) listViewOrder.getAdapter()).notifyDataSetChanged();
    }

    private void hideEmptyOrderImg() {
        ivOrderEmpty.setVisibility(View.GONE);
        listViewOrder.setVisibility(View.VISIBLE);
    }


    public void getOrdersOfUser() {
        String userId = PrefsHelper.getInstance(getApplicationContext()).getCurrentUser().getUserId();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        Query query = database.getReference("order")
                .orderByChild("userId").equalTo(userId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                orders.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Order order = postSnapshot.getValue(Order.class);
                    orders.add(order);
                    Log.e(TAG, "Value is: " + order.getOrderId());
                }
                Log.e("TEST", "Size: " + orders.size());
                if (orders.size() > 0) {
                    hideEmptyOrderImg();
                }
                else{
                    showEmptyOrderImg();
                }
                ((OrderAdapter) listViewOrder.getAdapter()).notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }


}