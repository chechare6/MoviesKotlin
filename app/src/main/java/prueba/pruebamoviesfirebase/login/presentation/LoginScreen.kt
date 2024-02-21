package prueba.pruebamoviesfirebase.login.presentation

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import prueba.pruebamoviesfirebase.R
import prueba.pruebamoviesfirebase.login.utils.AnalyticsManager
import prueba.pruebamoviesfirebase.login.utils.AuthManager
import prueba.pruebamoviesfirebase.login.utils.AuthRes
import prueba.pruebamoviesfirebase.movieList.util.Screen
import prueba.pruebamoviesfirebase.ui.theme.Purple40

/**
 * Composable que representa la pantalla de inicio de sesión en la aplicación.
 *
 * @param analytics Administrador de análisis para rastreo de eventos.
 * @param auth Administrador de autenticación para gestionar la información del usuario.
 * @param navigation Controlador de navegación para gestionar la navegación entre pantallas.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    analytics: AnalyticsManager,
    auth: AuthManager,
    navController: NavHostController
) {
    // Registra la vista de pantalla en los análisis.
    analytics.LogScreenView(screenName = Screen.LogIn.route)

    // Estados para almacenar el correo electrónico y la contraseña introducidos por el usuario.
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Obtiene el contexto actual.
    val context = LocalContext.current
    // Ámbito de la coroutine para lanzar operaciones asíncronas.
    val scope = rememberCoroutineScope()

    //Establecemos el diseño de la pantalla
    Box(modifier = Modifier.fillMaxSize()) {
        // Texto con enlace para redirigir a la pantalla de registro.
        ClickableText(
            text = AnnotatedString("¿No tienes una cuenta? Regístrate"),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(40.dp),
            onClick = {
                navController.navigate(Screen.SignUp.route)
                analytics.logButtonClicked("Click: ¿No tienes una cuenta? Registrate")
            },
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = FontFamily.Default,
                textDecoration = TextDecoration.Underline,
                color = Purple40
            )
        )
    }

    // Contenido principal de la pantalla.
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Logo de la APP.
        Image(
            painter = painterResource(id = R.drawable.ic_movie),
            contentDescription = "Movies Proyect",
            modifier = Modifier.size(100.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        // Título de la aplicación.
        Text(
            text = "Movies Proyect",
            style = TextStyle(fontSize = 30.sp)
        )
        Spacer(modifier = Modifier.height(30.dp))
        // Campos de entrada para correo electrónico y contraseña.
        TextField(
            label = { Text(text = "Correo electrónico") },
            value = email,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            onValueChange = { email = it }
        )
        Spacer(modifier = Modifier.height(10.dp))
        TextField(
            label = { Text(text = "Contraseña") },
            value = password,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            onValueChange = { password = it }
        )
        Spacer(modifier = Modifier.height(20.dp))

        // Botón para iniciar sesión con correo y contraseña.
        Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
            Button(
                onClick = {
                    scope.launch {
                        emailPassSignIn(email, password, auth, analytics, context, navController)
                    }
                },
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = "Iniciar Sesión".uppercase())
            }
        }
        Spacer(modifier = Modifier.height(20.dp))

        // Enlace para redirigir a la pantalla de recuperación de contraseña.
        ClickableText(
            text = AnnotatedString("¿Olvidaste tu contraseña?"),
            onClick = {
                navController.navigate(Screen.ForgotPassword.route) {
                    popUpTo(Screen.LogIn.route) {
                        inclusive = true
                    }
                }
            },
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = FontFamily.Default,
                textDecoration = TextDecoration.Underline,
                color = Purple40
            )
        )
        Spacer(modifier = Modifier.height(25.dp))
        Text(text = "-------- o --------", style = TextStyle(color = Color.Gray))
        Spacer(modifier = Modifier.height(25.dp))

        // Botones para iniciar sesión como invitado y con Google.
        SocialMediaButton(
            onClick = {
                scope.launch {
                    incognitoSignIn(auth, analytics, navController)
                }
            },
            text = "Continuar como invitado",
            icon = R.drawable.ic_incognito,
            color = Color(0xFF363636)
        )
        Spacer(modifier = Modifier.height(15.dp))
        SocialMediaButton(
            onClick = {
                scope.launch {

                }
            },
            text = "Continuar con Google",
            icon = R.drawable.ic_google,
            color = Color(0xFFF1F1F1)
        )
    }
}

/**
 * Función para manejar la autenticación como invitado.
 */
private suspend fun incognitoSignIn(
    auth: AuthManager,
    analytics: AnalyticsManager,
    navController: NavHostController
) {
    when(val result = auth.signInAnonymously()) {
        is AuthRes.Success -> {
            analytics.logButtonClicked("Click: Continuar como invitado")
            navController.navigate(Screen.Home.route) {
                popUpTo(Screen.Home.route) {
                    inclusive = true
                }
            }
        }
        is AuthRes.Error -> {
            analytics.logError("Error SignIn Incognito: ${result.errorMessage}")
        }
    }
}

/**
 * Función para manejar la autenticación con correo electrónico y contraseña.
 */
private suspend fun emailPassSignIn(
    email: String,
    password: String,
    auth: AuthManager,
    analytics: AnalyticsManager,
    context: Context,
    navController: NavHostController
) {
    if (email.isNotEmpty() && password.isNotEmpty()) {
        when (val result = auth.signInWithEmailAndPassword(email, password)) {
            is AuthRes.Success -> {
                analytics.logButtonClicked("Click: Iniciar sesión con correo y contraseña")
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.LogIn.route) {
                        inclusive = true
                    }
                }
            }
            is AuthRes.Error -> {
                analytics.logButtonClicked("Error SignUp: ${result.errorMessage}")
                Toast.makeText(context, "Error SignUp: ${result.errorMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    } else {
        Toast.makeText(context, "Existen campos vacíos", Toast.LENGTH_SHORT).show()
    }
}

/**
 * Composable que representa un botón de inicio de sesión para redes sociales.
 *
 * @param onClick Acción a realizar al hacer clic en el botón.
 * @param text Texto del botón.
 * @param icon Recurso de icono para el botón.
 * @param color Color de fondo del botón.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SocialMediaButton(
    onClick: () -> Unit,
    text: String,
    icon: Int,
    color: Color
    ) {
    // Estado para controlar el clic del botón.
    var click by remember { mutableStateOf(false) }

    // Establecemos el diseño del boton.
    Surface (
        onClick = onClick,
        modifier = Modifier
            .padding(start = 40.dp, end = 40.dp)
            .clickable { click = !click },
        shape = RoundedCornerShape(50),
        border = BorderStroke(width = 1.dp, color = if(icon == R.drawable.ic_incognito) color else Color.Gray),
        color = color
    ) {
        Row(
            modifier = Modifier
                .padding(start = 12.dp, end = 16.dp, top = 12.dp, bottom = 12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            // Icono del botón.
            Icon(
                painterResource(id = icon),
                modifier = Modifier.size(24.dp),
                contentDescription = text,
                tint = Color.Unspecified
            )
            Spacer(modifier = Modifier.width(8.dp))
            // Texto del botón.
            Text(text = text, color = if(icon == R.drawable.ic_incognito) Color.White else Color.Black)
            click = true
        }
    }
}