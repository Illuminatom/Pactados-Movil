package com.uniandes.pactados.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uniandes.pactados.R
import com.uniandes.pactados.components.BottomNavItem
import com.uniandes.pactados.components.PactadosBottomBar
import com.uniandes.pactados.ui.theme.*

@Composable
fun StreakScreen(
    onHomeClick: () -> Unit,
    onProfileClick: () -> Unit,
    onBackClick: () -> Unit
) {

    var selectedTab by remember { mutableStateOf(2) }

    val navItems = listOf(
        BottomNavItem("Perfil", Icons.Filled.Person),
        BottomNavItem("Inicio", Icons.Filled.Home),
        BottomNavItem("Mi racha", Icons.Filled.Star)
    )

    // Estados para la animacion del circulo
    var isStreakFlipped by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(
        targetValue = if (isStreakFlipped) 180f else 0f,
        animationSpec = tween(durationMillis = 600), // duracion de la animacion
        label = "flipAnimation"
    )

    Scaffold(
        bottomBar = {
            PactadosBottomBar(
                selectedIndex = selectedTab,
                onItemSelected = { index ->
                    selectedTab = index
                    when (index) {
                        0 -> onProfileClick()
                        1 -> onHomeClick()
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
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver",
                        tint = MainOrange
                    )
                }
            }

            // Título Principal
            Text(
                text = "Hola Kevin\nVigila tu progreso",
                color = BlackText,
                fontSize = 40.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center,
                lineHeight = 40.sp,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Contenedor del Circulo y la instruccion con flecha
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                // Circulo animado
                Box(
                    modifier = Modifier
                        .size(180.dp)
                        .clickable { isStreakFlipped = !isStreakFlipped }
                        .graphicsLayer {
                            rotationY = rotation
                            cameraDistance = 12f * density // Efecto de profundidad
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (rotation <= 90f) {
                        // CARA FRONTAL: Imagen con muñeco
                        Image(
                            painter = painterResource(id = R.drawable.streak),
                            contentDescription = "Imagen de racha",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                                .border(4.dp, MainOrange, CircleShape)
                        )
                    } else {
                        // CARA TRASERA: Circulo de progreso
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .graphicsLayer { rotationY = 180f },
                            contentAlignment = Alignment.Center
                        ) {
                            // Hace que el borde no este pintado por completo sino solo 2/3
                            CircularProgressIndicator(
                                progress = 0.66f,
                                modifier = Modifier.fillMaxSize(),
                                color = MainOrange,
                                trackColor = MainOrange.copy(alpha = 0.19f),
                                strokeWidth = 4.dp,
                                strokeCap = StrokeCap.Round
                            )
                            // Texto y Emoji en el centro
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "🔥",
                                    fontSize = 48.sp,
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )
                                Text(
                                    text = "20 días",
                                    color = BlackText,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                // Esto oculta el hint de Oprimir el circulo cuando ya se oprimio
                if (rotation <= 90f) {
                    Column(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .offset(x = (-12).dp, y = (-10).dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Oprime\npara ver\ntu racha",
                            color = BlackText,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            lineHeight = 16.sp
                        )
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Flecha",
                            tint = BlackText,
                            modifier = Modifier
                                .padding(top = 4.dp, end = 24.dp)
                                .size(24.dp)
                                .rotate(-45f)
                        )
                    }
                }
            }

            // Subtitulo
            Text(
                text = "¡Excelente! Tu esfuerzo\nse nota",
                color = BlackText,
                fontSize = 30.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center,
                lineHeight = 32.sp,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Barras de Progreso
            StreakProgressBar(label = "Tomar agua", percentage = 0.5f, percentageText = "50%")
            Spacer(modifier = Modifier.height(24.dp))
            StreakProgressBar(label = "Sacar al perro", percentage = 0.7f, percentageText = "70%")

            Spacer(modifier = Modifier.height(48.dp))

            // Botones
            StreakOutlineButton(text = "Ver Calendario", onClick = { /* TODO: Navegar a Calendario */ })
            Spacer(modifier = Modifier.height(16.dp))
            StreakOutlineButton(text = "Ver Registros", onClick = { /* TODO: Navegar a Registros */ })

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

// ==========================================
// COMPONENTES INTERNOS DE LA PANTALLA
// ==========================================

@Composable
fun StreakProgressBar(label: String, percentage: Float, percentageText: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .clip(RoundedCornerShape(36.dp))
            .background(MainOrange.copy(alpha = 0.19f))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(fraction = percentage)
                .fillMaxHeight()
                .clip(RoundedCornerShape(36.dp))
                .background(MainOrange)
        )

        // Textos superpuestos en ambos extremos
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                color = BlackText,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = percentageText,
                color = BlackText,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun StreakOutlineButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 26.dp)
            .height(64.dp)
            .border(1.dp, BlackText, RoundedCornerShape(12.dp)),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD1A5)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = text,
            color = BlackText,
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium
        )
    }
}