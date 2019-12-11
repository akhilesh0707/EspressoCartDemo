package com.demo.espresso.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.espresso.R
import com.demo.espresso.data.DummyProductData
import com.demo.espresso.data.Product
import com.demo.espresso.ui.adapter.CartProductListAdapter
import com.demo.espresso.ui.adapter.ProductListAdapter
import com.demo.espresso.util.EspressoIdlingResource
import com.demo.espresso.util.SharedPreferenceUtil
import com.demo.espresso.util.TopSpacingItemDecoration
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.fragment_product_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CartActivity : AppCompatActivity() {

    lateinit var listAdapter: CartProductListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        init()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@CartActivity)
            removeItemDecoration(TopSpacingItemDecoration(30))
            addItemDecoration(TopSpacingItemDecoration(30))
            listAdapter = CartProductListAdapter()
            adapter = listAdapter
        }
        var arrayList: ArrayList<Product> = SharedPreferenceUtil.getArrayList(this)
        var map = arrayList.groupBy { it -> it.quantity }
        listAdapter.submitList(arrayList)
    }

    private fun init() {
        buttonBuy.setOnClickListener(View.OnClickListener {
            SharedPreferenceUtil.clear(this@CartActivity)
            recyclerView.visibility = View.GONE
            buttonBuy.visibility = View.GONE
            textViewSuccess.visibility = View.VISIBLE
        })
    }
}
