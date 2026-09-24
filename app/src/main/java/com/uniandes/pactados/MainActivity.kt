package com.uniandes.pactados
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.uniandes.pactados.screens.AlarmsScreen
import com.uniandes.pactados.screens.EditAlarmScreen
import com.uniandes.pactados.screens.HomeScreen
import com.uniandes.pactados.screens.LoginScreen
import com.uniandes.pactados.screens.NewAlarmScreen
import com.uniandes.pactados.screens.ProfileScreen
import com.uniandes.pactados.screens.RecoverPasswordScreen
import com.uniandes.pactados.screens.RegisterScreen
import com.uniandes.pactados.screens.StreakScreen
import com.uniandes.pactados.screens.RecordsScreen
import com.uniandes.pactados.screens.CalendarScreen
import com.uniandes.pactados.screens.ChooseSongScreen
import com.uniandes.pactados.ui.theme.PactadosTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        val controller = WindowCompat.getInsetsController(window, window.decorView)
        controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        controller.hide(WindowInsetsCompat.Type.systemBars())

        setContent {
            PactadosTheme {
                Surface(modifier = Modifier.fillMaxSize()) {

                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = "login"
                    ) {

                        // Pantalla de Login
                        composable("login") {
                            LoginScreen(
                                onRegisterClick = {
                                    navController.navigate("registro")
                                },
                                onRecoverClick = {
                                    navController.navigate("recuperar")
                                },
                                onLoginClick = {
                                    navController.navigate("home")
                                }
                            )
                        }

                        // Pantalla de Registro
                        composable("registro") {
                            RegisterScreen(
                                onBackClick = { navController.popBackStack() },
                                onRegisterSuccess = { navController.popBackStack()
                                }
                            )
                        }

                        // Pantalla de Recuperacion
                        composable("recuperar") {
                            RecoverPasswordScreen(
                                onBackClick = { navController.popBackStack() },
                                onRecoverSuccess = { navController.popBackStack()
                                }
                            )
                        }

                        // Pantalla de inicio
                        composable("home") {
                            HomeScreen(
                                onProfileClick = { navController.navigate("profile")},
                                onStreakClick = { navController.navigate("streak")},
                                onAlarmsClick = { navController.navigate("alarmas")},
                                onCreateAlarmClick = { navController.navigate("nueva_alarma")}
                            )
                        }

                        // Pantalla de Mis alarmas
                        composable("alarmas") {
                            AlarmsScreen(
                                onProfileClick = { navController.navigate("profile")},
                                onHomeClick = { navController.navigate("home")},
                                onStreakClick = { navController.navigate("streak")},
                                onAddClick = { navController.navigate("nueva_alarma")},
                                onEditClick = { id -> navController.navigate("editar_alarma/$id")}
                            )
                        }

                        // Pantalla de Nueva Alarma
                        composable("nueva_alarma") { entry ->
                            val selectedSong by entry.savedStateHandle
                                .getStateFlow<String?>("selected_song", null).collectAsState()
                            NewAlarmScreen(
                                selectedSong = selectedSong,
                                onBackClick = { navController.popBackStack() },
                                onChooseSongClick = { currentSong ->
                                    entry.savedStateHandle["song_initial"] = currentSong
                                    navController.navigate("elegir_cancion")
                                },
                                // Al crear se muestra la lista de alarmas (sin dejar el formulario en la pila)
                                onFinished = {
                                    navController.popBackStack()
                                    if (navController.currentDestination?.route != "alarmas") {
                                        navController.navigate("alarmas")
                                    }
                                }
                            )
                        }

                        // Pantalla de Editar Alarma
                        composable(
                            "editar_alarma/{alarmId}",
                            arguments = listOf(navArgument("alarmId") { type = NavType.IntType })
                        ) { backStackEntry ->
                            val selectedSong by backStackEntry.savedStateHandle
                                .getStateFlow<String?>("selected_song", null).collectAsState()
                            EditAlarmScreen(
                                selectedSong = selectedSong,
                                alarmId = backStackEntry.arguments?.getInt("alarmId") ?: -1,
                                onBackClick = { navController.popBackStack() },
                                onChooseSongClick = { currentSong ->
                                    backStackEntry.savedStateHandle["song_initial"] = currentSong
                                    navController.navigate("elegir_cancion")
                                },
                                onFinished = { navController.popBackStack() }
                            )
                        }

                        composable("elegir_cancion") {
                            ChooseSongScreen(
                                initialSong = navController.previousBackStackEntry
                                    ?.savedStateHandle?.get<String>("song_initial"),
                                onBackClick = { navController.popBackStack() },
                                onSongSelected = { song ->
                                    navController.previousBackStackEntry?.savedStateHandle
                                        ?.set("selected_song", song)
                                    navController.popBackStack()
                                }
                            )
                        }

                        // Pantalla de Perfil
                        composable("profile") {
                            ProfileScreen(
                                onHomeClick = { navController.navigate("home")},
                                onStreakClick = { navController.navigate("streak")},
                                onLogoutClick = {
                                    navController.navigate("login"){
                                        popUpTo(0)
                                    }
                                }
                            )
                        }

                        // Pantalla de Racha
                        composable("streak") {
                            StreakScreen(
                                onHomeClick = { navController.navigate("home")},
                                onProfileClick = {navController.navigate("profile")},
                                onBackClick = { navController.navigate("Home")},
                                onRecordsClick = { navController.navigate("mis_registros") },
                                onCalendarClick = { navController.navigate("calendario") }
                            )
                        }
                        composable("calendario") {
                            CalendarScreen(
                                onBackClick = { navController.popBackStack() },
                                onProfileClick = { navController.navigate("profile") },
                                onHomeClick = { navController.navigate("home") },
                                onStreakClick = {
                                    navController.navigate("streak") {
                                        popUpTo("streak") { inclusive = false }
                                        launchSingleTop = true
                                    }
                                }
                            )
                        }
                        composable("mis_registros") {
                            RecordsScreen(
                                onBackClick = { navController.popBackStack() },
                                onProfileClick = { navController.navigate("profile") },
                                onHomeClick = { navController.navigate("home") },
                                onStreakClick = {
                                    navController.navigate("streak") {
                                        popUpTo("streak") { inclusive = false }
                                        launchSingleTop = true
                                    }
                                }
                            )
                        }
                    }

                }
            }
        }
    }
}
