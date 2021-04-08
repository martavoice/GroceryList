package com.test.grocerylist.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.test.grocerylist.App;
import com.test.grocerylist.model.ItemModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PrefUtils {
    private static PrefUtils sInstance;
    private static final String PREF_NAME = "com.test.grocerylist.utils.PrefUtils";
    private static final String KEY_ITEMS_LIST = "items_list";

    private SharedPreferences preferences;

    @NonNull
    private Gson mGson;

    @NonNull
    public static PrefUtils getInstance() {
        if (sInstance == null) {
            synchronized (PrefUtils.class) {
                if (sInstance == null) {
                    sInstance = new PrefUtils(App.getInstance());
                }
            }
        }
        return sInstance;
    }

    private PrefUtils(@NonNull Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        GsonBuilder builder = new GsonBuilder();

        mGson = builder.create();
    }

    public void saveItems(@Nullable List<ItemModel> list) {
        preferences.edit().putString(KEY_ITEMS_LIST, mGson.toJson(list)).apply();
    }

    public List<ItemModel> getItems() {
        String tags = preferences.getString(KEY_ITEMS_LIST, null);
        if (TextUtils.isEmpty(tags)) {
            return new ArrayList<>();
        }

        Type type = new TypeToken<List<ItemModel>>() {
        }.getType();
        return mGson.fromJson(tags, type);
    }

    @SuppressLint("ApplySharedPref")
    public void clear() {
        preferences.edit().clear().commit();
    }

}
