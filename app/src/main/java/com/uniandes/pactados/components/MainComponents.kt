package com.uniandes.pactados.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
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