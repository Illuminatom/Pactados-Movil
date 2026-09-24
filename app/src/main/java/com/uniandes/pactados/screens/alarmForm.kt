package com.uniandes.pactados.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uniandes.pactados.components.ScreenHeader
import com.uniandes.pactados.data.AlarmRepository
import com.uniandes.pactados.ui.theme.*
import kotlinx.coroutines.delay
import java.util.Calendar

// ==========================================
// PANTALLA NUEVA ALARMA
// ==========================================
@Composable
fun NewAlarmScreen(
    onBackClick: () -> Unit,
    onChooseSongClick: (String?) -> Unit,
    onFinished: () -> Unit,
    selectedSong: String? = null
) {
    val now = remember { Calendar.getInstance() }

    AlarmFormScreen(
        title = "Nueva Alarma",
        submitText = "Crear Alarma",
        submitWidth = 177.dp,
        submitHeight = 60.dp,
        submitFontSize = 22.sp,
        timeCardColor = ClearButtonBackground,
        initialHour = now.get(Calendar.HOUR_OF_DAY),
        initialMinute = now.get(Calendar.MINUTE),
        initialName = "",
        initialDescription = "",
        initialSong = null,
        selectedSong = selectedSong,
        popup = AlarmPopupStyle(
            title = "Alarma Creada",
            message = "Haz creado una alarma nueva",
            titleColor = Color(0xFF454545),
            titleWeight = FontWeight.Normal,
            messageColor = Color(0xFF454545),
            messageWeight = FontWeight.Medium,
            offsetY = 0.dp
        ),
        onBackClick = onBackClick,
        onChooseSongClick = onChooseSongClick,
        onSubmit = { hour, minute, name, description, song ->
            AlarmRepository.add(hour, minute, name, description, song)
        },
        onFinished = onFinished
    )
}

// ==========================================
// PANTALLA EDITAR ALARMA
// ==========================================
@Composable
fun EditAlarmScreen(
    alarmId: Int,
    onBackClick: () -> Unit,
    onChooseSongClick: (String?) -> Unit,
    onFinished: () -> Unit,
    selectedSong: String? = null
) {
    val alarm = remember(alarmId) { AlarmRepository.get(alarmId) }

    // Si la alarma ya no existe se vuelve a la lista
    if (alarm == null) {
        LaunchedEffect(Unit) { onBackClick() }
        return
    }

    AlarmFormScreen(
        title = "Editar Alarma",
        submitText = "Guardar Cambios",
        submitWidth = 174.dp,
        submitHeight = 56.dp,
        submitFontSize = 16.sp,
        timeCardColor = AlarmCardBackground,
        initialHour = alarm.hour,
        initialMinute = alarm.minute,
        initialName = alarm.name,
        initialDescription = alarm.description,
        initialSong = alarm.song,
        selectedSong = selectedSong,
        popup = AlarmPopupStyle(
            title = "Guardado",
            message = "La alarma ha sido modificada\nsatisfactoriamente",
            titleColor = BlackText,
            titleWeight = FontWeight.Medium,
            messageColor = BlackText,
            messageWeight = FontWeight.Bold,
            offsetY = (-59).dp
        ),
        onBackClick = onBackClick,
        onChooseSongClick = onChooseSongClick,
        onSubmit = { hour, minute, name, description, song ->
            AlarmRepository.update(
                alarm.copy(hour = hour, minute = minute, name = name, description = description, song = song)
            )
        },
        onFinished = onFinished
    )
}

// ==========================================
// FORMULARIO COMPARTIDO
// ==========================================
data class AlarmPopupStyle(
    val title: String,
    val message: String,
    val titleColor: Color,
    val titleWeight: FontWeight,
    val messageColor: Color,
    val messageWeight: FontWeight,
    val offsetY: Dp
)

