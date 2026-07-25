package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.PhotoAction
import com.example.data.PhotoItem
import com.example.data.PhotoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.ArrayDeque

data class SwipeHistoryItem(
    val photo: PhotoItem,
    val previousAction: PhotoAction
)

enum class AppTab {
    SWIPER,
    TRASH,
    KEPT
}

class SwipeCleanerViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val repository = PhotoRepository(application, db.photoDao())

    val unreviewedPhotos: StateFlow<List<PhotoItem>> = repository.unreviewedPhotos
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val trashedPhotos: StateFlow<List<PhotoItem>> = repository.trashedPhotos
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val keptPhotos: StateFlow<List<PhotoItem>> = repository.keptPhotos
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allPhotos: StateFlow<List<PhotoItem>> = repository.allPhotos
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val trashedSizeBytes: StateFlow<Long> = repository.trashedPhotos
        .map { list -> list.sumOf { it.sizeBytes } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0L)

    private val _currentTab = MutableStateFlow(AppTab.SWIPER)
    val currentTab: StateFlow<AppTab> = _currentTab.asStateFlow()

    private val _hasPermission = MutableStateFlow(false)
    val hasPermission: StateFlow<Boolean> = _hasPermission.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // History stack for Undo action
    private val undoStack = ArrayDeque<SwipeHistoryItem>()

    private val _canUndo = MutableStateFlow(false)
    val canUndo: StateFlow<Boolean> = _canUndo.asStateFlow()

    init {
        loadPhotos()
    }

    fun setPermissionGranted(granted: Boolean) {
        _hasPermission.value = granted
        if (granted) {
            loadPhotos()
        }
    }

    fun loadPhotos() {
        viewModelScope.launch {
            _isLoading.value = true
            repository.loadGalleryPhotos()
            _isLoading.value = false
        }
    }

    fun selectTab(tab: AppTab) {
        _currentTab.value = tab
    }

    fun swipeLeft(photo: PhotoItem) {
        recordHistory(photo)
        viewModelScope.launch {
            repository.updateAction(photo.id, PhotoAction.TRASH)
        }
    }

    fun swipeRight(photo: PhotoItem) {
        recordHistory(photo)
        viewModelScope.launch {
            repository.updateAction(photo.id, PhotoAction.KEEP)
        }
    }

    fun swipeUp(photo: PhotoItem) {
        recordHistory(photo)
        viewModelScope.launch {
            repository.updateAction(photo.id, PhotoAction.FAVORITE)
        }
    }

    private fun recordHistory(photo: PhotoItem) {
        undoStack.push(SwipeHistoryItem(photo, photo.action))
        _canUndo.value = undoStack.isNotEmpty()
    }

    fun undoLastAction() {
        if (undoStack.isEmpty()) return
        val lastItem = undoStack.pop()
        _canUndo.value = undoStack.isNotEmpty()

        viewModelScope.launch {
            repository.updateAction(lastItem.photo.id, lastItem.previousAction)
        }
    }

    fun restorePhotoFromTrash(photo: PhotoItem) {
        viewModelScope.launch {
            repository.restoreFromTrash(photo.id)
        }
    }

    fun restoreAllFromTrash() {
        viewModelScope.launch {
            val list = trashedPhotos.value
            list.forEach { photo ->
                repository.restoreFromTrash(photo.id)
            }
        }
    }

    fun emptyTrashInApp() {
        viewModelScope.launch {
            repository.emptyTrashInApp()
        }
    }

    companion object {
        fun formatBytes(bytes: Long): String {
            val kb = 1024L
            val mb = kb * 1024L
            val gb = mb * 1024L

            return when {
                bytes >= gb -> String.format(java.util.Locale.US, "%.2f GB", bytes.toDouble() / gb)
                bytes >= mb -> String.format(java.util.Locale.US, "%.1f MB", bytes.toDouble() / mb)
                bytes >= kb -> String.format(java.util.Locale.US, "%d KB", bytes / kb)
                else -> "$bytes B"
            }
        }
    }
}
