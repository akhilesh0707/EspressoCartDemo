package com.demo.espresso.data

data class Product (
    val id: Int,
    val productName: String,
    val productDescription: String,
    val productImage: String,
    val price: Int,
    var quantity: Int=0
)