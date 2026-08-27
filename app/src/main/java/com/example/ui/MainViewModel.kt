package com.example.ui

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.DeliveryOrder
import com.example.data.repository.AppSettings
import com.example.data.repository.DeliveryOrderRepository
import com.example.data.repository.SettingsRepository
import com.example.util.PhotoFileHelper
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File

sealed interface AppScreen {
    data object Home : AppScreen
    data class Preview(val photoFile: File, val photoUri: Uri) : AppScreen
    data object Settings : AppScreen
    data object History : AppScreen
    data class OrderDetail(val order: DeliveryOrder) : AppScreen
}

sealed interface UiEvent {
    data class ShowSnackbar(val message: String) : UiEvent
    data class LaunchEmail(
        val recipientEmail: String,
        val ccEmail: String,
        val subject: String,
        val photoUri: Uri,
        val orderCode: String,
        val notes: String,
        val storeName: String
    ) : UiEvent
    data object OpenCamera : UiEvent
    data object OpenGallery : UiEvent
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val orderRepository = DeliveryOrderRepository(db.deliveryOrderDao())
    private val settingsRepository = SettingsRepository(application)

    val settings: StateFlow<AppSettings> = settingsRepository.settings

    val orders: StateFlow<List<DeliveryOrder>> = orderRepository.allOrders
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _currentScreen = MutableStateFlow<AppScreen>(AppScreen.Home)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    private val _uiEvents = MutableSharedFlow<UiEvent>()
    val uiEvents: SharedFlow<UiEvent> = _uiEvents.asSharedFlow()

    // Temporary photo holder while capturing
    var pendingPhotoFile: File? = null
    var pendingPhotoUri: Uri? = null

    // Order form state for preview screen
    private val _orderCodeInput = MutableStateFlow("")
    val orderCodeInput: StateFlow<String> = _orderCodeInput.asStateFlow()

    private val _recipientEmailInput = MutableStateFlow("")
    val recipientEmailInput: StateFlow<String> = _recipientEmailInput.asStateFlow()

    private val _subjectInput = MutableStateFlow("")
    val subjectInput: StateFlow<String> = _subjectInput.asStateFlow()

    private val _notesInput = MutableStateFlow("")
    val notesInput: StateFlow<String> = _notesInput.asStateFlow()

    fun navigateTo(screen: AppScreen) {
        _currentScreen.value = screen
    }

    fun prepareNewPhotoCapture(): Uri {
        val (file, uri) = PhotoFileHelper.createNewPhotoUri(getApplication())
        pendingPhotoFile = file
        pendingPhotoUri = uri
        return uri
    }

    fun onPhotoCaptured(success: Boolean) {
        if (success && pendingPhotoFile != null && pendingPhotoUri != null) {
            val file = pendingPhotoFile!!
            val uri = pendingPhotoUri!!
            initPreviewForm(file, uri)
            _currentScreen.value = AppScreen.Preview(file, uri)
        }
    }

    fun onGalleryPhotoSelected(uri: Uri) {
        viewModelScope.launch {
            val copied = PhotoFileHelper.copyUriToLocalStorage(getApplication(), uri)
            if (copied != null) {
                val (file, photoUri) = copied
                pendingPhotoFile = file
                pendingPhotoUri = photoUri
                initPreviewForm(file, photoUri)
                _currentScreen.value = AppScreen.Preview(file, photoUri)
            } else {
                _uiEvents.emit(UiEvent.ShowSnackbar("No se pudo cargar la imagen seleccionada."))
            }
        }
    }

    private fun initPreviewForm(file: File, uri: Uri) {
        val currentSettings = settings.value
        val nowFormatted = PhotoFileHelper.getFormattedDateForSubject()
        val defaultCode = PhotoFileHelper.generateDefaultOrderCode()

        _orderCodeInput.value = defaultCode
        _recipientEmailInput.value = currentSettings.defaultRecipientEmail
        _subjectInput.value = "${currentSettings.subjectPrefix} - $nowFormatted"
        _notesInput.value = ""
    }

