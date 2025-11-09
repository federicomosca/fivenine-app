package me.dogs.fivenine.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import me.dogs.fivenine.data.model.RestaurantEntity

@Dao
interface RestaurantDao {
    @Query("SELECT * FROM restaurants WHERE listId = :listId")
    fun getRestaurantsByListId(listId: Int): Flow<List<RestaurantEntity>>

    @Query("SELECT * FROM restaurants WHERE id = :id")
    suspend fun getRestaurantById(id: Int): RestaurantEntity?

    @Insert
    suspend fun insertRestaurant(restaurant: RestaurantEntity)

    @Update
    suspend fun updateRestaurant(restaurant: RestaurantEntity)

    @Delete
    suspend fun deleteRestaurant(restaurant: RestaurantEntity)

    @Query("DELETE FROM restaurants WHERE listId = :listId")
    suspend fun deleteRestaurantsByListId(listId: Int)
}
