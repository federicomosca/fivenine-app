package me.dogs.fivenine.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "videogames")
data class VideoGameEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val listId: Int,
    val title: String,
    val year: Int,
    val publisher: String,
    val platform: String, // "PlayStation", "Xbox", "PC", "Nintendo", etc.
    val genre: String
)
