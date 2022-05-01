package com.example.quanlydoan.ui.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.quanlydoan.R;
import com.example.quanlydoan.data.PrefsHelper;
import com.example.quanlydoan.data.model.Category;
import com.example.quanlydoan.data.model.Food;
import com.example.quanlydoan.ui.BaseActivity;
import com.example.quanlydoan.ui.foodinfo.FoodInfoActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HomeActivity extends BaseActivity implements CategoryAdapter.Callback {
    ImageView imgHomeUser;
    GridView gridViewHomeFood;
    ArrayList<Category> categories = new ArrayList<>();
    ArrayList<Food> foods = new ArrayList<>();
    ArrayList<Food> backupFoods = new ArrayList<>();
    RecyclerView recyclerViewType;
    CategoryAdapter categoryAdapter;
    FoodAdapter foodAdapter;
    TextView btnHomeRefresh, textViewHomeFullname;
    SearchView searchViewHomeFood;

    private String categoryFilter = "";
    private static final String TAG = "DataManager";
    private static final String TAM = "Tam";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();
        getWindow().setStatusBarColor(getResources().getColor(R.color.primary));
        setControl();
        setEvent();
        showLoading();
    }

    private void setEvent() {
        categoryAdapter = new CategoryAdapter(categories, this);
        getCategories();
        recyclerViewType.setAdapter(categoryAdapter);

        foodAdapter = new FoodAdapter(this, R.layout.layout_item_food, foods);
        getFoodsByCategory();
        gridViewHomeFood.setAdapter(foodAdapter);

        gridViewHomeFood.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Food food = foods.get(i);
                Intent intent = new Intent(HomeActivity.this, FoodInfoActivity.class);
                intent.putExtra("food", food);
                startActivity(intent);
            }
        });

        btnHomeRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoryAdapter = new CategoryAdapter(categories, HomeActivity.this);
                recyclerViewType.setAdapter(categoryAdapter);
                categoryFilter = "";
                searchViewHomeFood.setQuery("", false);
                getFoodsByCategory();
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
        btnHomeRefresh = findViewById(R.id.btnHomeRefresh);
        searchViewHomeFood = findViewById(R.id.searchViewHomeFood);
        imgHomeUser = findViewById(R.id.imgHomeUser);
        textViewHomeFullname = findViewById(R.id.textViewHomeFullname);
        textViewHomeFullname.setText(PrefsHelper.getInstance(getApplicationContext()).getCurrentUser().getFullName());
        Picasso.get().load(PrefsHelper.getInstance(getApplicationContext()).getCurrentUser().getAvatar()).into(imgHomeUser);
    }

    private void initTypes() {
        Category category1 = new Category();
        category1.setName("Pizza");
        categories.add(category1);
        categories.add(category1);
        categories.add(category1);
        categories.add(category1);
        categories.add(category1);
    }

    private void initFoods() {
        Food food1 = new Food();
        food1.setImage("https://cdn.bepcuoi.com/media/650-425-banh-pizza-pho-mai-ca-chua-bepcuoi-189.jpg");
        food1.setName("Pizza rau củ");
        foods.add(food1);
        foods.add(food1);
        foods.add(food1);
        foods.add(food1);
        foods.add(food1);
    }

    private void getCategories() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("restaurant").child("categories");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                categories.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Category category = postSnapshot.getValue(com.example.quanlydoan.data.model.Category.class);
                    categories.add(category);
                    categoryAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    public void getFoodsByCategory() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        Query query;
        if (categoryFilter == "") {
            query = database.getReference("restaurant").child("foods");
        } else {
            query = database.getReference("restaurant").child("foods")
                    .orderByChild("categoryId").equalTo(categoryFilter);
        }
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                hideLoading();
                foods.clear();
                backupFoods.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Food food = postSnapshot.getValue(com.example.quanlydoan.data.model.Food.class);
                    foods.add(food);
                    backupFoods.add(food);
                    foodAdapter.notifyDataSetChanged();
//                    Search
                    searchViewHomeFood.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String s) {
                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String s) {
                            search(s);
                            return false;
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }


    @Override
    public void onItemClick(int position, String categoryId) {
        categoryFilter = categoryId;
        getFoodsByCategory();
    }

    private void search(String text) {
        foods.clear();
        text = text.toLowerCase();
        if (text.length() == 0) foods.addAll(backupFoods);
        else {
            for (Food food : backupFoods) {
                if (food.getName().toLowerCase().contains(text)) {
                    foods.add(food);
                }
            }
        }
        foodAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setControl();
        setEvent();
    }
}
