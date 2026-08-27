package com.example.util

import android.content.Context
import android.content.Intent
import android.net.Uri

object EmailHelper {

    fun createSendEmailIntent(
        context: Context,
        recipientEmail: String,
        ccEmail: String,
        subject: String,
        photoUri: Uri,
        orderCode: String,
        notes: String,
        storeName: String
    ): Intent {
        val emailBody = buildString {
            appendLine("Estimado equipo de Logística / Almacén,")
            appendLine()
            appendLine("Se adjunta la fotografía correspondiente al comprobante de la orden de entrega.")
            appendLine()
            appendLine("--------------------------------------------------")
            appendLine("DATOS DE LA ORDEN:")
            if (orderCode.isNotBlank()) {
                appendLine("• Nº de Referencia / Orden: $orderCode")
            }
            appendLine("• Fecha y Hora de Registro: ${PhotoFileHelper.getFormattedDateTime()}")
            if (storeName.isNotBlank()) {
                appendLine("• Tienda / Sucursal: $storeName")
            }
            if (notes.isNotBlank()) {
                appendLine("• Observaciones: $notes")
            }
            appendLine("--------------------------------------------------")
            appendLine()
            appendLine("Saludos cordiales,")
            appendLine("Equipo de Tienda - miCerveza")
            appendLine("POR CERVECERÍA BUCANERO S.A.")
        }

        val sendIntent = Intent(Intent.ACTION_SEND).apply {
            type = "image/jpeg"
            putExtra(Intent.EXTRA_EMAIL, arrayOf(recipientEmail))
            if (ccEmail.isNotBlank()) {
                putExtra(Intent.EXTRA_CC, arrayOf(ccEmail))
            }
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, emailBody)
            putExtra(Intent.EXTRA_STREAM, photoUri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        return Intent.createChooser(sendIntent, "Enviar Orden de Entrega por Correo")
    }
}
