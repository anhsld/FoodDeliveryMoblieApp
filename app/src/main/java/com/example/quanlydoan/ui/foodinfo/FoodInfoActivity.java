package com.example.quanlydoan.ui.foodinfo;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.quanlydoan.R;
import com.example.quanlydoan.data.PrefsHelper;
import com.example.quanlydoan.data.model.CartFood;
import com.example.quanlydoan.data.model.Food;
import com.example.quanlydoan.data.model.Order;
import com.example.quanlydoan.ui.AppConstants;
import com.example.quanlydoan.ui.BaseActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FoodInfoActivity extends BaseActivity {
    Food food = new Food();
    ImageView imgFoodInfo;
    TextView textViewFoodInfoName, textViewFoodInfoPrice, btnFoodInfoDec, btnFoodInfoInc, textViewFoodInfoAmount, textViewFoodInfoTotal, textViewFoodInfoDescripton;
    LinearLayout btnFoodInfoAddToCart;
    int amount = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_info);
        setControl();
        setEvent();
        fillData();
    }

    private void setControl() {
        imgFoodInfo = findViewById(R.id.imgFoodInfo);
        textViewFoodInfoName = findViewById(R.id.textViewFoodInfoName);
        textViewFoodInfoPrice = findViewById(R.id.textViewFoodInfoPrice);
        btnFoodInfoDec = findViewById(R.id.btnFoodInfoDec);
        btnFoodInfoInc = findViewById(R.id.btnFoodInfoInc);
        textViewFoodInfoAmount = findViewById(R.id.textViewFoodInfoAmount);
        textViewFoodInfoTotal = findViewById(R.id.textViewFoodInfoTotal);
        btnFoodInfoAddToCart = findViewById(R.id.btnFoodInfoAddToCart);
        textViewFoodInfoDescripton = findViewById(R.id.textViewFoodInfoDescripton);
    }

    private void setEvent() {
        btnFoodInfoDec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (amount > 1)
                    amount -= 1;
                fillData();
            }
        });
        btnFoodInfoInc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                amount += 1;
                fillData();
            }
        });
        btnFoodInfoAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Order order = PrefsHelper.getInstance(getApplicationContext()).getCurrentCart();
                List<CartFood> foods = order.getFoods();
                Food food1 = (Food) getIntent().getSerializableExtra(AppConstants.EXTRA_FOOD_KEY);
                CartFood cartFood = new CartFood();
                cartFood.setFood(food1);
                cartFood.setAmount(amount);
                boolean existed = false;
                for(CartFood i: foods){
                    if(i.getFood().getFoodId().equals(food1.getFoodId())){
                        existed = true;
                        break;
                    }
                }
                if(!existed){
                    foods.add(cartFood);
                        order.setFoods(foods);
                        PrefsHelper.getInstance(getApplicationContext()).setCurrentCart(order);
                    showMessage("Added to your cart");
                }
                else{
                    showMessage("This food has been in your cart yet");
                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void fillData() {
        food = (Food) getIntent().getSerializableExtra(AppConstants.EXTRA_FOOD_KEY);
        Picasso.get().load(food.getImage()).into(imgFoodInfo);
        textViewFoodInfoName.setText(food.getName());
        textViewFoodInfoDescripton.setText(food.getDescription());
        textViewFoodInfoAmount.setText(String.valueOf(amount));
        textViewFoodInfoPrice.setText("$" + food.getPrice());
        textViewFoodInfoTotal.setText("$"+ food.getPrice() * amount);
    }
}