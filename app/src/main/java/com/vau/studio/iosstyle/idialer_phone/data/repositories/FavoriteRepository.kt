package com.vau.studio.iosstyle.idialer_phone.data.repositories

import com.vau.studio.iosstyle.idialer_phone.data.local.FavoriteDao
import com.vau.studio.iosstyle.idialer_phone.data.models.Contact
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FavoriteRepository @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val favoriteDao: FavoriteDao,
){

    suspend fun addToFavorite(contact: Contact): Long = withContext(dispatcher) {
        favoriteDao.insertFavorite(contact = contact)
    }

    suspend fun getAllFavorite(): Flow<List<Contact>> {
        return favoriteDao.readAllFavorites().flowOn(dispatcher)
    }

    suspend fun updateFavorite(contact: Contact) = withContext(dispatcher) {
        favoriteDao.updateFavorite(contact = contact)
    }

    suspend fun deleteFavorite(contact: Contact): Long = withContext(dispatcher) {
        favoriteDao.deleteFavorite(contact = contact)
    }

    suspend fun deleteAllFavorite(): Long = withContext(dispatcher) {
        favoriteDao.deleteAll()
    }
}