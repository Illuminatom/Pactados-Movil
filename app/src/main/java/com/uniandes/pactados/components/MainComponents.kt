package com.uniandes.pactados.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uniandes.pactados.ui.theme.*

@Composable
fun PactadosBottomBar(
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    items: List<BottomNavItem> // Lista de opciones (Perfil, Inicio, Mi racha)
) {
    Surface(
        shape = RoundedCornerShape(topStart = 30.dp, topEnd = 10.dp),
        color = MainOrange,
        modifier = Modifier.fillMaxWidth()
    ) {
        NavigationBar(
            containerColor = Color.Transparent,
            tonalElevation = 0.dp
        ) {
            items.forEachIndexed { index, item ->
                NavigationBarItem(
                    selected = selectedIndex == index,
                    onClick = { onItemSelected(index) },
                    icon = { Icon(item.icon, contentDescription = item.title) },
                    label = { Text(item.title) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MainOrange,
                        selectedTextColor = BackgroundCream,
                        indicatorColor = BackgroundCream,
                        unselectedIconColor = BlackText,
                        unselectedTextColor = BlackText
                    )
                )
            }
        }
    }
}

data class BottomNavItem(
    val title: String,
    val icon: ImageVector
)

// Encabezado naranja con esquinas inferiores redondeadas (Mis alarmas, Nueva/Editar Alarma...)
// - Con onBackClick: flecha a la izquierda y titulo alineado a la derecha
// - Con action: titulo a la izquierda y la accion (ej. boton +) a la derecha
@Composable
fun ScreenHeader(
    title: String,
    onBackClick: (() -> Unit)? = null,
    action: (@Composable () -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .clip(RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp))
            .background(MainOrange)
            .padding(
                start = if (onBackClick != null) 14.dp else 40.dp,
                end = if (action != null) 38.dp else 26.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (onBackClick != null) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver",
                    tint = TitleText
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
        }

        // Se reduce solo si el titulo no cabe en pantallas angostas
        BasicText(
            text = title,
            modifier = Modifier.weight(1f),
            style = TextStyle(
                fontFamily = InterExtraBold,
                fontWeight = FontWeight.ExtraBold,
                color = TitleText,
                textAlign = if (onBackClick != null) TextAlign.End else TextAlign.Start
            ),
            maxLines = 1,
            autoSize = TextAutoSize.StepBased(minFontSize = 32.sp, maxFontSize = 48.sp, stepSize = 1.sp)
        )

        if (action != null) {
            action()
        }
    }
}