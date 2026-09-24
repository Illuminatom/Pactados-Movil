package com.uniandes.pactados.screens

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.BookmarkBorder
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uniandes.pactados.R

private val RecordsCream = Color(0xFFFCF6EE)
private val RecordsOrange = Color(0xFFECA052)
private val RecordsInk = Color(0xFF20201E)
private val RecordsAccent = Color(0xFFFF881F)

/** Sample record with its matching local photo. */
data class RecordExample(
    val title: String = "Tomar Agua",
    val time: String = "12:00 am",
    val date: String = "Lunes 7 de Septiembre",
    val song: String = "Shake it Off",
    @param:DrawableRes val imageRes: Int = R.drawable.tomar_agua
)

/** Isolated screen: navigation is supplied by the caller; no shared screen is changed. */
@Composable
fun RecordsScreen(
    onBackClick: () -> Unit,
    onProfileClick: () -> Unit,
    onHomeClick: () -> Unit,
    onStreakClick: () -> Unit,
    latestRecord: RecordExample = RecordExample(),
    previousRecords: List<RecordExample> = listOf(
        RecordExample(title = "Leer Libro", imageRes = R.drawable.leer_libro),
        RecordExample(title = "Sacar Perro", imageRes = R.drawable.sacar_perro),
        RecordExample(title = "Tomar Agua", imageRes = R.drawable.tomar_agua),
        RecordExample(title = "Ir al Gimnasio", imageRes = R.drawable.ir_gimnasio),
        RecordExample(title = "Leer Libro", imageRes = R.drawable.leer_libro),
        RecordExample(title = "Sacar Perro", imageRes = R.drawable.sacar_perro)
    )
) {
    // Reference is 295 px wide. Scale the design uniformly to the device width.
    BoxWithConstraints(Modifier.fillMaxSize().background(RecordsCream)) {
        val density = LocalDensity.current
        val scale = maxWidth.value / 295f
        CompositionLocalProvider(LocalDensity provides Density(density.density * scale, density.fontScale)) {
            Column(Modifier.fillMaxSize()) {
                Box(
                    Modifier.fillMaxWidth().height(120.dp)
                        .clip(RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp))
                        .background(RecordsOrange)
                ) {
                    Box(Modifier.offset(3.dp, 8.dp).size(48.dp).clickable(onClick = onBackClick), Alignment.Center) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver", Modifier.size(16.dp), tint = Color(0xFF71634C))
                    }
                    RecordText("Mis\nregistros", 36, Modifier.offset(44.dp, 22.dp), FontWeight.Bold, lineHeight = 41)
                    Icon(Icons.Filled.Folder, null, Modifier.offset(236.dp, 43.dp).size(34.dp), tint = RecordsInk)
                }
                Column(
                    Modifier.weight(1f).fillMaxWidth().verticalScroll(rememberScrollState())
                        .padding(top = 20.dp, bottom = 24.dp)
                ) {
                    Row(Modifier.padding(start = 15.dp), verticalAlignment = Alignment.CenterVertically) {
                        RecordText("⇒", 29)
                        Spacer(Modifier.width(10.dp))
                        RecordText("Tu ultimo registro", 29)
                    }
                    Spacer(Modifier.height(24.dp))
                    RecordPhoto(latestRecord, Modifier.align(Alignment.CenterHorizontally).size(148.dp))
                    Spacer(Modifier.height(16.dp))
                    RecordText("♫ ${latestRecord.song}♫", 12, Modifier.align(Alignment.CenterHorizontally), color = Color(0xFFFF570F))
                    Spacer(Modifier.height(4.dp))
                    Row(Modifier.padding(start = 28.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        RecordText(latestRecord.title, 16, weight = FontWeight.Medium)
                        RecordText(latestRecord.time, 16, weight = FontWeight.Medium)
                    }
                    RecordText(latestRecord.date, 12, Modifier.padding(start = 28.dp))
                    Spacer(Modifier.height(6.dp))
                    Box(Modifier.padding(horizontal = 16.dp).fillMaxWidth().height(0.7.dp).background(Color(0xFFFF7846)))
                    Spacer(Modifier.height(6.dp))
                    Row(Modifier.padding(start = 20.dp), verticalAlignment = Alignment.CenterVertically) {
                        RecordText("⇒", 22, color = RecordsAccent)
                        Spacer(Modifier.width(8.dp))
                        RecordText("Registros anteriores", 22, color = RecordsAccent)
                    }
                    Spacer(Modifier.height(6.dp))
                    Column(Modifier.padding(horizontal = 25.dp), verticalArrangement = Arrangement.spacedBy(9.dp)) {
                        previousRecords.chunked(4).forEach { row ->
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(17.dp)) {
                                row.forEach { record -> RecordPhoto(record, Modifier.size(51.dp)) }
                            }
                        }
                    }
                }
                Row(
                    Modifier.fillMaxWidth().height(49.dp)
                        .clip(RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp))
                        .background(RecordsOrange).padding(horizontal = 28.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    RecordNavItem("Perfil", Icons.Outlined.AccountCircle, onProfileClick)
                    RecordNavItem("Inicio", Icons.Outlined.BookmarkBorder, onHomeClick)
                    RecordNavItem("Mi racha", Icons.Outlined.StarBorder, onStreakClick)
                }
            }
        }
    }
}

@Composable
private fun RecordPhoto(record: RecordExample, modifier: Modifier) {
    Image(
        painterResource(record.imageRes), "Registro: ${record.title}, ${record.date}, ${record.time}",
        modifier.clip(RoundedCornerShape(6.dp)), contentScale = ContentScale.Crop
    )
}

@Composable
private fun RecordNavItem(label: String, icon: ImageVector, onClick: () -> Unit) {
    Column(
        Modifier.width(80.dp).fillMaxHeight().clickable(onClick = onClick).padding(top = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(icon, null, Modifier.size(17.dp), tint = Color.Black)
        Spacer(Modifier.height(4.dp))
        RecordText(label, 9, weight = FontWeight.Medium, color = Color.Black)
    }
}

@Composable
private fun RecordText(
    text: String,
    size: Int,
    modifier: Modifier = Modifier,
    weight: FontWeight = FontWeight.Normal,
    color: Color = RecordsInk,
    lineHeight: Int = size + 3
) {
    Text(text, modifier, style = TextStyle(
        color = color, fontSize = size.sp, lineHeight = lineHeight.sp,
        fontFamily = FontFamily.SansSerif, fontWeight = weight
    ))
}

@Preview(name = "Mis registros · referencia", widthDp = 295, heightDp = 664, showBackground = true)
@Preview(name = "Mis registros · teléfono", widthDp = 393, heightDp = 852, showBackground = true)
@Composable
private fun RecordsScreenPreview() {
    RecordsScreen(onBackClick = {}, onProfileClick = {}, onHomeClick = {}, onStreakClick = {})
}
