package com.example.quanlydoan.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.quanlydoan.data.model.Order;
import com.example.quanlydoan.data.model.User;
import com.google.gson.Gson;

public class PrefsHelper {

    //Shared Preferences lưu trữ thông tin dưới dạng key-value
    SharedPreferences sharedPreferences;

    //khai báo key để lưu user va cart
    private static final String PREF_KEY_CURRENT_USER = "PREF_KEY_CURRENT_USER";
    private static final String PREF_KEY_CURRENT_CART = "PREF_KEY_CURRENT_CART";


    //singleton class
    //class PrefHelper sẽ khởi tạo 1 lần duy nhất và được lưu lại trong biến instace để dùng trong
    // toàn bộ quá trình hoạt động của app
    private static PrefsHelper instance;

    //trả về instace của class PrefHelper
    public static PrefsHelper getInstance(Context context) {
        if (instance == null) { //PrefHelper chưa được khởi tạo
            instance = new PrefsHelper(context);
        }
        return instance;
    }


    //SharedPrefernces cần tham số context để khởi tạo
    /*
    Context chứa thông tin toàn cục về môi trường ứng dụng.
    Nó cho phép truy cập đến các tài nguyên ứng dụng (activity, fragment, intent,...)
     */
    public PrefsHelper(Context context) {
        //Khỏi tạo 1 SharedPreferences có tên là SharePrefs và đặt quyền riêng tư cho tập tin này là private
        //xem dữ liệu ở View -> Tool Windows -> Device file explorer -> data -> data -> com.example.quanlydoan -> share_prefs
        this.sharedPreferences = context.getSharedPreferences("SharePrefs", Context.MODE_PRIVATE);
    }



    //thêm user vào SharedPreferences
    public void setCurrentUser(User user) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //thêm thông tin với key: PREF_KEY_CURRENT_USER và value là 1 json object
        /* new Gson().toJson(object):
        sử dụng thư viện gson được import trong file Grandle Scripts -> build.grandle (Module: QuanLyDoAn.app)
        .....
        dependencies {
            ...
            implementation 'com.google.code.gson:gson:2.9.0'
            ...
        }
        .....

        hàm toJson(Object object) dùng để convert Object thành kiểu json để lưu vào SharedPreference vì SharedPreference ko hỗ trợ lưu object
         */
        editor.putString(PREF_KEY_CURRENT_USER, new Gson().toJson(user));
        editor.apply();
    }

    //lấy dữ liệu user
    public User getCurrentUser() {
        // hàm fromJson(String json, Object object) để convert chuỗi json thành Object (ở dưới là object User)
        return new Gson().fromJson(sharedPreferences.getString(PREF_KEY_CURRENT_USER, (String) ""), User.class);
    }

    //xóa value user khỏi SharedPreferences thông qua key PREF_KEY_CURRENT_USER
    public void removeCurrentUser() {
        sharedPreferences.edit().remove(PREF_KEY_CURRENT_USER).apply();
        removeCurrentCart();
    }





    public Order getCurrentCart() {
        return new Gson().fromJson(sharedPreferences.getString(PREF_KEY_CURRENT_CART, (String) ""), Order.class);
    }

    public void setCurrentCart(Order order) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PREF_KEY_CURRENT_CART, new Gson().toJson(order));
        editor.apply();
    }

    public void removeCurrentCart() {
        sharedPreferences.edit().remove(PREF_KEY_CURRENT_CART).apply();
    }
}
