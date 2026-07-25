package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PhotoDao {
    @Query("SELECT * FROM photos ORDER BY dateAdded DESC")
    fun getAllPhotos(): Flow<List<PhotoItem>>

    @Query("SELECT * FROM photos WHERE action = 'UNREVIEWED' ORDER BY dateAdded DESC")
    fun getUnreviewedPhotos(): Flow<List<PhotoItem>>

    @Query("SELECT * FROM photos WHERE action = 'TRASH' ORDER BY trashedAt DESC")
    fun getTrashedPhotos(): Flow<List<PhotoItem>>

    @Query("SELECT * FROM photos WHERE action = 'KEEP' OR action = 'FAVORITE' ORDER BY dateAdded DESC")
    fun getKeptPhotos(): Flow<List<PhotoItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(photo: PhotoItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(photos: List<PhotoItem>)

    @Query("UPDATE photos SET action = :action, trashedAt = :trashedAt WHERE id = :id")
    suspend fun updateAction(id: String, action: PhotoAction, trashedAt: Long? = System.currentTimeMillis())

    @Query("DELETE FROM photos WHERE id = :id")
    suspend fun deletePhoto(id: String)

    @Query("DELETE FROM photos WHERE action = 'TRASH'")
    suspend fun emptyTrashInDatabase()

    @Query("DELETE FROM photos")
    suspend fun clearAll()
}
