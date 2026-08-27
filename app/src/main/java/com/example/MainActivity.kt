package com.example

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.DeliveryOrder
import com.example.ui.AppScreen
import com.example.ui.MainViewModel
import com.example.ui.UiEvent
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.OrderDetailScreen
import com.example.ui.screens.PreviewScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.util.EmailHelper
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                DeliveryOrderApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun DeliveryOrderApp(viewModel: MainViewModel) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val settings by viewModel.settings.collectAsStateWithLifecycle()
    val orders by viewModel.orders.collectAsStateWithLifecycle()

    val orderCodeInput by viewModel.orderCodeInput.collectAsStateWithLifecycle()
    val recipientEmailInput by viewModel.recipientEmailInput.collectAsStateWithLifecycle()
    val subjectInput by viewModel.subjectInput.collectAsStateWithLifecycle()
    val notesInput by viewModel.notesInput.collectAsStateWithLifecycle()

    // Camera Launcher
    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        viewModel.onPhotoCaptured(success)
        if (!success) {
            scope.launch {
                snackbarHostState.showSnackbar("Captura de foto cancelada.")
            }
        }
    }

    // Camera Permission Launcher
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            val uri = viewModel.prepareNewPhotoCapture()
            takePictureLauncher.launch(uri)
        } else {
            scope.launch {
                snackbarHostState.showSnackbar("Se requiere permiso de cámara para capturar la orden.")
            }
        }
    }

    // Function to initiate camera capture
    val startCameraCapture = {
        val permissionCheck = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        )
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            val uri = viewModel.prepareNewPhotoCapture()
            takePictureLauncher.launch(uri)
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    // Gallery Picker Launcher
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            viewModel.onGalleryPhotoSelected(uri)
        }
    }

    // Handle UI Events (Snackbars, Email Intent)
    LaunchedEffect(Unit) {
        viewModel.uiEvents.collect { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(event.message)
                }
                is UiEvent.LaunchEmail -> {
                    val emailIntent = EmailHelper.createSendEmailIntent(
                        context = context,
                        recipientEmail = event.recipientEmail,
                        ccEmail = event.ccEmail,
                        subject = event.subject,
                        photoUri = event.photoUri,
                        orderCode = event.orderCode,
                        notes = event.notes,
                        storeName = event.storeName
                    )
                    try {
                        context.startActivity(emailIntent)
                    } catch (e: Exception) {
                        Toast.makeText(
                            context,
                            "No se encontró una aplicación de correo instalada.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                is UiEvent.OpenCamera -> {
                    startCameraCapture()
                }
                is UiEvent.OpenGallery -> {
                    galleryLauncher.launch("image/*")
                }
            }
        }
    }

    // Back button handling
    BackHandler(enabled = currentScreen !is AppScreen.Home) {
        when (currentScreen) {
            is AppScreen.Home -> { /* Exit app */ }
            is AppScreen.Preview -> viewModel.navigateTo(AppScreen.Home)
            is AppScreen.Settings -> viewModel.navigateTo(AppScreen.Home)
            is AppScreen.History -> viewModel.navigateTo(AppScreen.Home)
            is AppScreen.OrderDetail -> viewModel.navigateTo(AppScreen.History)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        AnimatedContent(
            targetState = currentScreen,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            label = "screen_transition"
        ) { screen ->
            when (screen) {
                is AppScreen.Home -> {
                    HomeScreen(
                        settings = settings,
                        recentOrders = orders,
                        onTakePhotoClick = startCameraCapture,
                        onSelectGalleryClick = { galleryLauncher.launch("image/*") },
                        onOpenSettingsClick = { viewModel.navigateTo(AppScreen.Settings) },
                        onOpenHistoryClick = { viewModel.navigateTo(AppScreen.History) },
                        onOrderClick = { order -> viewModel.navigateTo(AppScreen.OrderDetail(order)) },
                        onResendOrderClick = { order -> viewModel.resendPastOrder(order) }
                    )
                }

                is AppScreen.Preview -> {
                    PreviewScreen(
                        photoFile = screen.photoFile,
                        photoUri = screen.photoUri,
                        orderCode = orderCodeInput,
                        recipientEmail = recipientEmailInput,
                        subject = subjectInput,
                        notes = notesInput,
                        onOrderCodeChange = viewModel::updateOrderCode,
                        onRecipientEmailChange = viewModel::updateRecipientEmail,
                        onSubjectChange = viewModel::updateSubject,
                        onNotesChange = viewModel::updateNotes,
                        onRetakeClick = startCameraCapture,
                        onAcceptAndSendClick = {
                            viewModel.acceptAndSendOrder(screen.photoFile, screen.photoUri)
                        },
                        onBackClick = { viewModel.navigateTo(AppScreen.Home) }
                    )
                }

                is AppScreen.Settings -> {
                    SettingsScreen(
                        currentSettings = settings,
                        onSaveSettings = { defaultEmail, ccEmail, subjectPrefix, storeName, appendDetails ->
                            viewModel.saveSettings(
                                defaultEmail = defaultEmail,
                                ccEmail = ccEmail,
                                subjectPrefix = subjectPrefix,
                                storeName = storeName,
                                appendDetails = appendDetails
                            )
                        },
                        onBackClick = { viewModel.navigateTo(AppScreen.Home) }
                    )
                }

                is AppScreen.History -> {
                    HistoryScreen(
                        orders = orders,
                        onOrderClick = { order -> viewModel.navigateTo(AppScreen.OrderDetail(order)) },
                        onResendClick = { order -> viewModel.resendPastOrder(order) },
                        onBackClick = { viewModel.navigateTo(AppScreen.Home) }
                    )
                }

                is AppScreen.OrderDetail -> {
                    OrderDetailScreen(
                        order = screen.order,
                        onResendClick = { viewModel.resendPastOrder(screen.order) },
                        onDeleteClick = { viewModel.deleteOrder(screen.order) },
                        onBackClick = { viewModel.navigateTo(AppScreen.History) }
                    )
                }
            }
        }
    }
}

