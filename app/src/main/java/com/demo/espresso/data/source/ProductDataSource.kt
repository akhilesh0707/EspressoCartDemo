package com.demo.espresso.data.source

import com.demo.espresso.data.Product

interface ProductDataSource {

    fun getProduct(productId: Int): Product?

    fun getProducts(): List<Product>
}