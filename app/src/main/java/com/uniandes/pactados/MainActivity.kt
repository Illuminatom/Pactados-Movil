package com.uniandes.pactados
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.uniandes.pactados.screens.LoginScreen
import com.uniandes.pactados.screens.RecoverPasswordScreen
import com.uniandes.pactados.screens.RegisterScreen
import com.uniandes.pactados.ui.theme.PactadosTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                                    // Al hacer clic, navegamos a la ruta "registro"
                                    navController.navigate("registro")
                                },
                                onRecoverClick = {
                                    navController.navigate("recuperar")
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
                    }

                }
            }
        }
    }
}