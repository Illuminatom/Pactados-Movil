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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uniandes.pactados.components.AuthButton
import com.uniandes.pactados.components.AuthLogo
import com.uniandes.pactados.components.AuthTextField
import com.uniandes.pactados.ui.theme.*

@Composable
fun LoginScreen(
    onRegisterClick: () -> Unit,
    onRecoverClick: () -> Unit,
    onLoginClick: () -> Unit
) {
    var celular by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }

    // Estados de error
    var celularError by remember { mutableStateOf(false) }
    var contrasenaError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundCream)
            .systemBarsPadding()
            .imePadding()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        AuthLogo()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(0.85f),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Bienvenido",
                    color = OrangeText,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
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
                    value = contrasena,
                    onValueChange = {
                        contrasena = it
                        contrasenaError = false
                    },
                    label = "Contraseña",
                    placeholder = "Contraseña",
                    isError = contrasenaError,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )
                Box(modifier = Modifier.height(16.dp).padding(start = 8.dp), contentAlignment = Alignment.CenterStart) {
                    if (contrasenaError) {
                        Text("Este campo es obligatorio", color = Color.Red, fontSize = 10.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            AuthButton(
                text = "Iniciar Sesión",
                onClick = {
                    celularError = celular.isBlank()
                    contrasenaError = contrasena.isBlank()

                    if (!celularError && !contrasenaError) {
                        onLoginClick()
                    }
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            TextButton(onClick = onRecoverClick) {
                Text(
                    text = "¿Olvidaste tu\ncontraseña?",
                    color = BlackText,
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(onClick = onRegisterClick ) {
                Text(
                    text = "Crea tu\ncuenta",
                    color = BlackText,
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
            }
        }
    }
}