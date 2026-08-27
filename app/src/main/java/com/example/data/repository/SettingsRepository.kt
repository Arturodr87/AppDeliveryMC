package com.example.data.repository

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class AppSettings(
    val defaultRecipientEmail: String = "logistica@mitienda.com",
    val ccEmail: String = "",
    val subjectPrefix: String = "Orden de Entrega",
    val storeName: String = "Tienda Bucanero",
    val appendOrderDetailsToBody: Boolean = true
)

class SettingsRepository(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("micerveza_settings", Context.MODE_PRIVATE)

    private val _settings = MutableStateFlow(loadSettings())
    val settings: StateFlow<AppSettings> = _settings.asStateFlow()

    private fun loadSettings(): AppSettings {
        return AppSettings(
            defaultRecipientEmail = prefs.getString(KEY_DEFAULT_EMAIL, "logistica@mitienda.com")
                ?.takeIf { it.isNotBlank() } ?: "logistica@mitienda.com",
            ccEmail = prefs.getString(KEY_CC_EMAIL, "") ?: "",
            subjectPrefix = prefs.getString(KEY_SUBJECT_PREFIX, "Orden de Entrega")
                ?.takeIf { it.isNotBlank() } ?: "Orden de Entrega",
            storeName = prefs.getString(KEY_STORE_NAME, "Tienda Bucanero")
                ?.takeIf { it.isNotBlank() } ?: "Tienda Bucanero",
            appendOrderDetailsToBody = prefs.getBoolean(KEY_APPEND_DETAILS, true)
        )
    }

    fun updateSettings(
        defaultRecipientEmail: String,
        ccEmail: String,
        subjectPrefix: String,
        storeName: String,
        appendOrderDetailsToBody: Boolean = true
    ) {
        val cleanEmail = defaultRecipientEmail.trim().ifBlank { "logistica@mitienda.com" }
        val cleanPrefix = subjectPrefix.trim().ifBlank { "Orden de Entrega" }
        val cleanStore = storeName.trim().ifBlank { "Tienda Bucanero" }

        prefs.edit()
            .putString(KEY_DEFAULT_EMAIL, cleanEmail)
            .putString(KEY_CC_EMAIL, ccEmail.trim())
            .putString(KEY_SUBJECT_PREFIX, cleanPrefix)
            .putString(KEY_STORE_NAME, cleanStore)
            .putBoolean(KEY_APPEND_DETAILS, appendOrderDetailsToBody)
            .apply()

        _settings.value = AppSettings(
            defaultRecipientEmail = cleanEmail,
            ccEmail = ccEmail.trim(),
            subjectPrefix = cleanPrefix,
            storeName = cleanStore,
            appendOrderDetailsToBody = appendOrderDetailsToBody
        )
    }

    companion object {
        private const val KEY_DEFAULT_EMAIL = "key_default_email"
        private const val KEY_CC_EMAIL = "key_cc_email"
        private const val KEY_SUBJECT_PREFIX = "key_subject_prefix"
        private const val KEY_STORE_NAME = "key_store_name"
        private const val KEY_APPEND_DETAILS = "key_append_details"
    }
}
