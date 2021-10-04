package com.vau.studio.iosstyle.idialer_phone.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vau.studio.iosstyle.idialer_phone.data.models.Contact

@Database(entities = [Contact::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
}