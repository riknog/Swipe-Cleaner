package com.example

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Style
import androidx.compose.material.icons.outlined.DeleteSweep
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Style
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.AppTab
import com.example.ui.SwipeCleanerViewModel
import com.example.ui.screens.KeptScreen
import com.example.ui.screens.SwiperScreen
import com.example.ui.screens.TrashScreen
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                SwipeCleanerApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeCleanerApp(viewModel: SwipeCleanerViewModel = viewModel()) {
    val context = LocalContext.current
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val unreviewedPhotos by viewModel.unreviewedPhotos.collectAsStateWithLifecycle()
    val trashedPhotos by viewModel.trashedPhotos.collectAsStateWithLifecycle()
    val keptPhotos by viewModel.keptPhotos.collectAsStateWithLifecycle()
    val trashedSizeBytes by viewModel.trashedSizeBytes.collectAsStateWithLifecycle()
    val canUndo by viewModel.canUndo.collectAsStateWithLifecycle()
    val hasPermission by viewModel.hasPermission.collectAsStateWithLifecycle()

    // Permission launcher
    val permissionToRequest = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_IMAGES
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        viewModel.setPermissionGranted(isGranted)
    }

    // Check permission on launch
    LaunchedEffect(Unit) {
        val check = ContextCompat.checkSelfPermission(context, permissionToRequest)
        viewModel.setPermissionGranted(check == PackageManager.PERMISSION_GRANTED)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "SwipeCleaner",
                        fontWeight = FontWeight.Black,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.testTag("bottom_navigation_bar")
            ) {
                // Swiper Tab
                NavigationBarItem(
                    selected = currentTab == AppTab.SWIPER,
                    onClick = { viewModel.selectTab(AppTab.SWIPER) },
                    icon = {
                        BadgedBox(
                            badge = {
                                if (unreviewedPhotos.isNotEmpty()) {
                                    Badge { Text("${unreviewedPhotos.size}") }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = if (currentTab == AppTab.SWIPER) Icons.Filled.Style else Icons.Outlined.Style,
                                contentDescription = "Deslizar"
                            )
                        }
                    },
                    label = { Text("Deslizar") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    modifier = Modifier.testTag("tab_swiper")
                )

                // Trash Tab
                NavigationBarItem(
                    selected = currentTab == AppTab.TRASH,
                    onClick = { viewModel.selectTab(AppTab.TRASH) },
                    icon = {
                        BadgedBox(
                            badge = {
                                if (trashedPhotos.isNotEmpty()) {
                                    Badge(containerColor = MaterialTheme.colorScheme.error) {
                                        Text("${trashedPhotos.size}")
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = if (currentTab == AppTab.TRASH) Icons.Filled.DeleteSweep else Icons.Outlined.DeleteSweep,
                                contentDescription = "Lixeira"
                            )
                        }
                    },
                    label = { Text("Lixeira") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.error,
                        indicatorColor = MaterialTheme.colorScheme.errorContainer
                    ),
                    modifier = Modifier.testTag("tab_trash")
                )

                // Kept Tab
                NavigationBarItem(
                    selected = currentTab == AppTab.KEPT,
                    onClick = { viewModel.selectTab(AppTab.KEPT) },
                    icon = {
                        BadgedBox(
                            badge = {
                                if (keptPhotos.isNotEmpty()) {
                                    Badge(containerColor = Color(0xFF34C759)) {
                                        Text("${keptPhotos.size}")
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = if (currentTab == AppTab.KEPT) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                contentDescription = "Guardadas"
                            )
                        }
                    },
                    label = { Text("Guardadas") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF34C759),
                        indicatorColor = Color(0xFF34C759).copy(alpha = 0.2f)
                    ),
                    modifier = Modifier.testTag("tab_kept")
                )
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                AppTab.SWIPER -> SwiperScreen(
                    unreviewedPhotos = unreviewedPhotos,
                    trashedPhotosCount = trashedPhotos.size,
                    keptPhotosCount = keptPhotos.size,
                    trashedSizeBytes = trashedSizeBytes,
                    canUndo = canUndo,
                    hasPermission = hasPermission,
                    onRequestPermission = { permissionLauncher.launch(permissionToRequest) },
                    onSwipeLeft = { viewModel.swipeLeft(it) },
                    onSwipeRight = { viewModel.swipeRight(it) },
                    onSwipeUp = { viewModel.swipeUp(it) },
                    onUndo = { viewModel.undoLastAction() },
                    onOpenTrash = { viewModel.selectTab(AppTab.TRASH) }
                )

                AppTab.TRASH -> TrashScreen(
                    trashedPhotos = trashedPhotos,
                    trashedSizeBytes = trashedSizeBytes,
                    onRestorePhoto = { viewModel.restorePhotoFromTrash(it) },
                    onRestoreAll = { viewModel.restoreAllFromTrash() },
                    onEmptyTrash = { viewModel.emptyTrashInApp() }
                )

                AppTab.KEPT -> KeptScreen(
                    keptPhotos = keptPhotos,
                    onMoveToTrash = { viewModel.swipeLeft(it) }
                )
            }
        }
    }
}
