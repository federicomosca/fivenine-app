package me.dogs.fivenine.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import me.dogs.fivenine.data.model.ListEntity

@Dao
interface ListDao {
    @Query("SELECT * FROM lists")
    fun getAllLists(): Flow<List<ListEntity>>

    @Insert
    suspend fun insertList(list: ListEntity): Long

    @Delete
    suspend fun deleteList(list: ListEntity)
}