package com.example.quanlydoan.data;

import android.net.Uri;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;

import com.example.quanlydoan.data.model.Category;
import com.example.quanlydoan.data.model.Discount;
import com.example.quanlydoan.data.model.Food;
import com.example.quanlydoan.data.model.Order;
import com.example.quanlydoan.data.model.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class DataManager {

    private static final String TAG = "DataManager";

    public static final double LAT = 10.84729648975777;
    public static final double LNG = 106.78571634009982;

    public static void getCategories() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("restaurant").child("categories");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Category category = postSnapshot.getValue(Category.class);
                    Log.e(TAG, "Value is: " + category.getName());
                }
//                GenericTypeIndicator<List<Category>> t = new GenericTypeIndicator<List<Category>>() {};
//                List<Category> categories = dataSnapshot.getValue(t);
//                for (Category category : categories) {
//                    Log.e(TAG, "Value is: " + category.getName());
//                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    public static void getDiscounts() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("restaurant").child("discount");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<List<Discount>> t = new GenericTypeIndicator<List<Discount>>() {};
                List<Discount> discounts = dataSnapshot.getValue(t);
                for (Discount discount : discounts) {
                    Log.e(TAG, "Value is: " + discount.getDiscountId());
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    public static void getRecommendFoods() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("restaurant").child("recommend");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<List<Food>> t = new GenericTypeIndicator<List<Food>>() {};
                List<Food> foods = dataSnapshot.getValue(t);
                for (Food food: foods) {
                    Log.e(TAG, "Value is: " + food.getName());
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    public static void getDetailFood(String categoryId, String foodId) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("restaurant").child("foods")
                .child(categoryId + "_" + foodId);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Food food = dataSnapshot.getValue(Food.class);
                Log.e(TAG, "Value is: " + food.getFoodId());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    public static void getFoodsByCategory(String categoryId) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        Query query = database.getReference("restaurant").child("foods")
                .orderByChild("categoryId").equalTo(categoryId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Food food = postSnapshot.getValue(Food.class);
                    Log.e(TAG, "Value is: " + food.getName());
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    public static void searchFood(String name) {

    }

    public static void register() {
        Uri file = Uri.fromFile(new File("storage/emulated/0/Pictures/h4.jpg"));
        User user = new User("nguyenvantien", "sa", "123", "emailemail@gmail.com", "phone", "avatar", "address");


        //upload file to firebase storage
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference ref = storageRef.child("avatar/"+user.getUsername());
        UploadTask uploadTask = ref.putFile(file);
        uploadTask.continueWithTask(task -> {
            if (!task.isSuccessful()) {
                throw task.getException();
            }
            return ref.getDownloadUrl();
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                //get url image
                Uri downloadUri = task.getResult();
                user.setAvatar(downloadUri.toString());
                user.setUserId(user.getUsername());

                //Now insert user
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference usersRef = database.getReference("user").child(user.getUsername());
                usersRef.setValue(user);
                usersRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user1 = dataSnapshot.getValue(User.class);
                        Log.e(TAG, "Value is: " + user1.getFullName());
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w(TAG, "Failed to read value.", error.toException());
                    }
                });
            } else {
            }
        });
    }

    public static void getUserProfile(String username) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("user").child(username);
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User user = snapshot.getValue(User.class);
                    Log.e(TAG, user.getEmail());
                } else {
                    //user not found
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //ignored
            }
        });
    }

    public static void login(String username, String password) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("user").child(username);
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User user = snapshot.getValue(User.class);
                    if (user.getPassword().equals(password)) {
                        Log.e(TAG, user.getEmail());
                    } else {
                        //wrong password
                        Log.e(TAG, "SAI MAT KHAU ROI THANG DAU BUOI");
                    }
                } else {
                    // user not found
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //ignored
            }
        });
    }



    public static void getOrdersOfUser(String username) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        Query query = database.getReference("order")
                .orderByChild("userId").equalTo(username);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Order order = postSnapshot.getValue(Order.class);
                    Log.e(TAG, "Value is: " + order.getOrderId());
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    public static void getDetailOrder(String orderId) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        Query query = database.getReference("order")
                .orderByChild("orderId").equalTo(orderId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Order order = postSnapshot.getValue(Order.class);
                    Log.e(TAG, "Value is: " + order.getOrderId());
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    public static void addOrder() {
        Order order = new Order();
        long time = new Date().getTime();
        Date date = new Date(time);
        String formatDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(date);
        Log.e(TAG, String.valueOf(time));
        Log.e(TAG, formatDate);

        order.setCreateDate(time);
        order.setDiscount(200);
        order.setLat(10.847254341212597);
        order.setLng(106.77859239332878);
        order.setDistance(calDistance(LAT, LNG,
                order.getLat(), order.getLng()));
        order.setTotalPrice(250.8);
        order.setShipCost(order.getTotalPrice() * order.getDistance());
        order.setStatus("OnGoing");
        order.setTotalPayment(Math.max(order.getTotalPrice() + order.getShipCost() - order.getDiscount(), 0));

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

    public static double calDistance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344; //kilometer
        return ((double) Math.round(dist * 10) / 10); // round to 1 number
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts decimal degrees to radians             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts radians to decimal degrees             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

}


