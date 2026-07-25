package com.example

import com.example.data.PhotoAction
import com.example.data.PhotoItem
import com.example.ui.SwipeCleanerViewModel
import org.junit.Assert.assertEquals
import org.junit.Test

class SwipeCleanerUnitTest {

    @Test
    fun `test formatBytes formats bytes correctly`() {
        assertEquals("500 B", SwipeCleanerViewModel.formatBytes(500L))
        assertEquals("1024 B", SwipeCleanerViewModel.formatBytes(1024L) /* 1 KB */)
        assertEquals("1.0 MB", SwipeCleanerViewModel.formatBytes(1024L * 1024L))
        assertEquals("4.5 MB", SwipeCleanerViewModel.formatBytes((4.5 * 1024 * 1024).toLong()))
        assertEquals("2.10 GB", SwipeCleanerViewModel.formatBytes((2.1 * 1024 * 1024 * 1024).toLong()))
    }

    @Test
    fun `test PhotoItem default action is UNREVIEWED`() {
        val photo = PhotoItem(
            id = "test_1",
            contentUri = "content://media/external/images/media/1",
            displayName = "test_image.jpg",
            sizeBytes = 2048000L
        )

        assertEquals(PhotoAction.UNREVIEWED, photo.action)
        assertEquals(null, photo.trashedAt)
    }

    @Test
    fun `test PhotoItem trash action update`() {
        val originalPhoto = PhotoItem(
            id = "test_1",
            contentUri = "content://media/external/images/media/1",
            displayName = "test_image.jpg"
        )

        val trashedPhoto = originalPhoto.copy(
            action = PhotoAction.TRASH,
            trashedAt = System.currentTimeMillis()
        )

        assertEquals(PhotoAction.TRASH, trashedPhoto.action)
        assert(trashedPhoto.trashedAt != null)
    }
}
