package com.uniandes.pactados
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.uniandes.pactados.screens.LoginScreen
import com.uniandes.pactados.ui.theme.PactadosTheme

// Asegúrate de importar el tema de tu aplicación y la función LoginScreen
// import com.tu.paquete.ui.theme.PactadosTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PactadosTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Llamamos a la pantalla de inicio de sesión
                    LoginScreen()
                }
            }
        }
    }
}