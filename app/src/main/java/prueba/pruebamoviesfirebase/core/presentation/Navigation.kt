package prueba.pruebamoviesfirebase.core.presentation

import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.firebase.auth.FirebaseUser
import prueba.pruebamoviesfirebase.details.presentation.DetailsScreen
import prueba.pruebamoviesfirebase.login.presentation.ForgotPasswordScreen
import prueba.pruebamoviesfirebase.login.presentation.LoginScreen
import prueba.pruebamoviesfirebase.login.presentation.SignUpScreen
import prueba.pruebamoviesfirebase.login.utils.AnalyticsManager
import prueba.pruebamoviesfirebase.login.utils.AuthManager
import prueba.pruebamoviesfirebase.movieList.util.Screen
import prueba.pruebamoviesfirebase.ui.theme.PruebaMoviesFirebaseTheme

/**
 * Composable que define la estructura de navegación de la aplicación
 * @param context El contexto de la aplicación
 * @param navController Gestiona la navegación entre pantallas
 */
@Composable
fun Navigation(
    context: Context,
    navController: NavHostController = rememberNavController()
) {
    PruebaMoviesFirebaseTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            // Administrador de análisis para rastreo de eventos.
            val analytics = AnalyticsManager(context)

            // Administrador de autenticación para gestionar la información del usuario.
            val authManager = AuthManager()

            // Obtiene la información del usuario actual.
            val user: FirebaseUser? = authManager.getCurrentUser()


            // Configura el controlador de navegación y establece la pantalla de inicio según la autenticación del usuario.
            NavHost(
                navController = navController,
                startDestination = if(user == null) Screen.LogIn.route else Screen.Home.route

            ) {// Pantalla de inicio de sesión.
                composable(Screen.LogIn.route) {
                    LoginScreen(
                        analytics = analytics,
                        auth = authManager,
                        navController = navController
                    )
                }
                // Pantalla de registro.
                composable(Screen.SignUp.route) {
                    SignUpScreen(
                        analytics = analytics,
                        auth = authManager,
                        navController = navController
                    )
                }
                // Pantalla de recuperación de contraseña.
                composable(Screen.ForgotPassword.route) {
                    ForgotPasswordScreen(
                        analytics = analytics,
                        auth = authManager,
                        navController = navController
                    )
                }
                // Pantalla principal.
                composable(Screen.Home.route) {
                    HomeScreen(navController = navController, context)
                }
                // Pantalla detalles de la pelicula.
                composable(
                        Screen.Details.route + "/{movieId}?fromPopular={fromPopular}",
                        arguments = listOf(
                            navArgument("movieId") { type = NavType.IntType },
                            navArgument("fromPopular") { type = NavType.BoolType }
                        )
                    ) {
                    val fromPopular = it.arguments?.getBoolean("fromPopular") ?: false
                    DetailsScreen(fromPopular)
                }
            }
        }
    }
}