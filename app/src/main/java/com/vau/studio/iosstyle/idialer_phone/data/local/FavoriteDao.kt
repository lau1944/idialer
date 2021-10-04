package com.vau.studio.iosstyle.idialer_phone.data.local

import androidx.room.*
import com.vau.studio.iosstyle.idialer_phone.data.models.Contact
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    /**
     * get all favorite contact
     */
    @Query("SELECT * FROM contact")
    fun readAllFavorites(): Flow<List<Contact>>

    /**
     * Update one contact info
     */
    @Update
    fun updateFavorite(contact: Contact): Int

    /**
     * add 1 favorite contact
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavorite(contact: Contact): Long

    /**
     * Delete specific contact
     */
    @Delete
    fun deleteFavorite(contact: Contact)

    /**
     * Delete all contacts
     */
    @Query("DELETE FROM contact")
    fun deleteAll(): Int

}