@Composable
fun AlarmFormScreen(
    title: String,
    submitText: String,
    submitWidth: Dp,
    submitHeight: Dp,
    submitFontSize: TextUnit,
    timeCardColor: Color,
    initialHour: Int,
    initialMinute: Int,
    initialName: String,
    initialDescription: String,
    initialSong: String?,
    popup: AlarmPopupStyle,
    onBackClick: () -> Unit,
    onChooseSongClick: (String?) -> Unit,
    onSubmit: (hour: Int, minute: Int, name: String, description: String, song: String?) -> Unit,
    onFinished: () -> Unit,
    selectedSong: String? = null
) {
    var hour by rememberSaveable { mutableIntStateOf(initialHour) }
    var minute by rememberSaveable { mutableIntStateOf(initialMinute) }
    var nombre by rememberSaveable { mutableStateOf(initialName) }
    var descripcion by rememberSaveable { mutableStateOf(initialDescription) }
    var cancion by rememberSaveable { mutableStateOf(initialSong) }

    LaunchedEffect(selectedSong) {
        if (selectedSong != null) cancion = selectedSong
    }

    var nombreError by remember { mutableStateOf(false) }
    var showPopup by remember { mutableStateOf(false) }

    LaunchedEffect(showPopup) {
        if (showPopup) {
            delay(3000L)
            showPopup = false
            onFinished()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundCream)
                .imePadding()
                .blur(radius = if (showPopup) 4.dp else 0.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ScreenHeader(title = title, onBackClick = onBackClick)

            Spacer(modifier = Modifier.height(26.dp))

            AlarmTimeCard(
                hour = hour,
                minute = minute,
                containerColor = timeCardColor,
                onTimeChange = { h, m ->
                    hour = h
                    minute = m
                },
                modifier = Modifier.padding(horizontal = 47.dp)
            )

            Spacer(modifier = Modifier.height(67.dp))

            AlarmTextField(
                value = nombre,
                onValueChange = {
                    nombre = it
                    nombreError = false
                },
                label = "Nombre Alarma",
                height = 56.dp,
                singleLine = true,
                isError = nombreError,
                modifier = Modifier.padding(horizontal = 73.dp)
            )

            // El mensaje de error ocupa el mismo espacio que la separacion del prototipo
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(27.dp)
                    .padding(horizontal = 81.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                if (nombreError) {
                    Text("Este campo es obligatorio", color = Color.Red, fontSize = 10.sp)
                }
            }

            AlarmTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = "Descripcion",
                height = 194.dp,
                singleLine = false,
                modifier = Modifier.padding(horizontal = 73.dp)
            )

            Spacer(modifier = Modifier.height(27.dp))

            ChooseSongButton(
                song = cancion,
                onClick = { onChooseSongClick(cancion) },
                modifier = Modifier.padding(horizontal = 49.dp)
            )

            Spacer(modifier = Modifier.height(27.dp))

            Button(
                onClick = {
                    nombreError = nombre.isBlank()
                    if (!nombreError) {
                        onSubmit(hour, minute, nombre.trim(), descripcion.trim(), cancion)
                        showPopup = true
                    }
                },
                modifier = Modifier
                    .width(submitWidth)
                    .height(submitHeight),
                shape = RoundedCornerShape(24.dp),
                contentPadding = PaddingValues(0.dp),
                colors = ButtonDefaults.buttonColors(containerColor = OrangeText)
            ) {
                Text(
                    text = submitText,
                    color = TitleText,
                    fontSize = submitFontSize,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1
                )
            }

            Spacer(modifier = Modifier.height(13.dp))
        }

        if (showPopup) {
            AlarmPopup(style = popup)
        }
    }
}

// ==========================================
// COMPONENTES INTERNOS DEL FORMULARIO
// ==========================================

