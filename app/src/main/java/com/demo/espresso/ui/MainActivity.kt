package com.demo.espresso.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.request.RequestOptions
import com.demo.espresso.R
import com.demo.espresso.data.source.ProductDataSource
import com.demo.espresso.data.source.ProductRemoteDataSource
import com.demo.espresso.factory.ProductFragmentFactory
import kotlinx.android.synthetic.main.activity_main.*
import android.preference.PreferenceManager
import android.content.SharedPreferences
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.demo.espresso.data.Product
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.toolbar.*


class MainActivity : AppCompatActivity(), UICommunicationListener {

    private lateinit var requestOptions: RequestOptions
    private lateinit var productDataSource: ProductDataSource
    val cartProductsKey: String = "CartProducts"
    override fun onCreate(savedInstanceState: Bundle?) {
        initDependencies()
        supportFragmentManager.fragmentFactory = ProductFragmentFactory(
            requestOptions,
            productDataSource
        )
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        cartCount();
        init()
    }

    fun cartCount() {
        var arrayList = getArrayList(cartProductsKey)
        if (arrayList.size > 0) {
            textViewCount.visibility = View.VISIBLE
            textViewCount.text = arrayList.size.toString()
        } else {
            textViewCount.visibility = View.GONE
        }
    }

    private fun init() {
        if (supportFragmentManager.fragments.size == 0) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, ProductListFragment::class.java, null)
                .commit()
        }
    }

    private fun initDependencies() {
        if (!::requestOptions.isInitialized) {
            // glide
            requestOptions = RequestOptions
                .placeholderOf(R.drawable.default_image)
                .error(R.drawable.default_image)
        }
        if (!::productDataSource.isInitialized) {
            // Data Source
            productDataSource = ProductRemoteDataSource
        }
    }

    override fun loading(isLoading: Boolean) {
        if (isLoading)
            progress_bar.visibility = View.VISIBLE
        else
            progress_bar.visibility = View.INVISIBLE
    }

    fun saveArrayList(list: ArrayList<Product>, key: String) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val editor = prefs.edit()
        val gson = Gson()
        val json = gson.toJson(list)
        editor.putString(key, json)
        editor.apply()     // This line is IMPORTANT !!!
    }

    fun getArrayList(key: String): ArrayList<Product> {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val gson = Gson()
        val json = prefs.getString(key, null)
        val type = object : TypeToken<ArrayList<Product>>() {}.getType()
        if(json!=null){
            return gson.fromJson(json, type)
        }else{
            return ArrayList<Product>();
        }
    }

    fun clear() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val editor = prefs.edit()
        editor.clear()
    }
}
