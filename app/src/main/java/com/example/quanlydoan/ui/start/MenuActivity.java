package com.example.quanlydoan.ui.start;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

import com.example.quanlydoan.R;
import com.example.quanlydoan.ui.cart.CartActivity;
import com.example.quanlydoan.ui.home.HomeActivity;
import com.example.quanlydoan.ui.order.OrderActivity;
import com.example.quanlydoan.ui.profile.ProfileActivity;

public class MenuActivity extends TabActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        getWindow().setStatusBarColor(getResources().getColor(R.color.primary));
        TabHost tabHost = getTabHost();

//        Home
        TabHost.TabSpec specHome = tabHost.newTabSpec("home");
        specHome.setIndicator("",getResources().getDrawable(R.drawable.home_icon));
        specHome.setContent(new Intent(this, HomeActivity.class));
        tabHost.addTab(specHome);

        TabHost.TabSpec specProfile = tabHost.newTabSpec("profile");
        specProfile.setIndicator("",getResources().getDrawable(R.drawable.profile_icon));
        specProfile.setContent(new Intent(this, ProfileActivity.class));
        tabHost.addTab(specProfile);

        TabHost.TabSpec specCart = tabHost.newTabSpec("cart");
        specCart.setIndicator("",getResources().getDrawable(R.drawable.cart_icon));
        specCart.setContent(new Intent(this, CartActivity.class));
        tabHost.addTab(specCart);

        TabHost.TabSpec specOrder = tabHost.newTabSpec("order");
        specOrder.setIndicator("",getResources().getDrawable(R.drawable.order_icon));
        specOrder.setContent(new Intent(this, OrderActivity.class));
        tabHost.addTab(specOrder);

        tabHost.getTabWidget().getChildAt(0)
                .setBackgroundResource(R.drawable.tab_select);

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
                    tabHost.getTabWidget().getChildAt(i)
                            .setBackgroundResource(R.drawable.tab_unselect);
                }
                tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab())
                        .setBackgroundResource(R.drawable.tab_select);
            }
        });

    }
}