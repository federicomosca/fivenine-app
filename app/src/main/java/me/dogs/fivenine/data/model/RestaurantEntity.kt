package me.dogs.fivenine.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "restaurants")
data class RestaurantEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val listId: Int,
    val name: String,
    val address: String,
    val type: String // "chinese", "italian", "bar", etc.
)
