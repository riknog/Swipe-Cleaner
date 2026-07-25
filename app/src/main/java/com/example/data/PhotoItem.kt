package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class PhotoAction {
    UNREVIEWED,
    KEEP,
    TRASH,
    FAVORITE
}

@Entity(tableName = "photos")
data class PhotoItem(
    @PrimaryKey val id: String,
    val contentUri: String,
    val displayName: String,
    val sizeBytes: Long = 0L,
    val dateAdded: Long = System.currentTimeMillis(),
    val action: PhotoAction = PhotoAction.UNREVIEWED,
    val trashedAt: Long? = null,
    val isDemo: Boolean = false
)