// Reloj de entrada (estilo "time input" de Material 3) con Cancel / OK
// y el icono de reloj que abre el selector de esfera.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlarmTimeCard(
    hour: Int,
    minute: Int,
    containerColor: Color,
    onTimeChange: (Int, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    val minuteFocus = remember { FocusRequester() }

    var hourText by remember { mutableStateOf(TextFieldValue("%02d".format(hour))) }
    var minuteText by remember { mutableStateOf(TextFieldValue("%02d".format(minute))) }
    var hourSelected by remember { mutableStateOf(true) }
    var showDial by remember { mutableStateOf(false) }

    // Valores confirmados con OK (Cancel vuelve a ellos)
    var confirmedHour by remember { mutableIntStateOf(hour) }
    var confirmedMinute by remember { mutableIntStateOf(minute) }

    fun setTexts(h: Int, m: Int) {
        hourText = TextFieldValue("%02d".format(h))
        minuteText = TextFieldValue("%02d".format(m))
    }

    fun publish() {
        val h = hourText.text.toIntOrNull()?.coerceIn(0, 23) ?: confirmedHour
        val m = minuteText.text.toIntOrNull()?.coerceIn(0, 59) ?: confirmedMinute
        onTimeChange(h, m)
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(219.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(containerColor)
            .padding(start = 24.dp, end = 24.dp, top = 48.dp, bottom = 24.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            TimeBox(
                value = hourText,
                selected = hourSelected,
                max = 23,
                imeAction = ImeAction.Next,
                onFocused = { hourSelected = true },
                onValueChange = { new, complete ->
                    hourText = new
                    publish()
                    if (complete) minuteFocus.requestFocus()
                },
                onFocusLost = {
                    hourText = TextFieldValue("%02d".format(hourText.text.toIntOrNull() ?: confirmedHour))
                    publish()
                },
                onImeAction = { minuteFocus.requestFocus() },
                modifier = Modifier.weight(1f)
            )
            Box(modifier = Modifier.width(26.dp), contentAlignment = Alignment.Center) {
                Text(text = ":", color = TitleText, fontSize = 48.sp)
            }
            TimeBox(
                value = minuteText,
                selected = !hourSelected,
                max = 59,
                imeAction = ImeAction.Done,
                onFocused = { hourSelected = false },
                onValueChange = { new, complete ->
                    minuteText = new
                    publish()
                    if (complete) focusManager.clearFocus()
                },
                onFocusLost = {
                    minuteText = TextFieldValue("%02d".format(minuteText.text.toIntOrNull() ?: confirmedMinute))
                    publish()
                },
                onImeAction = { focusManager.clearFocus() },
                modifier = Modifier
                    .weight(1f)
                    .focusRequester(minuteFocus)
            )
        }

        Spacer(modifier = Modifier.height(7.dp))

        Row {
            Text("Hour", color = TitleText, fontSize = 12.sp, lineHeight = 16.sp, letterSpacing = 0.4.sp, modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.width(26.dp))
            Text("Minute", color = TitleText, fontSize = 12.sp, lineHeight = 16.sp, letterSpacing = 0.4.sp, modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Schedule,
                contentDescription = "Seleccionar con reloj",
                tint = BlackText,
                modifier = Modifier
                    .size(24.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable {
                        focusManager.clearFocus()
                        showDial = true
                    }
            )
            Spacer(modifier = Modifier.weight(1f))
            TextButton(onClick = {
                focusManager.clearFocus()
                setTexts(confirmedHour, confirmedMinute)
                onTimeChange(confirmedHour, confirmedMinute)
            }) {
                Text("Cancel", color = BlackText, fontSize = 14.sp, fontWeight = FontWeight.Medium, letterSpacing = 0.1.sp)
            }
            Spacer(modifier = Modifier.width(8.dp))
            TextButton(onClick = {
                focusManager.clearFocus()
                confirmedHour = hourText.text.toIntOrNull()?.coerceIn(0, 23) ?: confirmedHour
                confirmedMinute = minuteText.text.toIntOrNull()?.coerceIn(0, 59) ?: confirmedMinute
                setTexts(confirmedHour, confirmedMinute)
                onTimeChange(confirmedHour, confirmedMinute)
            }) {
                Text("OK", color = BlackText, fontSize = 14.sp, fontWeight = FontWeight.Medium, letterSpacing = 0.1.sp)
            }
        }
    }

    if (showDial) {
        val dialState = rememberTimePickerState(
            initialHour = hourText.text.toIntOrNull()?.coerceIn(0, 23) ?: confirmedHour,
            initialMinute = minuteText.text.toIntOrNull()?.coerceIn(0, 59) ?: confirmedMinute,
            is24Hour = true
        )
        AlertDialog(
            onDismissRequest = { showDial = false },
            containerColor = containerColor,
            confirmButton = {
                TextButton(onClick = {
                    confirmedHour = dialState.hour
                    confirmedMinute = dialState.minute
                    setTexts(dialState.hour, dialState.minute)
                    onTimeChange(dialState.hour, dialState.minute)
                    showDial = false
                }) { Text("OK", color = BlackText) }
            },
            dismissButton = {
                TextButton(onClick = { showDial = false }) { Text("Cancel", color = BlackText) }
            },
            text = {
                TimePicker(
                    state = dialState,
                    colors = TimePickerDefaults.colors(
                        clockDialColor = TimeUnselectedBackground,
                        selectorColor = TimeSelectedBackground,
                        timeSelectorSelectedContainerColor = TimeSelectedBackground,
                        timeSelectorSelectedContentColor = Color.White,
                        timeSelectorUnselectedContainerColor = TimeUnselectedBackground,
                        timeSelectorUnselectedContentColor = TitleText
                    )
                )
            }
        )
    }
}

// Casilla de hora o minuto. onValueChange recibe (valor, completo) donde completo = ya tiene 2 digitos.
@Composable
fun TimeBox(
    value: TextFieldValue,
    selected: Boolean,
    max: Int,
    imeAction: ImeAction,
    onFocused: () -> Unit,
    onValueChange: (TextFieldValue, Boolean) -> Unit,
    onFocusLost: () -> Unit,
    onImeAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    val contentColor = if (selected) Color.White else TitleText
    val shape = RoundedCornerShape(8.dp)
    var focused by remember { mutableStateOf(false) }

    BasicTextField(
        value = value,
        onValueChange = { new ->
            var digits = new.text.filter { it.isDigit() }
            // Si ya habia 2 digitos se empieza de nuevo con el digito recien escrito
            if (digits.length > 2) {
                val typed = new.text.getOrNull(new.selection.start - 1)
                digits = if (typed != null && typed.isDigit()) typed.toString() else digits.take(2)
            }
            val number = digits.toIntOrNull()
            if (digits.isEmpty() || (number != null && number <= max)) {
                onValueChange(TextFieldValue(digits, TextRange(digits.length)), digits.length == 2)
            }
        },
        modifier = modifier
            .height(72.dp)
            .onFocusChanged {
                if (it.isFocused) onFocused()
                if (focused && !it.isFocused) onFocusLost()
                focused = it.isFocused
            },
        textStyle = TextStyle(
            color = contentColor,
            fontSize = 48.sp,
            textAlign = TextAlign.Center
        ),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = imeAction),
        keyboardActions = KeyboardActions(onNext = { onImeAction() }, onDone = { onImeAction() }),
        cursorBrush = SolidColor(contentColor),
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(shape)
                    .background(if (selected) TimeSelectedBackground else TimeUnselectedBackground)
                    .then(if (selected) Modifier.border(2.dp, BlackText, shape) else Modifier),
                contentAlignment = Alignment.Center
            ) {
                innerTextField()
            }
        }
    )
}

