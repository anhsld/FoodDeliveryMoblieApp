package com.example.quanlydoan.ui.cart;

import androidx.annotation.NonNull;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.quanlydoan.R;
import com.example.quanlydoan.data.PrefsHelper;
import com.example.quanlydoan.data.model.CartFood;
import com.example.quanlydoan.data.model.Discount;
import com.example.quanlydoan.data.model.Order;
import com.example.quanlydoan.ui.AppConstants;
import com.example.quanlydoan.ui.BaseActivity;
import com.example.quanlydoan.ui.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CartActivity extends BaseActivity {

    private final String TAG = "CartActivity";

    ListView listViewCart;
    EditText editTextTypeVoucher;
    TextView textViewCartDiscount, textViewCartTotalPayment, textViewCartTotalPrice, textViewCartShipCost;
    Button btnCartCheckout;
    ImageView imgCartEmpty;

    private ArrayList<CartFood> cartFoods = new ArrayList<>();
    private List<Discount> discountList = new ArrayList<>();
    private CartFoodAdapter cartFoodAdapter;

    int salePercent = 0;
    double shipCost = 18;
    double totalPrice = 0;
    double totalPayment = 0;


    @Override
    protected void onResume() {
        super.onResume();
        setData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        setControl();
        setEvent();
        getDiscounts();
        setData();
    }

    private void setControl() {
        listViewCart = findViewById(R.id.listViewCart);
        editTextTypeVoucher = findViewById(R.id.editTextTypeVoucher);
        textViewCartDiscount = findViewById(R.id.textViewCartDiscount);
        textViewCartTotalPayment = findViewById(R.id.textViewCartTotalPayment);
        textViewCartTotalPrice = findViewById(R.id.textViewCartTotalPrice);
        textViewCartShipCost = findViewById(R.id.textViewCartShipCost);
        btnCartCheckout = findViewById(R.id.btnCartCheckout);
        imgCartEmpty = findViewById(R.id.imgCartEmpty);
    }

    private void setEvent() {
        editTextTypeVoucher.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String key = charSequence.toString();
                for (Discount discount : discountList) {
                    if (discount.getKey().equals(key)) {
                        if (discount.getUntilDate() < new Date().getTime()) {
                            showMessage("Apply discount successful!");
                            salePercent = discount.getPercent();
                            fillData();
                            return;
                        } else {
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

        btnCartCheckout.setOnClickListener(view -> {
            if (checkEmptyCart()) addOrder();
        });

    }

    private boolean checkEmptyCart() {
        if (PrefsHelper.getInstance(getApplicationContext()).getCurrentCart().getFoods().size() == 0) {
            showMessage("There are no foods in your cart");
            return false;
        }
        return true;
    }


    @SuppressLint("SetTextI18n")
    public void setData() {
        Order order = PrefsHelper.getInstance(getApplicationContext()).getCurrentCart();
        cartFoods.clear();
        cartFoods.addAll(order.getFoods());
        cartFoodAdapter = new CartFoodAdapter(this, R.layout.layout_item_cartfood, cartFoods);
        listViewCart.setAdapter(cartFoodAdapter);
        if (cartFoods.size() == 0) {
            showEmptyImg();
        } else {
            hideEmptyImg();
        }
        fillData();
    }

    public void showEmptyImg() {
        listViewCart.setVisibility(View.GONE);
        imgCartEmpty.setVisibility(View.VISIBLE);
    }

    public void hideEmptyImg() {
        listViewCart.setVisibility(View.VISIBLE);
        imgCartEmpty.setVisibility(View.GONE);
    }

    @SuppressLint("SetTextI18n")
    public void fillData() {
        textViewCartShipCost.setText("" + shipCost);
        textViewCartDiscount.setText(salePercent + "%");
        totalPayment = 0;
        totalPrice = 0;
        for (CartFood cartFood : cartFoods) {
            totalPrice += cartFood.getFood().getPrice() * cartFood.getAmount();
        }

        double distance = Utils.calDistance(10.84729648975777, 106.78571634009982,
                AppConstants.CURRENT_LATITUDE, AppConstants.CURRENT_LONGITUDE);
        shipCost = totalPrice * distance * 0.1;
        textViewCartShipCost.setText("$" + (double) Math.round(shipCost * 10) / 10);
        totalPayment = (totalPrice + shipCost) * (100 - salePercent) / 100;
        textViewCartTotalPrice.setText("$" + totalPrice);
        textViewCartTotalPayment.setText("$" + totalPayment);

    }

    private void getDiscounts() {
        showLoading();
        DatabaseReference myRef = FirebaseDatabase.getInstance()
                .getReference(AppConstants.RESTAURANT_REF).child(AppConstants.DISCOUNT_REF);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                discountList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Discount discount = postSnapshot.getValue(Discount.class);
                    discountList.add(discount);
                }
                hideLoading();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Failed to read value.", error.toException());
                hideLoading();
            }
        });
    }

    private void addOrder() {
        showLoading();
        Order order = PrefsHelper.getInstance(getApplicationContext()).getCurrentCart();
        order.setDiscount(salePercent);
        order.setTotalPayment(totalPayment);
        order.setTotalPrice(totalPrice);
        order.setCreateDate(new Date().getTime());
        order.setLat(AppConstants.CURRENT_LATITUDE);
        order.setLng(AppConstants.CURRENT_LONGITUDE);
        order.setDistance(Utils.calDistance(10.84729648975777, 106.78571634009982,
                order.getLat(), order.getLng()));
//        shipcost = 10% totalprice * khoang cach
        order.setShipCost(order.getTotalPrice() * 0.1 * order.getDistance());
        order.setStatus(AppConstants.ORDER_ONGOING);
        order.setUserId(PrefsHelper.getInstance(getApplicationContext()).getCurrentUser().getUserId());

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference(AppConstants.ORDER_REF).push();
        order.setOrderId(ref.getKey());
        ref.setValue(order);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                hideLoading();
                PrefsHelper.getInstance(getApplicationContext()).setCurrentCart(new Order());
                Order order1 = snapshot.getValue(Order.class);
                Log.e(TAG, order1.getOrderId());
                showPopupMessage("Order food successfull", R.raw.success);
                Handler handler = new Handler();
                handler.postDelayed(() -> {
                    // start activity chi tiet don dat hang
                    setData();
                    clearVoucher();

                }, AppConstants.DIALOG_POPUP_TIME);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Failed to read value.", error.toException());
                showPopupMessage("Order food fail", R.raw.err);
                hideLoading();
            }
        });
    }

    private void clearVoucher() {
        textViewCartDiscount.setText("");
        editTextTypeVoucher.setText("");
        salePercent = 0;
    }


}