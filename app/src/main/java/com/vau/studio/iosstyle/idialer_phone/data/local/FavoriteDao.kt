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
    suspend fun readAllFavorites(): Flow<List<Contact>>

    /**
     * Update one contact info
     */
    @Update
    suspend fun updateFavorite(contact: Contact): Long

    /**
     * add 1 favorite contact
     */
    @Insert
    suspend fun insertFavorite(contact: Contact): Long

    /**
     * Delete specific contact
     */
    @Delete
    suspend fun deleteFavorite(contact: Contact): Long

    /**
     * Delete all contacts
     */
    @Delete
    suspend fun deleteAll(): Long

}