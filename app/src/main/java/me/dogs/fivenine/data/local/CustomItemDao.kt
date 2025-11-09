package me.dogs.fivenine.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import me.dogs.fivenine.data.model.CustomItemEntity

@Dao
interface CustomItemDao {
    @Query("SELECT * FROM custom_items WHERE listId = :listId")
    fun getCustomItemsByListId(listId: Int): Flow<List<CustomItemEntity>>

    @Query("SELECT * FROM custom_items WHERE id = :id")
    suspend fun getCustomItemById(id: Int): CustomItemEntity?

    @Insert
    suspend fun insertCustomItem(customItem: CustomItemEntity)

    @Update
    suspend fun updateCustomItem(customItem: CustomItemEntity)

    @Delete
    suspend fun deleteCustomItem(customItem: CustomItemEntity)

    @Query("DELETE FROM custom_items WHERE listId = :listId")
    suspend fun deleteCustomItemsByListId(listId: Int)
}