    fun updateOrderCode(code: String) {
        _orderCodeInput.value = code
    }

    fun updateRecipientEmail(email: String) {
        _recipientEmailInput.value = email
    }

    fun updateSubject(subject: String) {
        _subjectInput.value = subject
    }

    fun updateNotes(notes: String) {
        _notesInput.value = notes
    }

    fun acceptAndSendOrder(photoFile: File, photoUri: Uri) {
        viewModelScope.launch {
            val currentSettings = settings.value
            val recipient = _recipientEmailInput.value.trim().ifBlank { currentSettings.defaultRecipientEmail }
            val subject = _subjectInput.value.trim().ifBlank {
                "${currentSettings.subjectPrefix} - ${PhotoFileHelper.getFormattedDateForSubject()}"
            }
            val orderCode = _orderCodeInput.value.trim()
            val notes = _notesInput.value.trim()
            val nowTimestamp = System.currentTimeMillis()
            val formattedDate = PhotoFileHelper.getFormattedDateTime(nowTimestamp)

            // Save to local database
            val order = DeliveryOrder(
                orderCode = orderCode,
                photoPath = photoFile.absolutePath,
                photoUriString = photoUri.toString(),
                timestamp = nowTimestamp,
                formattedDate = formattedDate,
                recipientEmail = recipient,
                ccEmail = currentSettings.ccEmail,
                subject = subject,
                notes = notes,
                isSent = true
            )
            orderRepository.insertOrder(order)

            // Emit launch email intent
            _uiEvents.emit(
                UiEvent.LaunchEmail(
                    recipientEmail = recipient,
                    ccEmail = currentSettings.ccEmail,
                    subject = subject,
                    photoUri = photoUri,
                    orderCode = orderCode,
                    notes = notes,
                    storeName = currentSettings.storeName
                )
            )

            // Success feedback notification
            _uiEvents.emit(UiEvent.ShowSnackbar("¡Foto enviada correctamente!"))

            // Navigate back to Home
            _currentScreen.value = AppScreen.Home
        }
    }

    fun resendPastOrder(order: DeliveryOrder) {
        viewModelScope.launch {
            val photoFile = File(order.photoPath)
            val uri = if (photoFile.exists()) {
                val authority = "${getApplication<Application>().packageName}.fileprovider"
                androidx.core.content.FileProvider.getUriForFile(getApplication(), authority, photoFile)
            } else {
                Uri.parse(order.photoUriString)
            }

            _uiEvents.emit(
                UiEvent.LaunchEmail(
                    recipientEmail = order.recipientEmail,
                    ccEmail = order.ccEmail,
                    subject = order.subject,
                    photoUri = uri,
                    orderCode = order.orderCode,
                    notes = order.notes,
                    storeName = settings.value.storeName
                )
            )
            _uiEvents.emit(UiEvent.ShowSnackbar("Reenviando orden de entrega..."))
        }
    }

    fun deleteOrder(order: DeliveryOrder) {
        viewModelScope.launch {
            try {
                val file = File(order.photoPath)
                if (file.exists()) {
                    file.delete()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            orderRepository.deleteOrder(order)
            _uiEvents.emit(UiEvent.ShowSnackbar("Orden eliminada del historial."))
            if (_currentScreen.value is AppScreen.OrderDetail) {
                _currentScreen.value = AppScreen.History
            }
        }
    }

    fun saveSettings(
        defaultEmail: String,
        ccEmail: String,
        subjectPrefix: String,
        storeName: String,
        appendDetails: Boolean
    ) {
        viewModelScope.launch {
            settingsRepository.updateSettings(
                defaultRecipientEmail = defaultEmail,
                ccEmail = ccEmail,
                subjectPrefix = subjectPrefix,
                storeName = storeName,
                appendOrderDetailsToBody = appendDetails
            )
            _uiEvents.emit(UiEvent.ShowSnackbar("Configuración guardada exitosamente."))
            _currentScreen.value = AppScreen.Home
        }
    }
}
