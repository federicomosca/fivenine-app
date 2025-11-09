package me.dogs.fivenine.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import me.dogs.fivenine.data.model.*

@Database(
    entities = [
        ListEntity::class,
        ListItemEntity::class,
        FilmEntity::class,
        RestaurantEntity::class,
        BookEntity::class,
        VideoGameEntity::class,
        CustomItemEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun listDao(): ListDao
    abstract fun listItemDao(): ListItemDao
    abstract fun filmDao(): FilmDao
    abstract fun restaurantDao(): RestaurantDao
    abstract fun bookDao(): BookDao
    abstract fun videoGameDao(): VideoGameDao
    abstract fun customItemDao(): CustomItemDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "fivenine_db"
                )
                    .fallbackToDestructiveMigration() // For development - will reset DB on schema changes
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

