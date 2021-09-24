package com.vau.studio.iosstyle.idialer_phone.data.local

import androidx.room.*
import com.vau.studio.iosstyle.idialer_phone.data.models.Contact

@Dao
interface FavoriteDao {

    @Query("SELECT * FROM contact")
    fun readAllFavorites(): List<Contact>

    @Update
    fun updateFavorite(contact: Contact)

    @Insert
    fun insertFavorite(contact: Contact)

    @Delete
    fun deleteFavorite(contact: Contact)

}