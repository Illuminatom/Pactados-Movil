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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.uniandes.pactados.screens.HomeScreen
import com.uniandes.pactados.screens.LoginScreen
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
                        startDestination = "streak"
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
                                onStreakClick = { navController.navigate("streak")}
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