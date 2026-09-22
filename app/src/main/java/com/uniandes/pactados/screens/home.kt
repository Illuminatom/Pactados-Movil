package com.uniandes.pactados.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Alarm
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uniandes.pactados.R
import com.uniandes.pactados.components.AuthButton
import com.uniandes.pactados.components.BottomNavItem
import com.uniandes.pactados.components.PactadosBottomBar
import com.uniandes.pactados.ui.theme.*

@Composable
fun HomeScreen() {
    var selectedTab by remember { mutableStateOf(1) }

    val navItems = listOf(
        BottomNavItem("Perfil", Icons.Filled.Person),
        BottomNavItem("Inicio", Icons.Filled.Home),
        BottomNavItem("Mi racha", Icons.Filled.Star)
    )

    Scaffold(
        bottomBar = {
            PactadosBottomBar(
                selectedIndex = selectedTab,
                onItemSelected = { selectedTab = it },
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

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.pactadoslogo),
                    contentDescription = "Logo de Pactados",
                    modifier = Modifier.fillMaxWidth(0.8f),
                    contentScale = ContentScale.Fit
                )
            }


            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 130.dp)
                    .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
                    .background(BackgroundCream)
                    .padding(32.dp),
                horizontalAlignment = Alignment.Start
            ) {

                Text(
                    text = "Bienvenido",
                    color = BlackText,
                    fontSize = 48.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = "¡Revisa tus alarmas!",
                    color = BlackText,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(2.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.CheckCircle,
                            contentDescription = "Al día",
                            tint = BlackText,
                            modifier = Modifier.size(30.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "ESTAS AL DIA",
                            color = BlackText,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Light
                        )
                    }

                    MetricCard(
                        title = "Alarmas Activas",
                        value = "4",
                        onClick = { /* TODO: Navegar a Alarmas */ }
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    MetricCard(
                        title = "Alarmas Pendientes",
                        value = "0",
                        onClick = { /* En teoria esto no lelva a ningun lado */ }
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        AuthButton(
                            text = "Crear Alarma",
                            onClick = { /* TODO: Navegar a crear */ }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MetricCard(title: String, value: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(containerColor = PopupBackground),
        contentPadding = PaddingValues(horizontal = 24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.Alarm,
                contentDescription = null,
                tint = BlackText
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = title,
                color = BlackText,
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = value,
                color = BlackText,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}