package com.uniandes.pactados.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Inbox
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.uniandes.pactados.data.CalendarHabit
import com.uniandes.pactados.data.calendarHabitExamples

private val CalendarCream = Color(0xFFFCF6EE)
private val CalendarOrange = Color(0xFFECA052)
private val CalendarInk = Color(0xFF252222)

@Composable
fun CalendarScreen(
    onBackClick: () -> Unit,
    onProfileClick: () -> Unit,
    onHomeClick: () -> Unit,
    onStreakClick: () -> Unit
) {
    var selectedId by rememberSaveable { mutableStateOf("water") }
    var filterOpen by rememberSaveable { mutableStateOf(false) }
    var selectedDay by rememberSaveable { mutableStateOf<Int?>(null) }
    val habit = calendarHabitExamples.first { it.id == selectedId }
    val day = selectedDay
    if (day != null) {
        BackHandler { selectedDay = null }
        CalendarDayScreen(day, { selectedDay = null }, onProfileClick, onHomeClick, onStreakClick)
        return
    }
    CalendarContent(habit, filterOpen, { filterOpen = it }, {
        selectedId = it.id
        filterOpen = false
    }, onBackClick, onProfileClick, onHomeClick, onStreakClick, { selectedDay = it })
}

@Composable
private fun CalendarContent(
    habit: CalendarHabit,
    filterOpen: Boolean,
    onFilterChange: (Boolean) -> Unit,
    onHabitSelected: (CalendarHabit) -> Unit,
    onBackClick: () -> Unit,
    onProfileClick: () -> Unit,
    onHomeClick: () -> Unit,
    onStreakClick: () -> Unit,
    onDayClick: (Int) -> Unit = {}
) {
    BackHandler(filterOpen) { onFilterChange(false) }
    BoxWithConstraints(Modifier.fillMaxSize().background(CalendarCream)) {
        val density = LocalDensity.current
        val scale = maxWidth.value / 354f
        CompositionLocalProvider(LocalDensity provides Density(density.density * scale, density.fontScale)) {
            Box(Modifier.fillMaxSize()) {
                Column(Modifier.fillMaxSize().blur(if (filterOpen) 2.dp else 0.dp)) {
                    Box(Modifier.fillMaxWidth().height(142.dp)
                        .clip(RoundedCornerShape(bottomStart = 34.dp, bottomEnd = 34.dp))
                        .background(CalendarOrange)) {
                        Box(Modifier.offset(7.dp, 12.dp).size(48.dp).clickable(onClick = onBackClick), Alignment.Center) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver", Modifier.size(19.dp), tint = Color(0xFF71634C))
                        }
                        CalendarText("Calendario", 42, Modifier.offset(53.dp, 24.dp), FontWeight.Bold)
                        Icon(Icons.Outlined.CalendarMonth, null, Modifier.offset(283.dp, 33.dp).size(47.dp), tint = Color.Black)
                        CalendarText("- ${habit.title}", 27, Modifier.offset(54.dp, 86.dp))
                        Box(Modifier.offset(282.dp, 76.dp).size(48.dp)
                            .clickable(role = Role.Button, onClickLabel = "Filtrar por hábito") { onFilterChange(true) }, Alignment.Center) {
                            Icon(Icons.Filled.Menu, "Filtrar por hábito", Modifier.size(33.dp), tint = CalendarInk)
                        }
                    }
                    Column(Modifier.weight(1f).fillMaxWidth().verticalScroll(rememberScrollState())
                        .padding(horizontal = 18.dp).padding(top = 14.dp, bottom = 24.dp)) {
                        CalendarText("Agosto", 36, Modifier.padding(start = 4.dp))
                        Spacer(Modifier.height(4.dp))
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            listOf("D", "L", "M", "X", "J", "V", "S").forEach { day ->
                                Box(Modifier.size(31.dp).clip(RoundedCornerShape(8.dp)).background(Color(0xFFEC8C37)), Alignment.Center) {
                                    CalendarText(day, 26, color = Color.White)
                                }
                            }
                        }
                        Spacer(Modifier.height(17.dp))
                        (0 until 5).forEach { week ->
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                (1..7).forEach { weekday ->
                                    val day = week * 7 + weekday
                                    if (day <= 31) CalendarDay(day, day in habit.postponedDays) { onDayClick(day) }
                                    else Spacer(Modifier.width(31.dp).height(49.dp))
                                }
                            }
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            CalendarText("✓", 27, weight = FontWeight.Bold, color = Color(0xFF50504E))
                            CalendarText(" Cumplida", 20, weight = FontWeight.Medium)
                        }
                        Spacer(Modifier.height(21.dp))
                        CalendarText("- Pospuesta", 20, weight = FontWeight.Medium)
                        Spacer(Modifier.height(13.dp))
                        Box(Modifier.fillMaxWidth().height(1.dp).background(Color(0xFFFF8955)))
                        Spacer(Modifier.height(12.dp))
                        CalendarText("El ${habit.postponedPercentage}% de las alarmas fueron\npospuestas ¡Esfuerzate mas!", 19, lineHeight = 24)
                    }
                    Row(Modifier.fillMaxWidth().height(59.dp)
                        .clip(RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp))
                        .background(CalendarOrange).padding(horizontal = 33.dp),
                        horizontalArrangement = Arrangement.SpaceBetween) {
                        CalendarNavItem("Perfil", Icons.Outlined.AccountCircle, onProfileClick)
                        CalendarNavItem("Inicio", Icons.Outlined.Inbox, onHomeClick)
                        CalendarNavItem("Mi racha", Icons.Outlined.StarBorder, onStreakClick)
                    }
                }
                if (filterOpen) {
                    Popup(alignment = Alignment.TopStart,
                        onDismissRequest = { onFilterChange(false) },
                        properties = PopupProperties(focusable = true)) {
                        // Full-screen scrim catches outside taps; the card stays anchored under the title.
                        Box(Modifier.fillMaxSize().clickable { onFilterChange(false) }) {
                            Column(Modifier.padding(top = 102.dp).width(321.dp)
                                .clip(RoundedCornerShape(18.dp)).background(Color.White)
                                .border(1.dp, CalendarInk, RoundedCornerShape(18.dp)).selectableGroup()) {
                                calendarHabitExamples.forEach { option ->
                                    val selected = option.id == habit.id
                                    Row(Modifier.fillMaxWidth().height(51.dp)
                                        .background(if (selected) Color(0xFFEEEEEE) else Color.White)
                                        .selectable(selected, role = Role.RadioButton, onClick = { onHabitSelected(option) })
                                        .padding(start = 63.dp, end = 25.dp),
                                        verticalAlignment = Alignment.CenterVertically) {
                                        CalendarText(option.filterLabel, 15, Modifier.weight(1f))
                                        if (selected) Icon(Icons.Filled.CheckBox, null, Modifier.size(19.dp), tint = Color.Black)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CalendarDay(day: Int, postponed: Boolean, onClick: () -> Unit) {
    Box(Modifier.width(31.dp).height(49.dp)
        .clickable(role = Role.Button, onClickLabel = "Ver progreso del día", onClick = onClick)
        .semantics(mergeDescendants = true) {
        contentDescription = "$day de agosto: ${if (postponed) "Pospuesta" else "Cumplida"}"
    }) {
        Box(Modifier.padding(top = 8.dp).size(31.dp).clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFF3CA94)), Alignment.Center) {
            CalendarText(day.toString(), 26, color = Color.Black)
        }
        CalendarText(if (postponed) "-" else "✓", if (postponed) 22 else 30,
            Modifier.offset(x = 22.dp, y = (-10).dp), FontWeight.Bold,
            if (postponed) Color(0xFF5664D9) else Color(0xFF50504E))
    }
}

@Composable
private fun CalendarNavItem(label: String, icon: ImageVector, onClick: () -> Unit) {
    Column(Modifier.width(96.dp).fillMaxHeight().clickable(onClick = onClick).padding(top = 14.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, null, Modifier.size(21.dp), tint = Color.Black)
        Spacer(Modifier.height(5.dp))
        CalendarText(label, 11, weight = FontWeight.Medium, color = Color.Black)
    }
}

@Composable
private fun CalendarText(text: String, size: Int, modifier: Modifier = Modifier,
    weight: FontWeight = FontWeight.Normal, color: Color = CalendarInk, lineHeight: Int = size + 4) {
    Text(text, modifier, style = TextStyle(color = color, fontSize = size.sp,
        lineHeight = lineHeight.sp, fontFamily = FontFamily.SansSerif, fontWeight = weight))
}

@Preview(name = "Calendario", widthDp = 354, heightDp = 790, showBackground = true)
@Composable
private fun CalendarPreview() {
    CalendarScreen({}, {}, {}, {})
}

@Preview(name = "Filtro por hábito", widthDp = 354, heightDp = 790, showBackground = true)
@Composable
private fun CalendarFilterPreview() {
    CalendarContent(calendarHabitExamples.first(), true, {}, {}, {}, {}, {}, {})
}
