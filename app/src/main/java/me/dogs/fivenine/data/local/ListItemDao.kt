package me.dogs.fivenine.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import me.dogs.fivenine.data.model.ListItemEntity

@Dao
interface ListItemDao {
    @Query("SELECT * FROM list_items WHERE listId = :listId")
    fun getItemsByListId(listId: Int): Flow<List<ListItemEntity>>

    @Insert
    suspend fun insertItem(item: ListItemEntity)

    @Delete
    suspend fun deleteItem(item: ListItemEntity)
}