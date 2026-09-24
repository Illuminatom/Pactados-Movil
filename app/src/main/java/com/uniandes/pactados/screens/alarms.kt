package com.uniandes.pactados.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.uniandes.pactados.components.BottomNavItem
import com.uniandes.pactados.components.PactadosBottomBar
import com.uniandes.pactados.components.ScreenHeader
import com.uniandes.pactados.data.Alarm
import com.uniandes.pactados.data.AlarmRepository
import com.uniandes.pactados.data.formattedTime
import com.uniandes.pactados.ui.theme.*

@Composable
fun AlarmsScreen(
    onProfileClick: () -> Unit,
    onHomeClick: () -> Unit,
    onStreakClick: () -> Unit,
    onAddClick: () -> Unit,
    onEditClick: (Int) -> Unit
) {
    val navItems = listOf(
        BottomNavItem("Perfil", Icons.Filled.Person),
        BottomNavItem("Inicio", Icons.Filled.Home),
        BottomNavItem("Mi racha", Icons.Filled.Star)
    )

    // Id de la alarma cuyo menu (Editar / Borrar) esta abierto
    var openMenuId by remember { mutableStateOf<Int?>(null) }

    val alarms = AlarmRepository.alarms.sortedBy { it.hour * 60 + it.minute }

    Scaffold(
        // Todo el fondo se difumina mientras el menu esta abierto
        modifier = Modifier.blur(if (openMenuId != null) 3.dp else 0.dp),
        bottomBar = {
            PactadosBottomBar(
                // Mis alarmas no es una pestaña de la barra
                selectedIndex = -1,
                onItemSelected = { index ->
                    when (index) {
                        0 -> onProfileClick()
                        1 -> onHomeClick()
                        2 -> onStreakClick()
                    }
                },
                items = navItems
            )
        },
        containerColor = BackgroundCream
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            ScreenHeader(
                title = "Mis alarmas",
                action = {
                    Icon(
                        imageVector = Icons.Outlined.AddCircleOutline,
                        contentDescription = "Nueva alarma",
                        tint = TitleText,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .clickable(onClick = onAddClick)
                    )
                }
            )

            if (alarms.isEmpty()) {
                Text(
                    text = "Aún no tienes alarmas.\nOprime + para crear una",
                    color = GrayText,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 57.dp, start = 31.dp, end = 31.dp)
                )
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(top = 57.dp, bottom = 26.dp),
                verticalArrangement = Arrangement.spacedBy(26.dp)
            ) {
                items(alarms, key = { it.id }) { alarm ->
                    AlarmItem(
                        alarm = alarm,
                        isMenuOpen = openMenuId == alarm.id,
                        onToggle = { AlarmRepository.setEnabled(alarm.id, it) },
                        onMenuClick = { openMenuId = alarm.id },
                        onDismissMenu = { openMenuId = null },
                        onEdit = {
                            openMenuId = null
                            onEditClick(alarm.id)
                        },
                        onDelete = {
                            openMenuId = null
                            AlarmRepository.delete(alarm.id)
                        }
                    )
                }
            }
        }
    }
}

// ==========================================
// COMPONENTES INTERNOS DE LA PANTALLA
// ==========================================

@Composable
fun AlarmItem(
    alarm: Alarm,
    isMenuOpen: Boolean,
    onToggle: (Boolean) -> Unit,
    onMenuClick: () -> Unit,
    onDismissMenu: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 31.dp, end = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Tarjeta con hora, nombre e interruptor
        Row(
            modifier = Modifier
                .weight(1f)
                .height(84.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(AlarmCardBackground)
                .border(1.dp, BlackText, RoundedCornerShape(8.dp))
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = alarm.formattedTime(),
                    color = TitleText,
                    fontSize = 22.sp,
                    lineHeight = 28.sp
                )
                Text(
                    text = alarm.name,
                    color = TitleText,
                    fontSize = 22.sp,
                    lineHeight = 28.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Switch(
                checked = alarm.enabled,
                onCheckedChange = onToggle,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = MainOrange,
                    checkedBorderColor = Color.Transparent,
                    uncheckedThumbColor = GrayText,
                    uncheckedTrackColor = AlarmCardBackground,
                    uncheckedBorderColor = GrayText
                )
            )
        }

        Spacer(modifier = Modifier.width(13.dp))

        // Boton de menu y su ventana emergente anclada
        Box {
            Icon(
                imageVector = Icons.Filled.Menu,
                contentDescription = "Opciones de la alarma",
                tint = TitleText,
                modifier = Modifier
                    .size(41.dp)
                    .clip(CircleShape)
                    .clickable(onClick = onMenuClick)
            )

            if (isMenuOpen) {
                AlarmOptionsMenu(
                    onDismiss = onDismissMenu,
                    onEdit = onEdit,
                    onDelete = onDelete
                )
            }
        }
    }
}

@Composable
fun AlarmOptionsMenu(
    onDismiss: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    // Posicion del prototipo: borde derecho 23dp antes del icono y 18dp por debajo de su borde superior
    val offset = with(LocalDensity.current) { IntOffset((-23).dp.roundToPx(), 18.dp.roundToPx()) }
    val shape = RoundedCornerShape(12.dp)

    Popup(
        alignment = Alignment.TopEnd,
        offset = offset,
        onDismissRequest = onDismiss,
        properties = PopupProperties(focusable = true)
    ) {
        Column(
            modifier = Modifier
                .width(206.dp)
                .shadow(6.dp, shape)
                .clip(shape)
                .background(MenuBackground)
                .border(1.dp, OrangeBorder, shape)
        ) {
            AlarmMenuOption(icon = Icons.Outlined.Edit, text = "Editar Alarma", onClick = onEdit)
            HorizontalDivider(thickness = 1.dp, color = OrangeBorder)
            AlarmMenuOption(icon = Icons.Outlined.Delete, text = "Borrar Alarma", onClick = onDelete)
        }
    }
}

@Composable
fun AlarmMenuOption(icon: ImageVector, text: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .clickable(onClick = onClick)
            .padding(start = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = GrayText,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = text,
            color = TitleText,
            fontSize = 22.sp
        )
    }
}
