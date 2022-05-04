package com.example.quanlydoan.ui.cart;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.quanlydoan.R;
import com.example.quanlydoan.data.PrefsHelper;
import com.example.quanlydoan.data.model.CartFood;
import com.example.quanlydoan.data.model.Order;
import com.example.quanlydoan.ui.orderInfo.OrderInfoActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CartFoodAdapter extends ArrayAdapter<CartFood> {
    Context context;
    int resource;
    ArrayList<CartFood> data;

    public CartFoodAdapter(@NonNull Context context, int resource, @NonNull ArrayList<CartFood> data) {
        super(context, resource, data);
        this.context = context;
        this.resource = resource;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(resource, null);
        CartFood cartFood = data.get(position);
        TextView txtCartItemFoodName, txtCartItemFoodPrice, txtCartItemFoodAllPrice, btnCartItemDec, btnCartItemInc, txtCartItemFoodAmount;
        ImageView imgCartItemFood, btnCartItemRemove;
        txtCartItemFoodName = convertView.findViewById(R.id.txtCartItemFoodName);
        txtCartItemFoodPrice = convertView.findViewById(R.id.txtCartItemFoodPrice);
        txtCartItemFoodAllPrice = convertView.findViewById(R.id.txtCartItemFoodAllPrice);
        txtCartItemFoodAmount = convertView.findViewById(R.id.txtCartItemFoodAmount);
        imgCartItemFood = convertView.findViewById(R.id.imgCartItemFood);
        btnCartItemDec = convertView.findViewById(R.id.btnCartItemDec);
        btnCartItemInc = convertView.findViewById(R.id.btnCartItemInc);
        btnCartItemRemove = convertView.findViewById(R.id.btnCartItemRemove);
        txtCartItemFoodAmount.setText("" + cartFood.getAmount());
        txtCartItemFoodName.setText(cartFood.getFood().getName());
        txtCartItemFoodPrice.setText("Price: $" + String.valueOf(cartFood.getFood().getPrice()));
        txtCartItemFoodAllPrice.setText("Total: $" + String.valueOf(cartFood.getFood().getPrice() * cartFood.getAmount()));
        Picasso.get().load(cartFood.getFood().getImage()).into(imgCartItemFood);

        if(context instanceof OrderInfoActivity){
            btnCartItemDec.setVisibility(View.GONE);
            btnCartItemInc.setVisibility(View.GONE);
            btnCartItemRemove.setVisibility(View.GONE);
            txtCartItemFoodAmount.setVisibility(View.GONE);
        }

        btnCartItemDec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cartFood.getAmount() > 1)
                    cartFood.setAmount(cartFood.getAmount() - 1);
                Order order = PrefsHelper.getInstance(context).getCurrentCart();
                order.getFoods().get(position).setAmount(cartFood.getAmount());
                PrefsHelper.getInstance(context).setCurrentCart(order);
                txtCartItemFoodAmount.setText("" + cartFood.getAmount());
                ((CartActivity) context).setData();
            }
        });

        btnCartItemInc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cartFood.setAmount(cartFood.getAmount() + 1);
                Order order = PrefsHelper.getInstance(context).getCurrentCart();
                order.getFoods().get(position).setAmount(cartFood.getAmount());
                PrefsHelper.getInstance(context).setCurrentCart(order);
                txtCartItemFoodAmount.setText("" + cartFood.getAmount());
                ((CartActivity) context).setData();
            }
        });

        btnCartItemRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Order order = PrefsHelper.getInstance(context).getCurrentCart();
                order.getFoods().remove(position);
                PrefsHelper.getInstance(context).setCurrentCart(order);
                ((CartActivity) context).setData();
            }
        });

        return convertView;
    }
}
