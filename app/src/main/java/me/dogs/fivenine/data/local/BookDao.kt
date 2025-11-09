package me.dogs.fivenine.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import me.dogs.fivenine.data.model.BookEntity

@Dao
interface BookDao {
    @Query("SELECT * FROM books WHERE listId = :listId")
    fun getBooksByListId(listId: Int): Flow<List<BookEntity>>

    @Query("SELECT * FROM books WHERE id = :id")
    suspend fun getBookById(id: Int): BookEntity?

    @Insert
    suspend fun insertBook(book: BookEntity)

    @Update
    suspend fun updateBook(book: BookEntity)

    @Delete
    suspend fun deleteBook(book: BookEntity)

    @Query("DELETE FROM books WHERE listId = :listId")
    suspend fun deleteBooksByListId(listId: Int)
}
