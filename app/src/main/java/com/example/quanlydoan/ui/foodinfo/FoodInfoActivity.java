package com.example.quanlydoan.ui.foodinfo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.quanlydoan.R;
import com.example.quanlydoan.data.model.Food;
import com.squareup.picasso.Picasso;

public class FoodInfoActivity extends AppCompatActivity {
    Food food;
    ImageView imgFoodInfo;
    TextView textViewFoodInfoName, textViewFoodInfoPrice, btnFoodInfoDec, btnFoodInfoInc, textViewFoodInfoAmount, textViewFoodInfoTotal, textViewFoodInfoDescripton;
    LinearLayout btnFoodInfoAddToCart;
    int amount = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_info);
        getSupportActionBar().hide();
        getWindow().setStatusBarColor(getResources().getColor(R.color.primary));
        food = (Food) getIntent().getSerializableExtra("food");
        setControl();
        setEvent();
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
        fillData();

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
    }

    private void fillData() {
        Picasso.get().load(food.getImage()).into(imgFoodInfo);
        textViewFoodInfoName.setText(food.getName());
        textViewFoodInfoDescripton.setText(food.getDescription());
        textViewFoodInfoAmount.setText(String.valueOf(amount));
        textViewFoodInfoPrice.setText("$" + String.valueOf(food.getPrice()));
        textViewFoodInfoTotal.setText("$"+String.valueOf(food.getPrice() * amount));
    }
}