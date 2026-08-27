package com.example.ui.screens

import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachEmail
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Subject
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
import com.example.ui.theme.BorderMediumGray
import com.example.ui.theme.BorderSoftGray
import com.example.ui.theme.BucaneroBlue
import com.example.ui.theme.BucaneroBlueDark
import com.example.ui.theme.BucaneroWhite
import com.example.ui.theme.BucaneroYellow
import com.example.ui.theme.BucaneroYellowContainer
import com.example.ui.theme.BucaneroYellowDark
import com.example.ui.theme.SurfaceBackground
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreviewScreen(
    photoFile: File,
    photoUri: Uri,
    orderCode: String,
    recipientEmail: String,
    subject: String,
    notes: String,
    onOrderCodeChange: (String) -> Unit,
    onRecipientEmailChange: (String) -> Unit,
    onSubjectChange: (String) -> Unit,
    onNotesChange: (String) -> Unit,
    onRetakeClick: () -> Unit,
    onAcceptAndSendClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.preview_title),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = BucaneroBlue
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.testTag("preview_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = BucaneroBlue
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BucaneroWhite
                )
            )
        },
        bottomBar = {
            // Action Buttons Bar (Sticky at bottom)
            Surface(
                color = BucaneroWhite,
                shadowElevation = 8.dp,
                border = BorderStroke(1.dp, BorderSoftGray)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // "Reintentar" (Retake photo)
                    OutlinedButton(
                        onClick = onRetakeClick,
                        modifier = Modifier
                            .weight(1f)
                            .height(54.dp)
                            .testTag("retake_photo_button"),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.5.dp, BorderMediumGray),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = BucaneroBlue
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = stringResource(R.string.retake_photo),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    // "Aceptar y Enviar" (Accept & Send)
                    Button(
                        onClick = onAcceptAndSendClick,
                        modifier = Modifier
                            .weight(1.3f)
                            .height(54.dp)
                            .testTag("accept_and_send_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BucaneroBlue,
                            contentColor = BucaneroWhite
                        ),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 3.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = null,
                            tint = BucaneroYellow,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(R.string.accept_and_send),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = BucaneroWhite
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(SurfaceBackground)
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // PHOTO PREVIEW CONTAINER
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("photo_preview_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = BucaneroWhite),
                border = BorderStroke(1.5.dp, BorderSoftGray),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(280.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF0F172A))
                            .border(1.dp, BorderSoftGray, RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            model = photoFile,
                            contentDescription = "Vista previa de la orden tomada",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Fit
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(BucaneroYellowDark)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Foto lista para adjuntar al correo",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = TextSecondary
                            )
                        }

                        Text(
                            text = "${(photoFile.length() / 1024)} KB",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextSecondary
                        )
                    }
                }
            }

            // ORDER DETAILS & EMAIL FORM
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = BucaneroWhite),
                border = BorderStroke(1.dp, BorderSoftGray)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = "Datos para el Correo",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = BucaneroBlueDark
                    )

                    // Autogenerated Subject
                    OutlinedTextField(
                        value = subject,
                        onValueChange = onSubjectChange,
                        label = { Text("Asunto del Correo (Autogenerado)") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Subject,
                                contentDescription = null,
                                tint = BucaneroBlue
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("email_subject_input"),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BucaneroBlue,
                            unfocusedBorderColor = BorderSoftGray
                        ),
                        singleLine = true
                    )

                    // Recipient email
                    OutlinedTextField(
                        value = recipientEmail,
                        onValueChange = onRecipientEmailChange,
                        label = { Text("Destinatario") },
                        placeholder = { Text("logistica@mitienda.com") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = null,
                                tint = BucaneroBlue
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("email_recipient_input"),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BucaneroBlue,
                            unfocusedBorderColor = BorderSoftGray
                        ),
                        singleLine = true
                    )

                    // Order Code / Reference (optional)
                    OutlinedTextField(
                        value = orderCode,
                        onValueChange = onOrderCodeChange,
                        label = { Text(stringResource(R.string.order_code_label)) },
                        placeholder = { Text("ej. ORD-00124") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Numbers,
                                contentDescription = null,
                                tint = BucaneroBlue
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("order_code_input"),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BucaneroBlue,
                            unfocusedBorderColor = BorderSoftGray
                        ),
                        singleLine = true
                    )

                    // Notes / Comments (optional)
                    OutlinedTextField(
                        value = notes,
                        onValueChange = onNotesChange,
                        label = { Text(stringResource(R.string.notes_label)) },
                        placeholder = { Text("ej. Firmado por el encargado de recepción") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Notes,
                                contentDescription = null,
                                tint = BucaneroBlue
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("order_notes_input"),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BucaneroBlue,
                            unfocusedBorderColor = BorderSoftGray
                        ),
                        minLines = 2,
                        maxLines = 4
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
