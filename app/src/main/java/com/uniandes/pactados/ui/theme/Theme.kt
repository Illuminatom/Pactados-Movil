package com.uniandes.pactados.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val PactadosLightColorScheme = lightColorScheme(
    primary = MainOrange,           // FF9C40 - Color principal para botones y la barra de navegación
    onPrimary = BlackText,          // 000000 - El color del texto o iconos que van SOBRE el color primario (botones)
    background = BackgroundCream,   // FFF6F0 - El color de fondo general de las pantallas
    onBackground = BlackText,       // 000000 - Color del texto general sobre el fondo
    surface = BackgroundCream,      // FFF6F0 - Color de fondo de las tarjetas o contenedores planos
    surfaceVariant = PopupBackground, // FFB36C - Usaremos este para las ventanas emergentes o modales
    onSurface = BlackText,          // 000000 - Color de texto sobre las superficies
    primaryContainer = OrangeBorder // FF570F - Podemos usarlo para detalles que requieran ese naranja fuerte
)

@Composable
fun PactadosTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = PactadosLightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}