// Campo con borde naranja y la etiqueta siempre sobre el borde (como en el prototipo)
@Composable
fun AlarmTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    height: Dp,
    singleLine: Boolean,
    isError: Boolean = false,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(4.dp)
    val borderColor = if (isError) Color.Red else OrangeBorder

    Box(modifier = modifier.fillMaxWidth()) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(height),
            textStyle = TextStyle(
                color = GrayText,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.5.sp
            ),
            singleLine = singleLine,
            cursorBrush = SolidColor(OrangeBorder),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White, shape)
                        .border(1.dp, borderColor, shape)
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    contentAlignment = if (singleLine) Alignment.CenterStart else Alignment.TopStart
                ) {
                    innerTextField()
                }
            }
        )

        Text(
            text = label,
            color = if (isError) Color.Red else GrayText,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.4.sp,
            modifier = Modifier
                .offset(x = 12.dp, y = (-8).dp)
                .background(FieldLabelBackground)
                .padding(horizontal = 4.dp)
        )
    }
}

@Composable
fun ChooseSongButton(song: String?, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val shape = RoundedCornerShape(12.dp)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(62.dp)
            .clip(shape)
            .background(ClearButtonBackground)
            .border(1.dp, BlackText, shape)
            .clickable(onClick = onClick),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.MusicNote,
            contentDescription = null,
            tint = TitleText,
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = song ?: "Elegir Cancion",
            color = TitleText,
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 1
        )
    }
}

// Ventana emergente sin oscurecer el fondo (el fondo se difumina)
@Composable
fun AlarmPopup(style: AlarmPopupStyle) {
    val shape = RoundedCornerShape(16.dp)
    Box(
        modifier = Modifier
            .fillMaxSize()
            // Bloquea los toques sobre el formulario mientras se muestra
            .pointerInput(Unit) { detectTapGestures { } },
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .offset(y = style.offsetY)
                .padding(horizontal = 44.dp)
                .fillMaxWidth()
                .height(160.dp)
                .shadow(6.dp, shape)
                .clip(shape)
                .background(PopupBackground)
                .border(1.dp, BlackText, shape),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = style.title,
                color = style.titleColor,
                fontSize = 36.sp,
                lineHeight = 42.sp,
                fontWeight = style.titleWeight,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = style.message,
                color = style.messageColor,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                fontWeight = style.messageWeight,
                textAlign = TextAlign.Center
            )
        }
    }
}
