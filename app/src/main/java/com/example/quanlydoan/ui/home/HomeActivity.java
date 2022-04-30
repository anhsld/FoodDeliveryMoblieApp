package com.example.quanlydoan.ui.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.quanlydoan.R;
import com.example.quanlydoan.ui.foodinfo.FoodInfoActivity;

import java.util.ArrayList;

public class    HomeActivity extends AppCompatActivity {
    GridView gridViewHomeFood;

    ArrayList<Type> types = new ArrayList<>();
    ArrayList<Food> foods = new ArrayList<>();
    RecyclerView recyclerViewType;
    TypeAdapter typeAdapter;
    FoodAdapter foodAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();
        getWindow().setStatusBarColor(getResources().getColor(R.color.primary));
        setControl();
        setEvent();
    }

    private void setEvent() {

        initTypes();
        typeAdapter = new TypeAdapter(types);
        recyclerViewType.setAdapter(typeAdapter);

        initFoods();
        foodAdapter = new FoodAdapter(this, R.layout.layout_item_food, foods);
        gridViewHomeFood.setAdapter(foodAdapter);

        gridViewHomeFood.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(HomeActivity.this, FoodInfoActivity.class);
                startActivity(intent);
            }
        });

    }

    private void setControl() {
        recyclerViewType = findViewById(R.id.recyclerViewType);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewType.setLayoutManager(mLayoutManager);
        recyclerViewType.setItemAnimator(new DefaultItemAnimator());

        gridViewHomeFood = findViewById(R.id.gridViewHomeFood);
    }

    private void initTypes(){
        Type type1 = new Type();
        type1.setName("Pizza");
        Type type2 = new Type();
        type2.setName("Hamburger");
        Type type3 = new Type();
        type3.setName("Spaghetti");
        Type type4 = new Type();
        type4.setName("Hot dog");
        Type type5 = new Type();
        type5.setName("Drink");
        types.add(type1);
        types.add(type2);
        types.add(type3);
        types.add(type4);
        types.add(type5);
    }

    private void initFoods(){
        Food food1 = new Food();
        food1.setImage("https://cdn.bepcuoi.com/media/650-425-banh-pizza-pho-mai-ca-chua-bepcuoi-189.jpg");
        food1.setName("Pizza rau củ");
        food1.setPrice(new Double(13.06));
        foods.add(food1);
        foods.add(food1);
        foods.add(food1);
        foods.add(food1);
        foods.add(food1);
    }
}