package com.uniandes.pactados.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.uniandes.pactados.components.BottomNavItem
import com.uniandes.pactados.components.PactadosBottomBar
import com.uniandes.pactados.components.AuthButton
import com.uniandes.pactados.components.AuthPopup
import com.uniandes.pactados.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun ProfileScreen(
        onHomeClick: () -> Unit,
        onStreakClick: () -> Unit,
        onLogoutClick: () -> Unit
    ) {
    var selectedTab by remember { mutableStateOf(0) }

    val navItems = listOf(
        BottomNavItem("Perfil", Icons.Filled.Person),
        BottomNavItem("Inicio", Icons.Filled.Home),
        BottomNavItem("Mi racha", Icons.Filled.Star)
    )

    var nombre by remember { mutableStateOf("Kevin Hernandez") }
    var celular by remember { mutableStateOf("3216549870") }
    var contrasena by remember { mutableStateOf("************") }

    // Estados para los pop ups
    var showSavePopup by remember { mutableStateOf(false)}
    var showLogoutPopup by remember { mutableStateOf(false) }
    var isLoggingOut by remember { mutableStateOf(false) }

    // Temporizador de 3 segundos para el popup de guardar
    LaunchedEffect(showSavePopup) {
        if (showSavePopup) {
            delay(3000L)
            showSavePopup = false
        }
    }

    // Temporizador de 3 segundos para el popup de cerrar sesion
    LaunchedEffect(isLoggingOut) {
        if (isLoggingOut) {
            delay(3000L)
            showLogoutPopup = false
            isLoggingOut = false
            onLogoutClick()
        }
    }

    Scaffold(
        bottomBar = {
            PactadosBottomBar(
                selectedIndex = selectedTab,
                onItemSelected = { index ->
                    selectedTab = index
                    when (index) {
                        1 -> onHomeClick()
                        2 -> onStreakClick()
                    }
                },
                items = navItems
            )
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MainOrange)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = "Icono Perfil",
                    tint = BlackText,
                    modifier = Modifier.size(36.dp)
                )
                Spacer(modifier = Modifier.width(90.dp))
                Text(
                    text = "Mi Perfil",
                    color = BlackText,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            // CONTENIDO
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 100.dp)
                    .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                    .background(BackgroundCream)
                    .padding(vertical = 32.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.Start
            ) {

                // Titulos
                Text(
                    text = "Hola Kevin",
                    color = BlackText,
                    fontSize = 48.sp,
                    fontWeight = FontWeight.ExtraBold,
                    lineHeight = 50.sp,
                    modifier = Modifier.padding(horizontal = 24.dp).padding(bottom = 32.dp)
                )

                Text(
                    text = "PERFIL",
                    color = BlackText,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.padding(horizontal = 24.dp).padding(bottom = 24.dp)
                )

                // Formulario
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 40.dp)
                ) {
                    ProfileTextField(
                        value = nombre,
                        onValueChange = { nombre = it },
                        label = "Nombre Completo"
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    ProfileTextField(
                        value = celular,
                        onValueChange = { celular = it },
                        label = "Celular",
                        keyboardType = KeyboardType.Phone
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    ProfileTextField(
                        value = contrasena,
                        onValueChange = { contrasena = it },
                        label = "Contraseña",
                        isPassword = true
                    )

                    Spacer(modifier = Modifier.height(40.dp))

                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        AuthButton(
                            text = "Guardar Cambios",
                            onClick = { showSavePopup = true }
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        TextButton(onClick = { showLogoutPopup = true }) {
                            Text(
                                text = "Cerrar mi\nSesion",
                                color = BlackText,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
        // ==========================================
        // VENTANA EMERGENTE DE GUARDAR CAMBIOS
        // ==========================================
        if (showSavePopup){
            AuthPopup(
                title = "Datos Modificados",
                message = "Tus datos han sido actualizados"
            )
        }
        // ==========================================
        // VENTANA EMERGENTE DE CERRAR SESION
        // ==========================================
        if (showLogoutPopup) {
            AuthPopup(
                title = "Cerrando Sesion",
                message = if (isLoggingOut) "Volviendo al inicio de sesion..." else "¿Estás seguro de que quieres cerrar sesión?"
            ) {
                // Solo mostramos los botones si NO estamos cerrando sesión aún
                if (!isLoggingOut) {
                    Spacer(modifier = Modifier.height(24.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = { isLoggingOut = true },
                            colors = ButtonDefaults.buttonColors(containerColor = MainOrange)
                        ) {
                            Text("Si", color = Color.Black)
                        }
                        Button(
                            onClick = { showLogoutPopup = false },
                            colors = ButtonDefaults.buttonColors(containerColor = MainOrange)
                        ) {
                            Text("No", color = Color.Black)
                        }
                    }
                }
            }
        }
    }
}



// ========================================================
// Componente Interno: Caja de texto con icono de lapiz
// ========================================================
@Composable
fun ProfileTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedBorderColor = OrangeBorder,
            unfocusedBorderColor = OrangeBorder,
            focusedLabelColor = Color.Gray,
            unfocusedLabelColor = Color.Gray
        ),
        trailingIcon = { // Este es el ícono al final del campo
            Icon(
                imageVector = Icons.Filled.Edit,
                contentDescription = "Editar",
                tint = Color.DarkGray
            )
        },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        singleLine = true
    )
}