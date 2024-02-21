package prueba.pruebamoviesfirebase.core.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import prueba.pruebamoviesfirebase.ui.theme.PruebaMoviesFirebaseTheme

/**
 * Clase principal que representa la actividad principal de la aplicación.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    /**
     * Método llamado cuando se crea la actividad.
     *
     * @param savedInstanceState Estado previamente guardado de la actividad.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Establece el contenido de la actividad con el tema de la aplicación y la navegación.
        setContent {
            PruebaMoviesFirebaseTheme {
                Navigation(this)
            }
        }
    }
}