package com.example.oncart.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.oncart.model.AddressModel
import com.example.oncart.model.LikedItems
import com.example.oncart.model.ProductItems

@Database(entities = [ProductItems::class, LikedItems::class, AddressModel::class], exportSchema = false, version = 6)
abstract class AppDatabase: RoomDatabase() {
    abstract fun getDao():AppDao

    companion object {
        var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "oncart_database")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration().build()
                INSTANCE = instance
                return instance
            }
        }
    }
}