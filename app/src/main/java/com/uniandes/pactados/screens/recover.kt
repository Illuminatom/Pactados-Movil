package com.uniandes.pactados.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uniandes.pactados.components.AuthButton
import com.uniandes.pactados.components.AuthLogo
import com.uniandes.pactados.components.AuthPopup
import com.uniandes.pactados.components.AuthTextField
import com.uniandes.pactados.components.BackButton
import kotlinx.coroutines.delay
import com.uniandes.pactados.ui.theme.*

@Composable
fun RecoverPasswordScreen(
    onBackClick: () -> Unit,
    onRecoverSuccess: () -> Unit
) {
    var celular by remember { mutableStateOf("") }
    var nuevaContrasena by remember { mutableStateOf("") }
    var confirmarContrasena by remember { mutableStateOf("") }

    var celularError by remember { mutableStateOf(false) }
    var nuevaContrasenaError by remember { mutableStateOf(false) }
    var confirmarContrasenaError by remember { mutableStateOf(false) }

    var showPopup by remember { mutableStateOf(false) }

    LaunchedEffect(showPopup) {
        if (showPopup) {
            delay(3000L)
            showPopup = false
            onRecoverSuccess()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundCream)
                .systemBarsPadding()
                .imePadding()
                .blur(radius = if (showPopup) 12.dp else 0.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            AuthLogo()

            Column(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Recuperar Contraseña",
                    color = OrangeText,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                AuthTextField(
                    value = celular,
                    onValueChange = {
                        celular = it
                        celularError = false
                    },
                    label = "Celular",
                    placeholder = "Numero de Celular",
                    isError = celularError,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                )
                Box(modifier = Modifier.height(16.dp).padding(start = 8.dp), contentAlignment = Alignment.CenterStart) {
                    if (celularError) {
                        Text("Este campo es obligatorio", color = Color.Red, fontSize = 10.sp)
                    }
                }

                AuthTextField(
                    value = nuevaContrasena,
                    onValueChange = {
                        nuevaContrasena = it
                        nuevaContrasenaError = false
                    },
                    label = "Nueva Contraseña",
                    placeholder = "Contraseña",
                    isError = nuevaContrasenaError,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )
                Box(modifier = Modifier.height(16.dp).padding(start = 8.dp), contentAlignment = Alignment.CenterStart) {
                    if (nuevaContrasenaError) {
                        Text("Este campo es obligatorio", color = Color.Red, fontSize = 10.sp)
                    }
                }

                AuthTextField(
                    value = confirmarContrasena,
                    onValueChange = {
                        confirmarContrasena = it
                        confirmarContrasenaError = false
                    },
                    label = "Confirmacion de Contraseña",
                    placeholder = "Confirmacion",
                    isError = confirmarContrasenaError,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )
                Box(modifier = Modifier.height(16.dp).padding(start = 8.dp), contentAlignment = Alignment.CenterStart) {
                    if (confirmarContrasenaError) {
                        Text("Este campo es obligatorio", color = Color.Red, fontSize = 10.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            AuthButton(
                text = "Recuperar Contraseña",
                onClick = {
                    celularError = celular.isBlank()
                    nuevaContrasenaError = nuevaContrasena.isBlank()
                    confirmarContrasenaError =
                        confirmarContrasena.isBlank() || (nuevaContrasena != confirmarContrasena)

                    if (!celularError && !nuevaContrasenaError && !confirmarContrasenaError) {
                        showPopup = true
                    }
                }
            )

            Spacer(modifier = Modifier.height(32.dp))
        }

        // Flecha de retroceso absoluta fuera del scroll
        BackButton(onClick = onBackClick)

        if (showPopup) {
            AuthPopup(
                title = "Mensaje Enviado",
                message = "Te enviamos un mensaje a tu celular para recuperar tu contraseña",
            )
        }
    }
}