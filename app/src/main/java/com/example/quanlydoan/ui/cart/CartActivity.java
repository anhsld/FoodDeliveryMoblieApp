package com.example.quanlydoan.ui.cart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.quanlydoan.R;
import com.example.quanlydoan.data.DataManager;
import com.example.quanlydoan.data.PrefsHelper;
import com.example.quanlydoan.data.model.CartFood;
import com.example.quanlydoan.data.model.Category;
import com.example.quanlydoan.data.model.Discount;
import com.example.quanlydoan.data.model.Order;
import com.example.quanlydoan.ui.BaseActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CartActivity extends BaseActivity {
    ListView listViewCart;
    ArrayList<CartFood> cartFoods = new ArrayList<>();
    CartFoodAdapter cartFoodAdapter;
    EditText editTextTypeVoucher;
    List<Discount> discountList = new ArrayList<>();
    TextView textViewCartDiscount, textViewCartTotalPayment, textViewCartTotalPrice, textViewCartShipCost;
    Button btnCartCheckout;

    private final String TAG = "Test";
    int salePercent = 0;
    float shipCost = 18;
    float totalPrice = 0;
    float totalPayment = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        getSupportActionBar().hide();
        getWindow().setStatusBarColor(getResources().getColor(R.color.primary));

        setControl();
        setEvent();
    }

    private void setEvent() {
        getCart();
        fillData();
        cartFoodAdapter = new CartFoodAdapter(this, R.layout.layout_item_cartfood, cartFoods);
        listViewCart.setAdapter(cartFoodAdapter);
        editTextTypeVoucher.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String key = charSequence.toString();
                for(Discount discount: discountList){
                    if(discount.getKey().equals(key)){
                        if(discount.getUntilDate() < new Date().getTime()){
                            showMessage("Apply discount successful!");
                            salePercent = discount.getPercent();
                            fillData();
                            return;
                        }
                        else{
                            showMessage("Expired discount!");
                        }
                    }
                }
                salePercent = 0;
                fillData();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btnCartCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addOrder();
            }
        });

    }

    private void setControl() {
        listViewCart = findViewById(R.id.listViewCart);
        editTextTypeVoucher = findViewById(R.id.editTextTypeVoucher);
        textViewCartDiscount = findViewById(R.id.textViewCartDiscount);
        textViewCartTotalPayment = findViewById(R.id.textViewCartTotalPayment);
        textViewCartTotalPrice = findViewById(R.id.textViewCartTotalPrice);
        textViewCartShipCost = findViewById(R.id.textViewCartShipCost);
        btnCartCheckout = findViewById(R.id.btnCartCheckout);
        getDiscounts();
    }

    public void fillData(){
        textViewCartShipCost.setText(""+shipCost);
        textViewCartDiscount.setText(salePercent+"%");
        totalPayment = 0;
        totalPrice = 0;
        for(CartFood cartFood: cartFoods){
            totalPrice += cartFood.getFood().getPrice()*cartFood.getAmount();
        }
        totalPayment = (totalPrice+shipCost) * (100-salePercent)/100;
        textViewCartTotalPrice.setText("$"+totalPrice);
        textViewCartTotalPayment.setText("$"+totalPayment);
    }

    public void updatePrefHelps(){
        Order order = PrefsHelper.getInstance(getApplicationContext()).getCurrentCart();
        order.setDiscount(salePercent);
        order.setTotalPayment(totalPayment);
        order.setTotalPrice(totalPrice);
    }

    private void getDiscounts(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("restaurant").child("discount");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                discountList.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Discount discount = postSnapshot.getValue(Discount.class);
                    discountList.add(discount);
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    private void getCart(){
        Order order = PrefsHelper.getInstance(getApplicationContext()).getCurrentCart();
        cartFoods.clear();
        cartFoods.addAll(order.getFoods());
    }

    private void addOrder(){
        updatePrefHelps();
        Order order = PrefsHelper.getInstance(getApplicationContext()).getCurrentCart();
        long time = new Date().getTime();
//        Date date = new Date(time);
//        String formatDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(date);
//        Log.e(TAG, String.valueOf(time));
//        Log.e(TAG, formatDate);

        order.setCreateDate(time);
//        order.setDiscount(200);
//        order.setLat(10.847254341212597);
//        order.setLng(106.77859239332878);
//        order.setDistance(DataManager.calDistance(10.84729648975777, 106.78571634009982,
//                order.getLat(), order.getLng()));
//        order.setTotalPrice(250.8);
//        order.setShipCost(order.getTotalPrice() * order.getDistance());
//        order.setStatus("OnGoing");
//        order.setTotalPayment(Math.max(order.getTotalPrice() + order.getShipCost() - order.getDiscount(), 0));

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("order").push();
        order.setOrderId(ref.getKey());
        ref.setValue(order);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Order order1 = snapshot.getValue(Order.class);
                Log.e(TAG, order1.getOrderId());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //ignored
            }
        });
    }



    @Override
    protected void onResume() {
        super.onResume();
        setEvent();
    }
}