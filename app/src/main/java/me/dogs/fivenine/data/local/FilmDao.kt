package me.dogs.fivenine.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import me.dogs.fivenine.data.model.FilmEntity

@Dao
interface FilmDao {
    @Query("SELECT * FROM films WHERE listId = :listId")
    fun getFilmsByListId(listId: Int): Flow<List<FilmEntity>>

    @Query("SELECT * FROM films WHERE id = :id")
    suspend fun getFilmById(id: Int): FilmEntity?

    @Insert
    suspend fun insertFilm(film: FilmEntity)

    @Update
    suspend fun updateFilm(film: FilmEntity)

    @Delete
    suspend fun deleteFilm(film: FilmEntity)

    @Query("DELETE FROM films WHERE listId = :listId")
    suspend fun deleteFilmsByListId(listId: Int)
}
