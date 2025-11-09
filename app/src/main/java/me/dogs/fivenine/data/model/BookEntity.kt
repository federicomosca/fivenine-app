package me.dogs.fivenine.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class BookEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val listId: Int,
    val title: String,
    val author: String,
    val year: Int,
    val genre: String
)
