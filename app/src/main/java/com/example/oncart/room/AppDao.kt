package com.example.oncart.room

import androidx.room.*
import com.example.oncart.model.LikedItems
import com.example.oncart.model.ProductItems

@Dao
interface AppDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToCart(productItem: ProductItems)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToLiked(productItem: LikedItems)

    @Delete
    suspend fun removeFromCart(productItem: ProductItems)

    @Delete
    suspend fun removeFromLiked(likedItem: LikedItems)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateCart(productItem: ProductItems)

    @Query("DELETE FROM ProductItems")
    suspend fun clearCart()

    @Query("SELECT * FROM ProductItems")
    fun getCartItems(): kotlinx.coroutines.flow.Flow<MutableList<ProductItems>>

    @Query("SELECT * FROM LikedItems")
    fun getLikedItems(): kotlinx.coroutines.flow.Flow<List<LikedItems>>

}