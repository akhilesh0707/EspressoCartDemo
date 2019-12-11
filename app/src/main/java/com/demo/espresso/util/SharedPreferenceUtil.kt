package com.demo.espresso.util

import android.content.Context
import android.preference.PreferenceManager
import com.demo.espresso.data.Product
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SharedPreferenceUtil {
    companion object {
        val PREF_NAME: String = SharedPreferenceUtil::class.java.name
        val KEY: String = "CartProducts"
        fun saveArrayList(list: ArrayList<Product>, context: Context) {
            val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            val editor = prefs.edit()
            val gson = Gson()
            val json = gson.toJson(list)
            editor.putString(KEY, json)
            editor.apply()     // This line is IMPORTANT !!!
        }

        fun getArrayList(context: Context): ArrayList<Product> {
            val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            val gson = Gson()
            val json = prefs.getString(KEY, null)
            val type = object : TypeToken<ArrayList<Product>>() {}.getType()
            if (json != null) {
                return gson.fromJson(json, type)
            } else {
                return ArrayList<Product>();
            }
        }

        fun clear(context: Context) {
            val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            var editor = prefs.edit().clear().commit()
        }

        fun productSaveAndExists(newProduct: Product, context: Context) {
            var arrayList = getArrayList(context)
            var isFound = false
            for (item in arrayList) {
                if (item.id == newProduct.id) {
                    item.quantity = item.quantity + newProduct.quantity
                    isFound = true
                    break
                }
            }

            if (!isFound) {
                arrayList.add(newProduct)
            }

            saveArrayList(arrayList, context)
        }
    }
}