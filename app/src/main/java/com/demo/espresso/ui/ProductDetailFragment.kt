package com.demo.espresso.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.demo.espresso.R
import com.demo.espresso.data.Product
import com.demo.espresso.data.source.ProductDataSource
import com.demo.espresso.util.SharedPreferenceUtil
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_product_detail.*

class ProductDetailFragment constructor(
    private val requestOptions: RequestOptions,
    private val productDataSource: ProductDataSource
) : Fragment() {

    private lateinit var product: Product

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { args ->
            args.getInt("product_id").let { productId ->
                productDataSource.getProduct(productId)?.let { productFromRemote ->
                    product = productFromRemote
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_product_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addToCart()
        setProductDetails()
    }

    private fun setProductDetails() {
        Glide.with(this@ProductDetailFragment)
            .applyDefaultRequestOptions(requestOptions)
            .load(product.productImage)
            .into(imageViewProductImage)
        textViewProductName.text = product.productName
        textViewDescription.text = product.productDescription
        textViewPrice.text = "Rs. ${product.price.toString()}"
    }

    private fun addToCart() {
        var mainActivity = activity as MainActivity
        buttonAddCart.setOnClickListener(View.OnClickListener {
            var arrayList: ArrayList<Product> =
                SharedPreferenceUtil.getArrayList(
                    mainActivity.applicationContext
                )
            product.quantity = Integer.parseInt(spinnerQuantity.selectedItem.toString())
            SharedPreferenceUtil.productSaveAndExists(product, mainActivity.applicationContext)
            mainActivity.cartCount()
            Toast.makeText(activity, "Product successfully add to cart", Toast.LENGTH_LONG).show()
            mainActivity.supportFragmentManager.popBackStack()
        })

    }

}