package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
import com.example.data.model.DeliveryOrder
import com.example.data.repository.AppSettings
import com.example.ui.components.BrandHeroBanner
import com.example.ui.components.MiCervezaLogoHeader
import com.example.ui.theme.BorderMediumGray
import com.example.ui.theme.BorderSoftGray
import com.example.ui.theme.BucaneroBlue
import com.example.ui.theme.BucaneroBlueContainer
import com.example.ui.theme.BucaneroBlueDark
import com.example.ui.theme.BucaneroWhite
import com.example.ui.theme.BucaneroYellow
import com.example.ui.theme.BucaneroYellowDark
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.SuccessGreenContainer
import com.example.ui.theme.SurfaceBackground
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    settings: AppSettings,
    recentOrders: List<DeliveryOrder>,
    onTakePhotoClick: () -> Unit,
    onSelectGalleryClick: () -> Unit,
    onOpenSettingsClick: () -> Unit,
    onOpenHistoryClick: () -> Unit,
    onOrderClick: (DeliveryOrder) -> Unit,
    onResendOrderClick: (DeliveryOrder) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SurfaceBackground)
    ) {
        // App Bar
        TopAppBar(
            title = {
                MiCervezaLogoHeader(showSubtitle = true)
            },
            actions = {
                IconButton(
                    onClick = onOpenHistoryClick,
                    modifier = Modifier.testTag("history_nav_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = "Historial",
                        tint = BucaneroBlue
                    )
                }
                IconButton(
                    onClick = onOpenSettingsClick,
                    modifier = Modifier.testTag("settings_nav_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Configuración",
                        tint = BucaneroBlue
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = BucaneroWhite
            )
        )

        HorizontalDivider(color = BorderSoftGray, thickness = 1.dp)

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Hero Banner
            item {
                BrandHeroBanner()
            }

            // PRIMARY CALL TO ACTION BUTTON (Big & Clear)
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("take_photo_card"),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = BucaneroWhite),
                    border = BorderStroke(1.5.dp, BorderSoftGray),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Acción Principal",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = BucaneroBlue,
                            letterSpacing = 0.5.sp
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Large prominent button: "Tomar Foto de Orden de Entrega"
                        Button(
                            onClick = onTakePhotoClick,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(64.dp)
                                .testTag("take_order_photo_button"),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = BucaneroBlue,
                                contentColor = BucaneroWhite
                            ),
                            elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = 4.dp,
                                pressedElevation = 1.dp
                            )
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .background(BucaneroYellow),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CameraAlt,
                                        contentDescription = null,
                                        tint = BucaneroBlueDark,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Text(
                                    text = stringResource(R.string.take_order_photo_btn),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BucaneroWhite
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Secondary action: select from gallery
                        OutlinedButton(
                            onClick = onSelectGalleryClick,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("gallery_photo_button"),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, BorderMediumGray),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = BucaneroBlue
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.PhotoLibrary,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = stringResource(R.string.select_gallery_photo),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            // Quick Info: Destination Email Status
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = BucaneroWhite),
                    border = BorderStroke(1.dp, BorderSoftGray)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onOpenSettingsClick() }
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(BucaneroBlueContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = null,
                                tint = BucaneroBlue,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Destinatario por defecto:",
                                fontSize = 12.sp,
                                color = TextSecondary,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = settings.defaultRecipientEmail,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = BucaneroBlueDark,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Icon(
                            imageVector = Icons.Outlined.Edit,
                            contentDescription = "Editar configuración",
                            tint = BucaneroBlue,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // Recent Deliveries Section Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Envíos Recientes",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "${recentOrders.size} registro(s) en este dispositivo",
                            fontSize = 12.sp,
                            color = TextMuted
                        )
                    }

                    if (recentOrders.isNotEmpty()) {
                        Text(
                            text = "Ver todos",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = BucaneroBlue,
                            modifier = Modifier
                                .clickable { onOpenHistoryClick() }
                                .padding(4.dp)
                        )
                    }
                }
            }

            // Recent Orders List (or Empty state)
            if (recentOrders.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = BucaneroWhite),
                        border = BorderStroke(1.dp, BorderSoftGray)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.AddAPhoto,
                                contentDescription = null,
                                tint = BucaneroYellowDark,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Aún no hay órdenes registradas",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Toma una foto para comenzar el flujo de envío automático por correo.",
                                fontSize = 13.sp,
                                color = TextSecondary,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }
            } else {
                items(recentOrders.take(4), key = { it.id }) { order ->
                    RecentOrderItemCard(
                        order = order,
                        onClick = { onOrderClick(order) },
                        onResend = { onResendOrderClick(order) }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun RecentOrderItemCard(
    order: DeliveryOrder,
    onClick: () -> Unit,
    onResend: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = BucaneroWhite),
        border = BorderStroke(1.dp, BorderSoftGray),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Photo thumbnail
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(BorderSoftGray),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = File(order.photoPath),
                    contentDescription = "Foto de Orden",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = if (order.orderCode.isNotBlank()) order.orderCode else "Orden de Entrega",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = BucaneroBlueDark,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Surface(
                        color = SuccessGreenContainer,
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = SuccessGreen,
                                modifier = Modifier.size(10.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "Enviado",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = SuccessGreen
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = order.formattedDate,
                    fontSize = 12.sp,
                    color = TextSecondary
                )

                Text(
                    text = "A: ${order.recipientEmail}",
                    fontSize = 11.sp,
                    color = TextMuted,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            IconButton(
                onClick = onResend,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Reenviar",
                    tint = BucaneroBlue,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
