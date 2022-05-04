package com.example.quanlydoan.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.quanlydoan.data.model.Order;
import com.example.quanlydoan.data.model.User;
import com.google.gson.Gson;

public class PrefsHelper {

    private static final String PREF_KEY_CURRENT_USER = "PREF_KEY_CURRENT_USER";
    private static final String PREF_KEY_CURRENT_CART = "PREF_KEY_CURRENT_CART";
    private static PrefsHelper instance;

    SharedPreferences sharedPreferences;

    public static PrefsHelper getInstance(Context context) {
        if (instance == null) {
            instance = new PrefsHelper(context);
        }
        return instance;
    }

    public PrefsHelper(Context context) {
        this.sharedPreferences = context.getSharedPreferences("SharePrefs", Context.MODE_PRIVATE);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key, T defaultValue) {
        if (defaultValue instanceof String) {
            return (T) sharedPreferences.getString(key, (String) defaultValue);
        } else if (defaultValue instanceof Boolean) {
            return (T) Boolean.valueOf(sharedPreferences.getBoolean(key, (Boolean) defaultValue));
        } else if (defaultValue instanceof Float) {
            return (T) Float.valueOf(sharedPreferences.getFloat(key, (Float) defaultValue));
        } else if (defaultValue instanceof Integer) {
            return (T) Integer.valueOf(sharedPreferences.getInt(key, (Integer) defaultValue));
        } else if (defaultValue instanceof Long) {
            return (T) Long.valueOf(sharedPreferences.getLong(key, (Long) defaultValue));
        }
        return null;
    }

    public <T> T get(String key, Class<T> tClass, T defaultValue) {
        return (T) new Gson().fromJson(sharedPreferences.getString(key, (String) defaultValue), tClass);
    }

    public <T> void put(String key, T data) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (data instanceof String) {
            editor.putString(key, (String) data);
        } else if (data instanceof Boolean) {
            editor.putBoolean(key, (Boolean) data);
        } else if (data instanceof Float) {
            editor.putFloat(key, (Float) data);
        } else if (data instanceof Integer) {
            editor.putInt(key, (Integer) data);
        } else if (data instanceof Long) {
            editor.putLong(key, (Long) data);
        } else {
            editor.putString(key, new Gson().toJson(data));
        }
        editor.apply();
    }

    public void clear() {
        sharedPreferences.edit().clear().apply();
    }

    public void remove(String[] item) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        for (String s : item) {
            editor.remove(s);
        }
        editor.apply();
    }

    public User getCurrentUser() {
        return get(PREF_KEY_CURRENT_USER, User.class, null);
    }

    public void setCurrentUser(User user) {
        put(PREF_KEY_CURRENT_USER, user);
    }

    public void removeCurrentUser() {
        sharedPreferences.edit().remove(PREF_KEY_CURRENT_USER).apply();
        removeCurrentCart();
    }

    public Order getCurrentCart() {
        return get(PREF_KEY_CURRENT_CART, Order.class, null);
    }

    public void setCurrentCart(Order order) {
        put(PREF_KEY_CURRENT_CART, order);
    }

    public void removeCurrentCart() {
        sharedPreferences.edit().remove(PREF_KEY_CURRENT_CART).apply();
    }
}
