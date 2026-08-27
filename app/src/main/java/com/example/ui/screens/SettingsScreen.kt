package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.repository.AppSettings
import com.example.ui.theme.BorderSoftGray
import com.example.ui.theme.BucaneroBlue
import com.example.ui.theme.BucaneroBlueContainer
import com.example.ui.theme.BucaneroBlueDark
import com.example.ui.theme.BucaneroWhite
import com.example.ui.theme.BucaneroYellow
import com.example.ui.theme.BucaneroYellowDark
import com.example.ui.theme.SurfaceBackground
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    currentSettings: AppSettings,
    onSaveSettings: (defaultEmail: String, ccEmail: String, subjectPrefix: String, storeName: String, appendDetails: Boolean) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var defaultEmail by remember { mutableStateOf(currentSettings.defaultRecipientEmail) }
    var ccEmail by remember { mutableStateOf(currentSettings.ccEmail) }
    var subjectPrefix by remember { mutableStateOf(currentSettings.subjectPrefix) }
    var storeName by remember { mutableStateOf(currentSettings.storeName) }
    var appendDetails by remember { mutableStateOf(currentSettings.appendOrderDetailsToBody) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.settings_title),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = BucaneroBlue
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.testTag("settings_back_button")
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
            Surface(
                color = BucaneroWhite,
                shadowElevation = 8.dp,
                border = BorderStroke(1.dp, BorderSoftGray)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Button(
                        onClick = {
                            onSaveSettings(
                                defaultEmail,
                                ccEmail,
                                subjectPrefix,
                                storeName,
                                appendDetails
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .testTag("save_settings_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BucaneroBlue,
                            contentColor = BucaneroWhite
                        ),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 3.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Save,
                            contentDescription = null,
                            tint = BucaneroYellow,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(R.string.save_settings),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
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
            // Header information card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = BucaneroBlueContainer),
                border = BorderStroke(1.dp, BucaneroBlue.copy(alpha = 0.2f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = BucaneroBlue,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Configura los datos base de tu equipo para automatizar el destinatario y formato de envío de órdenes de entrega.",
                        fontSize = 13.sp,
                        color = BucaneroBlueDark,
                        lineHeight = 18.sp
                    )
                }
            }

            // Email Settings Card
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
                        text = "Destinatarios de Correo",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = BucaneroBlueDark
                    )

                    // Default Recipient Email
                    OutlinedTextField(
                        value = defaultEmail,
                        onValueChange = { defaultEmail = it },
                        label = { Text(stringResource(R.string.default_email_label)) },
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
                            .testTag("settings_default_email_input"),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BucaneroBlue,
                            unfocusedBorderColor = BorderSoftGray
                        ),
                        singleLine = true
                    )

                    // CC Email (Optional)
                    OutlinedTextField(
                        value = ccEmail,
                        onValueChange = { ccEmail = it },
                        label = { Text(stringResource(R.string.cc_email_label)) },
                        placeholder = { Text("supervisor@mitienda.com") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.MailOutline,
                                contentDescription = null,
                                tint = BucaneroBlue
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("settings_cc_email_input"),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BucaneroBlue,
                            unfocusedBorderColor = BorderSoftGray
                        ),
                        singleLine = true
                    )
                }
            }

            // Subject and Store Name Configuration
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
                        text = "Personalización de la Orden",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = BucaneroBlueDark
                    )

                    // Subject Prefix
                    OutlinedTextField(
                        value = subjectPrefix,
                        onValueChange = { subjectPrefix = it },
                        label = { Text(stringResource(R.string.subject_prefix_label)) },
                        placeholder = { Text("Orden de Entrega") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.FormatQuote,
                                contentDescription = null,
                                tint = BucaneroBlue
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("settings_subject_prefix_input"),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BucaneroBlue,
                            unfocusedBorderColor = BorderSoftGray
                        ),
                        singleLine = true
                    )

                    Text(
                        text = "Ejemplo de asunto: \"$subjectPrefix - 2026-08-24 10:30\"",
                        fontSize = 12.sp,
                        color = TextSecondary,
                        modifier = Modifier.padding(start = 4.dp)
                    )

                    // Store / Team Name
                    OutlinedTextField(
                        value = storeName,
                        onValueChange = { storeName = it },
                        label = { Text("Nombre de la Tienda / Sucursal") },
                        placeholder = { Text("Tienda Bucanero Centro") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Business,
                                contentDescription = null,
                                tint = BucaneroBlue
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("settings_store_name_input"),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BucaneroBlue,
                            unfocusedBorderColor = BorderSoftGray
                        ),
                        singleLine = true
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
