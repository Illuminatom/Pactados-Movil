package com.uniandes.pactados
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.compose.material3.Surface
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
                        composable("nueva_alarma") {
                            NewAlarmScreen(
                                onBackClick = { navController.popBackStack() },
                                onChooseSongClick = { /* TODO: Navegar a Elegir Cancion */ },
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
                            EditAlarmScreen(
                                alarmId = backStackEntry.arguments?.getInt("alarmId") ?: -1,
                                onBackClick = { navController.popBackStack() },
                                onChooseSongClick = { /* TODO: Navegar a Elegir Cancion */ },
                                onFinished = { navController.popBackStack() }
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
                                onBackClick = { navController.navigate("Home")}
                            )
                        }
                    }

                }
            }
        }
    }
}