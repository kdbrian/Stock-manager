package io.github.junrdev.stockmanager.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import io.github.junrdev.stockmanager.model.Product

@Dao
interface ProductsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProduct(product: Product)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateItemsStock(product: List<Product>)

    @Query("SELECT * FROM product")
    suspend fun getAllProducts(): List<Product>

    @Query("SELECT * FROM product WHERE pid = :id")
    suspend fun getProductById(id: Long): Product

    @Query("SELECT * FROM product WHERE productName = :pname")
    suspend fun getProductByName(pname : String): Product


    @Query("SELECT * FROM product WHERE category like '%' || :category || '%'")
    suspend fun getAllProductsWithCategory(category: String): List<Product>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateProduct(product: Product)

    @Delete
    suspend fun deleteProduct(product: Product)
}