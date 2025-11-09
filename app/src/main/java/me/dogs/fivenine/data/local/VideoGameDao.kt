package me.dogs.fivenine.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import me.dogs.fivenine.data.model.VideoGameEntity

@Dao
interface VideoGameDao {
    @Query("SELECT * FROM videogames WHERE listId = :listId")
    fun getVideoGamesByListId(listId: Int): Flow<List<VideoGameEntity>>

    @Query("SELECT * FROM videogames WHERE id = :id")
    suspend fun getVideoGameById(id: Int): VideoGameEntity?

    @Insert
    suspend fun insertVideoGame(videoGame: VideoGameEntity)

    @Update
    suspend fun updateVideoGame(videoGame: VideoGameEntity)

    @Delete
    suspend fun deleteVideoGame(videoGame: VideoGameEntity)

    @Query("DELETE FROM videogames WHERE listId = :listId")
    suspend fun deleteVideoGamesByListId(listId: Int)
}
