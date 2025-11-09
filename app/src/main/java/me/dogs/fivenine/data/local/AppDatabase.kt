package me.dogs.fivenine.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import me.dogs.fivenine.data.model.ListEntity
import me.dogs.fivenine.data.model.ListItemEntity

@Database(entities = [ListEntity::class, ListItemEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun listDao(): ListDao
    abstract fun listItemDao(): ListItemDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "fivenine_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

