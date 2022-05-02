package com.example.quanlydoan.ui.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
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
import com.example.quanlydoan.data.model.Discount;
import com.example.quanlydoan.data.model.Food;
import com.example.quanlydoan.ui.AppConstants;
import com.example.quanlydoan.ui.BaseActivity;
import com.example.quanlydoan.ui.foodinfo.FoodInfoActivity;
import com.example.quanlydoan.ui.start.SliderAdapter;
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
    RecyclerView recyclerViewType;
    TextView btnHomeRefresh, textViewHomeFullname;
    SearchView searchViewHomeFood;
    ViewPager2 sliderView;

    private ArrayList<Category> categories = new ArrayList<>();
    private ArrayList<Food> foods = new ArrayList<>();
    private ArrayList<Food> backupFoods = new ArrayList<>();
    private ArrayList<Discount> discounts = new ArrayList<>();
    private String categoryFilter = "";
    private final String TAG = "HomeActivity";

    @Override
    protected void onResume() {
        super.onResume();
        setData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setControl();
        setEvent();
        setData();
    }

    private void setControl() {
        recyclerViewType = findViewById(R.id.recyclerViewType);
        gridViewHomeFood = findViewById(R.id.gridViewHomeFood);
        btnHomeRefresh = findViewById(R.id.btnHomeRefresh);
        searchViewHomeFood = findViewById(R.id.searchViewHomeFood);
        imgHomeUser = findViewById(R.id.imgHomeUser);
        textViewHomeFullname = findViewById(R.id.textViewHomeFullname);
        sliderView = findViewById(R.id.slider);
    }

    private void setEvent() {
        gridViewHomeFood.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Food food = foods.get(i);
                Intent intent = new Intent(HomeActivity.this, FoodInfoActivity.class);
                intent.putExtra(AppConstants.EXTRA_FOOD_KEY, food);
                startActivity(intent);
            }
        });

        btnHomeRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoryFilter = "";
                ((CategoryAdapter) recyclerViewType.getAdapter()).row_index = -1;
                ((CategoryAdapter) recyclerViewType.getAdapter()).notifyDataSetChanged();
                searchViewHomeFood.setQuery("", false);
                getFoodsByCategory();
            }
        });
    }

    private void setData() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewType.setLayoutManager(mLayoutManager);
        recyclerViewType.setItemAnimator(new DefaultItemAnimator());
        recyclerViewType.setAdapter(new CategoryAdapter(categories, this));

        gridViewHomeFood.setAdapter(new FoodAdapter(this, R.layout.layout_item_food, foods));

        textViewHomeFullname.setText(PrefsHelper.getInstance(getApplicationContext()).getCurrentUser().getFullName());
        Picasso.get().load(PrefsHelper.getInstance(getApplicationContext()).getCurrentUser().getAvatar()).into(imgHomeUser);

        getCategories();
        getFoodsByCategory();
        getDiscounts();
    }

    private void getCategories() {
        startMultiProcess();
        DatabaseReference myRef = FirebaseDatabase.getInstance()
                .getReference(AppConstants.RESTAURANT_REF).child(AppConstants.CATEGORY_REF);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                categories.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Category category = postSnapshot.getValue(Category.class);
                    categories.add(category);
                }
                recyclerViewType.getAdapter().notifyDataSetChanged();
                endMultiProcess();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Failed to read value.", error.toException());
                endMultiProcess();
            }
        });
    }

    public void getFoodsByCategory() {
        startMultiProcess();
        Query query;
        if (categoryFilter.equals("")) {
            query = FirebaseDatabase.getInstance().getReference(AppConstants.RESTAURANT_REF)
                    .child(AppConstants.FOOD_REF);
        } else {
            query = FirebaseDatabase.getInstance().getReference(AppConstants.RESTAURANT_REF)
                    .child(AppConstants.FOOD_REF).orderByChild("categoryId").equalTo(categoryFilter);
        }
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                foods.clear();
                backupFoods.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Food food = postSnapshot.getValue(Food.class);
                    foods.add(food);
                    backupFoods.add(food);
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
                ((FoodAdapter) gridViewHomeFood.getAdapter()).notifyDataSetChanged();
                endMultiProcess();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
                endMultiProcess();
            }
        });
    }

    private void getDiscounts() {
        startMultiProcess();
        DatabaseReference myRef = FirebaseDatabase.getInstance()
                .getReference(AppConstants.RESTAURANT_REF).child(AppConstants.DISCOUNT_REF);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                discounts.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Discount discount = postSnapshot.getValue(Discount.class);
                    discounts.add(discount);
                }
                setupSlider();
                endMultiProcess();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Failed to read value.", error.toException());
                endMultiProcess();
            }
        });
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
        ((FoodAdapter) gridViewHomeFood.getAdapter()).notifyDataSetChanged();
    }


    @Override
    public void onItemClick(int position, String categoryId) {
        categoryFilter = categoryId;
        getFoodsByCategory();
    }

    private void setupSlider() {
        SliderAdapter adapter = new SliderAdapter(discounts);
        sliderView.setAdapter(adapter);
        sliderView.setNestedScrollingEnabled(false);
        sliderView.getChildAt(0).setOverScrollMode(View.OVER_SCROLL_NEVER);
        autoSlideViewPager();
    }

    private void autoSlideViewPager() {
        int c = sliderView.getAdapter().getItemCount();
        int i = sliderView.getCurrentItem() + 1;
        if (i == c) i = 0;
        sliderView.setCurrentItem(i);
        sliderView.postDelayed(this::autoSlideViewPager, 3000);
    }
}
