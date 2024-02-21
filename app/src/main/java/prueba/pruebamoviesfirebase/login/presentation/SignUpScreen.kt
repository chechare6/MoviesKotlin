package prueba.pruebamoviesfirebase.login.presentation

import android.content.Context
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
import androidx.compose.foundation.text.ClickableText
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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.coroutines.launch
import prueba.pruebamoviesfirebase.login.utils.AnalyticsManager
import prueba.pruebamoviesfirebase.login.utils.AuthManager
import prueba.pruebamoviesfirebase.login.utils.AuthRes
import prueba.pruebamoviesfirebase.movieList.util.Screen
import prueba.pruebamoviesfirebase.ui.theme.Purple40

/**
 * Composable que representa la pantalla de registro en la aplicación.
 *
 * @param analytics Administrador de análisis para rastreo de eventos.
 * @param auth Administrador de autenticación para gestionar la información del usuario.
 * @param navigation Controlador de navegación para gestionar la navegación entre pantallas.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen (
    analytics: AnalyticsManager,
    auth: AuthManager,
    navController: NavHostController
) {
    // Registra la vista de pantalla en los análisis.
    analytics.LogScreenView(screenName = Screen.SignUp.route)

    // Obtiene el contexto actual.
    val context = LocalContext.current

    // Estados para almacenar el correo electrónico y la contraseña introducidos por el usuario.
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Ámbito de la coroutine para lanzar operaciones asíncronas.
    val scope = rememberCoroutineScope()

    // Establecemos el diseño de la pantalla
    Column(
        modifier= Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Título de la pantalla.
        Text(
            text = "Crear cuenta",
            style = TextStyle(fontSize = 40.sp, color = Purple40)
        )
        Spacer(modifier = Modifier.height(50.dp))

        // Campos de entrada para correo electrónico y contraseña.
        TextField(
            label = { Text(text = "Correo electrónico") },
            value = email,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            onValueChange = { email = it }
        )
        Spacer(modifier = Modifier.height(20.dp))
        TextField(
            label = { Text(text = "Contraseña") },
            value = password,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            onValueChange = { password = it }
        )
        Spacer(modifier = Modifier.height(30.dp))

        // Botón para realizar el registro.
        Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
            Button(
                onClick = {
                    scope.launch {
                        signUp(email, password, auth, analytics, context, navController)
                    }
                },
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = "Registrarse")
            }
        }
        Spacer(modifier = Modifier.height(40.dp))

        // Enlace para redirigir a la pantalla de inicio de sesión.
        ClickableText(
            text = AnnotatedString("¿Ya tienes cuenta? Inicia sesión"),
            onClick = {
                navController.popBackStack()
            },
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = FontFamily.Default,
                textDecoration = TextDecoration.Underline,
                color = Purple40
            )
        )
    }
}

/**
 * Función para manejar el registro con correo electrónico y contraseña.
 */
private suspend fun signUp(email: String, password: String, auth: AuthManager, analytics: AnalyticsManager, context: Context, navController: NavHostController) {
    if(email.isNotEmpty() && password.isNotEmpty()) {
        // Realiza la operación de registro y gestiona el resultado.
        when(val result = auth.createUserWithEmailAndPassword(email, password)) {
            is AuthRes.Success -> {
                // Registra el evento de registro exitoso y muestra un mensaje.
                analytics.logButtonClicked(FirebaseAnalytics.Event.SIGN_UP)
                Toast.makeText(context, "Registro exitoso", Toast.LENGTH_SHORT).show()

                // Regresa a la pantalla de inicio de sesión.
                navController.popBackStack()
            }
            is AuthRes.Error -> {
                // Registra el evento de error y muestra un mensaje de error.
                analytics.logButtonClicked("Error SignUp: ${result.errorMessage}")
                Toast.makeText(context, "Error SignUp: ${result.errorMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    } else {
        Toast.makeText(context, "Existen campos vacios", Toast.LENGTH_SHORT).show()
    }
}