package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FolderZip
import androidx.compose.material.icons.filled.RotateLeft
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.PhotoItem
import com.example.ui.SwipeCleanerViewModel
import com.example.ui.components.PermissionBanner
import com.example.ui.components.TinderCard

@Composable
fun SwiperScreen(
    unreviewedPhotos: List<PhotoItem>,
    trashedPhotosCount: Int,
    keptPhotosCount: Int,
    trashedSizeBytes: Long,
    canUndo: Boolean,
    hasPermission: Boolean,
    onRequestPermission: () -> Unit,
    onSwipeLeft: (PhotoItem) -> Unit,
    onSwipeRight: (PhotoItem) -> Unit,
    onSwipeUp: (PhotoItem) -> Unit,
    onUndo: () -> Unit,
    onOpenTrash: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Header Stats Bar
        Surface(
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Fila Pendente",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                    Text(
                        text = "${unreviewedPhotos.size} fotos para revisar",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Surface(
                    color = MaterialTheme.colorScheme.errorContainer,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "🗑️ ${SwipeCleanerViewModel.formatBytes(trashedSizeBytes)}",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
            }
        }

        if (!hasPermission) {
            Spacer(modifier = Modifier.height(8.dp))
            PermissionBanner(onRequestPermission = onRequestPermission)
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Card Deck Container
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            if (unreviewedPhotos.isEmpty()) {
                // Empty Queue Victory View
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(24.dp)
                        .testTag("empty_queue_card"),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.CheckCircle,
                            contentDescription = null,
                            tint = Color(0xFF34C759),
                            modifier = Modifier.size(72.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Tudo Limpo!",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Você revisou todas as fotos da sua galeria.\n$trashedPhotosCount fotos salvas na Lixeira e $keptPhotosCount mantidas.",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        if (trashedPhotosCount > 0) {
                            Button(
                                onClick = onOpenTrash,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.error
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.FolderZip,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Ver Lixeira Seguro ($trashedPhotosCount)")
                            }
                        }
                    }
                }
            } else {
                // Stack of photos (Show top card and next card preview)
                val topPhoto = unreviewedPhotos.first()
                val nextPhoto = unreviewedPhotos.getOrNull(1)

                if (nextPhoto != null) {
                    TinderCard(
                        photo = nextPhoto,
                        onSwipeLeft = {},
                        onSwipeRight = {},
                        onSwipeUp = {},
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp, vertical = 24.dp)
                    )
                }

                TinderCard(
                    photo = topPhoto,
                    onSwipeLeft = { onSwipeLeft(topPhoto) },
                    onSwipeRight = { onSwipeRight(topPhoto) },
                    onSwipeUp = { onSwipeUp(topPhoto) },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp, vertical = 12.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Action Bar Controls (Tinder buttons)
        if (unreviewedPhotos.isNotEmpty()) {
            val topPhoto = unreviewedPhotos.first()

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 🔴 EXCLUIR (Left)
                FilledIconButton(
                    onClick = { onSwipeLeft(topPhoto) },
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = Color(0xFFFF3B30),
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .size(60.dp)
                        .testTag("action_button_delete")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Descartar Foto",
                        modifier = Modifier.size(32.dp)
                    )
                }

                // ↩️ UNDO (Desfazer)
                IconButton(
                    onClick = onUndo,
                    enabled = canUndo,
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = if (canUndo) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    ),
                    modifier = Modifier
                        .size(48.dp)
                        .testTag("action_button_undo")
                ) {
                    Icon(
                        imageVector = Icons.Default.RotateLeft,
                        contentDescription = "Desfazer Útil",
                        modifier = Modifier.size(26.dp)
                    )
                }

                // ⭐ FAVORITAR (Up)
                IconButton(
                    onClick = { onSwipeUp(topPhoto) },
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = Color(0xFFFFCC00),
                        contentColor = Color.Black
                    ),
                    modifier = Modifier
                        .size(48.dp)
                        .testTag("action_button_favorite")
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Favoritar Foto",
                        modifier = Modifier.size(26.dp)
                    )
                }

                // 🟢 MANTER (Right)
                FilledIconButton(
                    onClick = { onSwipeRight(topPhoto) },
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = Color(0xFF34C759),
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .size(60.dp)
                        .testTag("action_button_keep")
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Manter Foto",
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
    }
}
