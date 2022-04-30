package com.example.quanlydoan.ui.cart;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.example.quanlydoan.R;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {
    ListView listViewCart;
    ArrayList<CartFood> cartFoods = new ArrayList<>();
    CartFoodAdapter cartFoodAdapter;
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
        initCartFoods();
        cartFoodAdapter = new CartFoodAdapter(this, R.layout.layout_item_cartfood, cartFoods);
        listViewCart.setAdapter(cartFoodAdapter);
    }

    private void setControl() {
        listViewCart = findViewById(R.id.listViewCart);
    }

    private void initCartFoods(){
        CartFood cartFood1 = new CartFood();
        cartFood1.setName("Pizza");
        cartFood1.setPrice(12.33);
        cartFood1.setImage("https://cdn.daylambanh.edu.vn/wp-content/uploads/2017/07/cach-lam-banh-pizza-bo.jpg");
        cartFood1.setAmount(2);
        cartFoods.add(cartFood1);
        cartFoods.add(cartFood1);
        cartFoods.add(cartFood1);
        cartFoods.add(cartFood1);
        cartFoods.add(cartFood1);
    }
}