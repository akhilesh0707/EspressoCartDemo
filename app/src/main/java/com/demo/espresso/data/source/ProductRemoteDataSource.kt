package com.demo.espresso.data.source

import com.demo.espresso.data.DummyProductData
import com.demo.espresso.data.Product

object ProductRemoteDataSource : ProductDataSource {

    private var PRODUCT_REMOTE_DATA = LinkedHashMap<Int, Product>(DummyProductData.products.size)

    init {
        for (product in DummyProductData.products) {
            addProduct(product)
        }
    }

    override fun getProducts(): List<Product> {
        return ArrayList(PRODUCT_REMOTE_DATA.values)
    }

    override fun getProduct(productId: Int): Product? {
        return PRODUCT_REMOTE_DATA[productId]
    }

    private fun addProduct(product: Product) {
        PRODUCT_REMOTE_DATA.put(product.id, product)
    }

}