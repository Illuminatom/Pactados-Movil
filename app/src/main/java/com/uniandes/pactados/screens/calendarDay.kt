package com.uniandes.pactados.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Inbox
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val DayCream = Color(0xFFFCF6EE)
private val DayOrange = Color(0xFFECA052)
private val DayInk = Color(0xFF252222)
private val AugustWeekdays = listOf("Domingo", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado")

/** Demo progress from the reference; the date follows the selected August calendar day. */
@Composable
fun CalendarDayScreen(
    day: Int,
    onBackClick: () -> Unit,
    onProfileClick: () -> Unit,
    onHomeClick: () -> Unit,
    onStreakClick: () -> Unit
) {
    require(day in 1..31)
    BoxWithConstraints(Modifier.fillMaxSize().background(DayCream)) {
        val density = LocalDensity.current
        val scale = maxWidth.value / 379f
        CompositionLocalProvider(LocalDensity provides Density(density.density * scale, density.fontScale)) {
            Column(Modifier.fillMaxSize()) {
                Box(Modifier.fillMaxWidth().height(108.dp)
                    .clip(RoundedCornerShape(bottomStart = 37.dp, bottomEnd = 37.dp)).background(DayOrange)) {
                    Box(Modifier.offset(10.dp, 14.dp).size(48.dp)
                        .clickable(role = Role.Button, onClick = onBackClick), Alignment.Center) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver al calendario", Modifier.size(20.dp), tint = Color(0xFF71634C))
                    }
                    Column(Modifier.offset(55.dp, 26.dp).clickable(role = Role.Button, onClick = onBackClick)) {
                        DayText("Calendario", 42, weight = FontWeight.Bold, lineHeight = 44)
                        Box(Modifier.width(228.dp).height(2.dp).background(Color(0xFF598AAA)))
                    }
                }
                Column(Modifier.weight(1f).fillMaxWidth().verticalScroll(rememberScrollState())
                    .padding(horizontal = 32.dp).padding(top = 14.dp, bottom = 26.dp)) {
                    Text("${AugustWeekdays[(day - 1) % 7]}\n$day\nde Agosto",
                        Modifier.fillMaxWidth(), style = TextStyle(color = DayInk,
                            fontSize = 44.sp, lineHeight = 53.sp, fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.SansSerif, textAlign = TextAlign.Center))
                    Spacer(Modifier.height(21.dp))
                    Box(Modifier.fillMaxWidth().height(4.dp).background(DayOrange))
                    Spacer(Modifier.height(13.dp))
                    DayText("Progreso", 28, lineHeight = 34)
                    DayText("•  Tomar agua: 50%", 20, Modifier.padding(start = 12.dp), FontWeight.Medium)
                    Spacer(Modifier.height(17.dp))
                    DayProgress(0.5f, Modifier.padding(start = 28.dp))
                    Spacer(Modifier.height(20.dp))
                    DayText("•  Sacar al perro: 100%", 20, Modifier.padding(start = 12.dp), FontWeight.Medium)
                    Spacer(Modifier.height(17.dp))
                    DayProgress(1f, Modifier.padding(start = 32.dp))
                    Spacer(Modifier.height(20.dp))
                    DayText("•  Leer la odisea: 0%", 20, Modifier.padding(start = 12.dp), FontWeight.Medium)
                    DayText("=> Alarma Cancelada", 20, Modifier.padding(start = 34.dp), FontWeight.Medium)
                    Spacer(Modifier.height(19.dp))
                    DayText("Reporte Final:", 20, weight = FontWeight.Medium)
                    Spacer(Modifier.height(15.dp))
                    DayText("El $day de Agosto completaste al 100% una tarea ¡Vamos por las demas!", 20, lineHeight = 26)
                }
                Row(Modifier.fillMaxWidth().height(63.dp)
                    .clip(RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp)).background(DayOrange)
                    .padding(horizontal = 35.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    DayNavItem("Perfil", Icons.Outlined.AccountCircle, onProfileClick)
                    DayNavItem("Inicio", Icons.Outlined.Inbox, onHomeClick)
                    DayNavItem("Mi racha", Icons.Outlined.StarBorder, onStreakClick)
                }
            }
        }
    }
}

@Composable
private fun DayProgress(progress: Float, modifier: Modifier) {
    Row(modifier.width(208.dp).height(9.dp).semantics {
        progressBarRangeInfo = ProgressBarRangeInfo(progress, 0f..1f)
    }.clip(RoundedCornerShape(6.dp)).background(Color.White).padding(1.dp),
        horizontalArrangement = Arrangement.spacedBy(3.dp)) {
        Box(Modifier.weight(progress).fillMaxHeight().clip(RoundedCornerShape(5.dp)).background(DayOrange))
        if (progress < 1f) Box(Modifier.weight(1f - progress).fillMaxHeight()
            .clip(RoundedCornerShape(5.dp)).background(Color(0xFFD5D5D5)))
    }
}

@Composable
private fun DayNavItem(label: String, icon: ImageVector, onClick: () -> Unit) {
    Column(Modifier.width(102.dp).fillMaxHeight().clickable(onClick = onClick).padding(top = 15.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, null, Modifier.size(22.dp), tint = Color(0xFF555555))
        Spacer(Modifier.height(5.dp))
        DayText(label, 11, color = Color(0xFF555555))
    }
}

@Composable
private fun DayText(text: String, size: Int, modifier: Modifier = Modifier,
    weight: FontWeight = FontWeight.Normal, color: Color = DayInk, lineHeight: Int = size + 6) {
    Text(text, modifier, style = TextStyle(color = color, fontSize = size.sp,
        lineHeight = lineHeight.sp, fontFamily = FontFamily.SansSerif, fontWeight = weight))
}

@Preview(name = "Detalle del 3 de agosto", widthDp = 379, heightDp = 844, showBackground = true)
@Composable
private fun CalendarDayPreview() {
    CalendarDayScreen(3, {}, {}, {}, {})
}
