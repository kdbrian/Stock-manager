package io.github.junrdev.stockmanager.data.local

import android.content.Context
import android.util.Log
import io.github.junrdev.stockmanager.model.Product
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductRepository(context: Context) {

    val productsDao = AppDB.getAppDb(context).productsDao()

    fun insertProduct(product: Product, onDone: (() -> Unit)? = null) {
        CoroutineScope(Dispatchers.IO).launch {
            productsDao.insertProduct(product)
            withContext(Dispatchers.Main) {
                onDone?.invoke()
            }
        }
    }

    suspend fun getProducts(): List<Product> {
        val products = productsDao.getAllProducts()
        Log.d(TAG, "getProducts: $products")
        return products
    }

    suspend fun getProductById(id: Long): Product {
        val product = productsDao.getProductById(id)
        Log.d(TAG, "getProductById: $product")
        return product
    }

    suspend fun getProductByName(pname : String): Product {
        val product = productsDao.getProductByName(pname)
        Log.d(TAG, "getProductByName: $product")
        return product
    }

    suspend fun getProductsWithCategory(cat: String): List<Product> {
        val products = productsDao.getAllProductsWithCategory(cat)
        Log.d(TAG, "getProducts: $products")
        return products
    }

    suspend fun updateProduct(product: Product) {
        productsDao.updateProduct(product)
    }

    suspend fun deleteProduct(product: Product) {
        productsDao.deleteProduct(product)
    }

    companion object {
        private const val TAG = "ProductRepository"
    }
}