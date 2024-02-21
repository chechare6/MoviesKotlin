package prueba.pruebamoviesfirebase.login.presentation

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import prueba.pruebamoviesfirebase.login.utils.AnalyticsManager
import prueba.pruebamoviesfirebase.login.utils.AuthManager
import prueba.pruebamoviesfirebase.login.utils.AuthRes
import prueba.pruebamoviesfirebase.movieList.util.Screen
import prueba.pruebamoviesfirebase.ui.theme.Purple40

/**
 * Composable que representa la pantalla de recuperación de contraseña en la aplicación.
 *
 * @param analytics Administrador de análisis para rastreo de eventos.
 * @param auth Administrador de autenticación para gestionar la información del usuario.
 * @param navigation Controlador de navegación para gestionar la navegación entre pantallas.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
    analytics: AnalyticsManager,
    auth: AuthManager,
    navController: NavHostController
) {
    // Registra la vista de pantalla en los análisis.
    analytics.LogScreenView(screenName = Screen.ForgotPassword.route)

    // Obtenemos el contexto actual.
    val context = LocalContext.current

    // Estado para almacenar el correo electrónico introducido por el usuario.
    var email by remember { mutableStateOf("") }

    // Ámbito de la coroutine para lanzar operaciones asíncronas.
    val scope = rememberCoroutineScope()


    // Establecemos el diseño
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Titulo de la pantalla
        Text(
            text = "Olvidó su contraseña",
            style = TextStyle(fontSize = 40.sp, color = Purple40)
        )
        Spacer(modifier = Modifier.height(50.dp))
        // Campo de entrada de correo electrónico.
        TextField(
            label = { Text(text = "Correo electrónico") },
            value = email,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            onValueChange = { email = it })

        Spacer(modifier = Modifier.height(30.dp))
        // Botón para enviar la solicitud de recuperación de contraseña.
        Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
            Button(
                onClick = {
                    scope.launch {
                        // Procesa la solicitud de recuperación de contraseña.
                        when(val res = auth.resetPassword(email)) {
                            is AuthRes.Success -> {
                                // Registra el evento de clic en el botón y muestra un mensaje de éxito.
                                analytics.logButtonClicked(buttonName = "Reset password $email")
                                Toast.makeText(context, "Correo enviado", Toast.LENGTH_SHORT).show()
                                // Navega de vuelta a la pantalla de inicio de sesión.
                                navController.navigate(Screen.LogIn.route)
                            }
                            is AuthRes.Error -> {
                                // Registra un error y muestra un mensaje de error.
                                analytics.logError(error = "Reset password error $email : ${res.errorMessage}")
                                Toast.makeText(context, "Error al enviar el correo", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                },
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                // Texto del boton.
                Text(text = "Recuperar contraseña")
            }
        }
    }
}