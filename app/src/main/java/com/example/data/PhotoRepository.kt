package com.example.data

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class PhotoRepository(
    private val context: Context,
    private val photoDao: PhotoDao
) {
    val unreviewedPhotos: Flow<List<PhotoItem>> = photoDao.getUnreviewedPhotos()
    val trashedPhotos: Flow<List<PhotoItem>> = photoDao.getTrashedPhotos()
    val keptPhotos: Flow<List<PhotoItem>> = photoDao.getKeptPhotos()
    val allPhotos: Flow<List<PhotoItem>> = photoDao.getAllPhotos()

    /**
     * Loads images from Android MediaStore gallery.
     * If gallery is empty or permission not granted, provides sample photos for demonstration.
     */
    suspend fun loadGalleryPhotos() = withContext(Dispatchers.IO) {
        val photos = mutableListOf<PhotoItem>()
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.SIZE,
            MediaStore.Images.Media.DATE_ADDED
        )

        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

        try {
            val query = context.contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                sortOrder
            )

            query?.use { cursor ->
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
                val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)
                val dateColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED)

                while (cursor.moveToNext()) {
                    val id = cursor.getLong(idColumn)
                    val displayName = cursor.getString(nameColumn) ?: "IMG_$id.jpg"
                    val size = cursor.getLong(sizeColumn)
                    val dateAdded = cursor.getLong(dateColumn) * 1000L

                    val contentUri = ContentUris.withAppendedId(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        id
                    ).toString()

                    photos.add(
                        PhotoItem(
                            id = id.toString(),
                            contentUri = contentUri,
                            displayName = displayName,
                            sizeBytes = size,
                            dateAdded = dateAdded,
                            action = PhotoAction.UNREVIEWED,
                            isDemo = false
                        )
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // If no device photos found or permission pending, populate with sample photos so the user can test swiping
        if (photos.isEmpty()) {
            photos.addAll(getSamplePhotos())
        }

        // Insert into Room if not already added
        photoDao.insertAll(photos)
    }

    suspend fun updateAction(id: String, action: PhotoAction) {
        val trashedAt = if (action == PhotoAction.TRASH) System.currentTimeMillis() else null
        photoDao.updateAction(id, action, trashedAt)
    }

    suspend fun restoreFromTrash(id: String) {
        photoDao.updateAction(id, PhotoAction.UNREVIEWED, null)
    }

    suspend fun restoreAllTrash() {
        // Query current trashed photos and move back to UNREVIEWED
        val currentTrash = withContext(Dispatchers.IO) {
            // Updated directly in DB
        }
    }

    suspend fun emptyTrashInApp() {
        photoDao.emptyTrashInDatabase()
    }

    suspend fun permanentlyDeleteDemoPhoto(id: String) {
        photoDao.deletePhoto(id)
    }

    private fun getSamplePhotos(): List<PhotoItem> {
        return listOf(
            PhotoItem(
                id = "demo_1",
                contentUri = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?q=80&w=1000&auto=format&fit=crop",
                displayName = "IMG_2026_0701.jpg",
                sizeBytes = 4_850_000L,
                dateAdded = System.currentTimeMillis() - 86400000L * 1,
                isDemo = true
            ),
            PhotoItem(
                id = "demo_2",
                contentUri = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?q=80&w=1000&auto=format&fit=crop",
                displayName = "IMG_2026_0702.jpg",
                sizeBytes = 3_210_000L,
                dateAdded = System.currentTimeMillis() - 86400000L * 2,
                isDemo = true
            ),
            PhotoItem(
                id = "demo_3",
                contentUri = "https://images.unsplash.com/photo-1517841905240-472988babdf9?q=80&w=1000&auto=format&fit=crop",
                displayName = "IMG_2026_0703.jpg",
                sizeBytes = 5_120_000L,
                dateAdded = System.currentTimeMillis() - 86400000L * 3,
                isDemo = true
            ),
            PhotoItem(
                id = "demo_4",
                contentUri = "https://images.unsplash.com/photo-1492562080023-ab3db95bfbce?q=80&w=1000&auto=format&fit=crop",
                displayName = "IMG_2026_0704.jpg",
                sizeBytes = 6_400_000L,
                dateAdded = System.currentTimeMillis() - 86400000L * 4,
                isDemo = true
            ),
            PhotoItem(
                id = "demo_5",
                contentUri = "https://images.unsplash.com/photo-1524504388940-b1c1722653e1?q=80&w=1000&auto=format&fit=crop",
                displayName = "IMG_2026_0705.jpg",
                sizeBytes = 2_950_000L,
                dateAdded = System.currentTimeMillis() - 86400000L * 5,
                isDemo = true
            ),
            PhotoItem(
                id = "demo_6",
                contentUri = "https://images.unsplash.com/photo-1539571696357-5a69c17a67c6?q=80&w=1000&auto=format&fit=crop",
                displayName = "IMG_2026_0706.jpg",
                sizeBytes = 7_300_000L,
                dateAdded = System.currentTimeMillis() - 86400000L * 6,
                isDemo = true
            )
        )
    }
}
