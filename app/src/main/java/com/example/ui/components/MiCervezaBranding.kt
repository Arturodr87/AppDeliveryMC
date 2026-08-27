package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.BorderSoftGray
import com.example.ui.theme.BucaneroBlue
import com.example.ui.theme.BucaneroBlueDark
import com.example.ui.theme.BucaneroWhite
import com.example.ui.theme.BucaneroYellow

@Composable
fun MiCervezaLogoHeader(
    modifier: Modifier = Modifier,
    showSubtitle: Boolean = true
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        // Beer mug brand icon
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(BucaneroWhite)
                .border(1.dp, BorderSoftGray, RoundedCornerShape(10.dp))
                .padding(4.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_micerveza_mark),
                contentDescription = "miCerveza Logo",
                modifier = Modifier.size(36.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = "mi",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = BucaneroBlue,
                    letterSpacing = (-0.5).sp
                )
                Text(
                    text = "Cerveza",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    color = BucaneroBlue,
                    letterSpacing = (-0.5).sp
                )
            }
            if (showSubtitle) {
                Text(
                    text = "POR CERVECERÍA BUCANERO S.A.",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = BucaneroBlueDark.copy(alpha = 0.85f),
                    letterSpacing = 0.8.sp
                )
            }
        }
    }
}

@Composable
fun BrandHeroBanner(
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp)),
        color = BucaneroBlue,
        shadowElevation = 4.dp
    ) {
        Box(
            modifier = Modifier
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            BucaneroBlue,
                            BucaneroBlueDark
                        )
                    )
                )
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Surface(
                        color = BucaneroYellow,
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = "EQUIPO DE TIENDA",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            color = BucaneroBlueDark,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Órdenes de Entrega",
                        color = BucaneroWhite,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Captura, confirma y envía comprobantes por correo al instante.",
                        color = BucaneroWhite.copy(alpha = 0.85f),
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(BucaneroWhite.copy(alpha = 0.12f))
                        .border(1.dp, BucaneroYellow.copy(alpha = 0.4f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_micerveza_mark),
                        contentDescription = null,
                        modifier = Modifier.size(44.dp)
                    )
                }
            }
        }
    }
}
