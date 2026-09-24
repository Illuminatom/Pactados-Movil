package com.uniandes.pactados.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.Normalizer

private data class SongOption(val title: String, val artist: String)
private val SongExamples = listOf(
    SongOption("Shake it Off", "Taylor Swift"),
    SongOption("Despacito", "Luis Fonsi"),
    SongOption("Vivir mi vida", "Marc Anthony"),
    SongOption("Happy", "Pharrell Williams"),
    SongOption("La bicicleta", "Carlos Vives y Shakira"),
    SongOption("Viva la Vida", "Coldplay")
)
private val SongOrange = Color(0xFFECA052)
private val SongCream = Color(0xFFFCF6EE)
private val SongInk = Color(0xFF252222)
private fun String.searchKey() = Normalizer.normalize(trim(), Normalizer.Form.NFD)
    .replace("\\p{M}+".toRegex(), "").lowercase(java.util.Locale.ROOT)

/** Local example catalog. Confirmation returns a title; no audio is bundled. */
@Composable
fun ChooseSongScreen(
    initialSong: String?,
    onBackClick: () -> Unit,
    onSongSelected: (String) -> Unit
) {
    var query by rememberSaveable { mutableStateOf("") }
    var selected by rememberSaveable { mutableStateOf(initialSong) }
    val options = remember(initialSong) {
        if (!initialSong.isNullOrBlank() && SongExamples.none { it.title == initialSong }) {
            listOf(SongOption(initialSong, "Canción actual")) + SongExamples
        } else SongExamples
    }
    val filtered = options.filter { "${it.title} ${it.artist}".searchKey().contains(query.searchKey()) }

    Column(Modifier.fillMaxSize().background(SongCream).imePadding()) {
        Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
            .background(SongOrange).padding(start = 8.dp, end = 24.dp, top = 18.dp, bottom = 24.dp),
            verticalAlignment = Alignment.Top) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver a la alarma", tint = SongInk)
            }
            Text("Elegir\ncanción", Modifier.weight(1f).padding(top = 4.dp), color = SongInk,
                fontSize = 36.sp, lineHeight = 40.sp, fontWeight = FontWeight.Bold)
            Icon(Icons.Filled.MusicNote, null, Modifier.padding(top = 27.dp).size(36.dp), tint = SongInk)
        }
        Text("Dale ritmo a tu hábito", Modifier.padding(start = 24.dp, top = 23.dp),
            color = SongInk, fontSize = 23.sp, fontWeight = FontWeight.Medium)
        Text("Elige una canción para tu alarma", Modifier.padding(start = 24.dp, top = 6.dp),
            color = Color(0xFF655B53), fontSize = 14.sp)
        OutlinedTextField(query, { query = it }, Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 18.dp),
            singleLine = true, label = { Text("Buscar canción o artista") },
            leadingIcon = { Icon(Icons.Filled.Search, null) },
            trailingIcon = {
                if (query.isNotEmpty()) IconButton(onClick = { query = "" }) {
                    Icon(Icons.Filled.Close, "Limpiar búsqueda")
                }
            }, shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = SongOrange,
                unfocusedBorderColor = Color(0xFFE7C8A9), focusedLabelColor = SongInk,
                focusedTextColor = SongInk, unfocusedTextColor = SongInk,
                focusedContainerColor = Color.White, unfocusedContainerColor = Color.White))
        Text("CANCIONES", Modifier.padding(start = 24.dp, bottom = 12.dp),
            color = Color(0xFF79624C), fontSize = 12.sp, letterSpacing = 1.5.sp, fontWeight = FontWeight.Bold)
        if (filtered.isEmpty()) {
            Box(Modifier.weight(1f).fillMaxWidth().padding(24.dp), Alignment.Center) {
                Text("No encontramos canciones.\nPrueba con otro nombre o artista.", color = SongInk)
            }
        } else {
            LazyColumn(Modifier.weight(1f).fillMaxWidth().selectableGroup(),
                contentPadding = PaddingValues(start = 24.dp, end = 24.dp, bottom = 12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(filtered, key = { it.title }) { song ->
                    val isSelected = selected == song.title
                    Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp))
                        .background(if (isSelected) Color(0xFFFFE2BF) else Color.White)
                        .border(if (isSelected) 2.dp else 1.dp,
                            if (isSelected) SongOrange else Color(0xFFEBDDD0), RoundedCornerShape(16.dp))
                        .selectable(isSelected, role = Role.RadioButton, onClick = { selected = song.title })
                        .padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(Modifier.size(44.dp).clip(RoundedCornerShape(12.dp)).background(SongCream), Alignment.Center) {
                            Icon(Icons.Filled.MusicNote, null, tint = SongOrange)
                        }
                        Column(Modifier.weight(1f).padding(horizontal = 12.dp)) {
                            Text(song.title, color = SongInk, fontWeight = FontWeight.Medium, fontSize = 17.sp)
                            Text(song.artist, color = Color(0xFF756B62), fontSize = 12.sp)
                        }
                        if (isSelected) Icon(Icons.Filled.Check, null, tint = SongInk, modifier = Modifier.size(22.dp))
                    }
                }
            }
        }
        Column(Modifier.fillMaxWidth().background(SongCream).padding(horizontal = 24.dp, vertical = 16.dp)) {
            Text(selected?.let { "Seleccionada: $it" } ?: "Selecciona una canción para continuar",
                color = SongInk, fontSize = 13.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Spacer(Modifier.height(10.dp))
            Button(onClick = { selected?.let(onSongSelected) }, enabled = selected != null,
                modifier = Modifier.fillMaxWidth().height(54.dp), shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SongOrange, contentColor = SongInk,
                    disabledContainerColor = Color(0xFFE8D9C8), disabledContentColor = Color(0xFF796F65))) {
                Text("Usar canción", fontSize = 18.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}

@Preview(name = "Elegir canción", widthDp = 393, heightDp = 852, showBackground = true)
@Composable
private fun ChooseSongPreview() {
    ChooseSongScreen("Shake it Off", {}, {})
}
