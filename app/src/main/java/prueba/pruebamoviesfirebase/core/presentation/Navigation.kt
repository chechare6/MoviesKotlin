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
            var analytics: AnalyticsManager = AnalyticsManager(context)
            val authManager: AuthManager = AuthManager()
            val user: FirebaseUser? = authManager.getCurrentUser()
            NavHost(
                navController = navController,
                startDestination = if(user == null) Screen.LogIn.route else Screen.Home.route

            ) {
                composable(Screen.LogIn.route) {
                    LoginScreen(
                        analytics = analytics,
                        auth = authManager,
                        navController = navController
                    )
                }
                composable(Screen.SignUp.route) {
                    SignUpScreen(
                        analytics = analytics,
                        auth = authManager,
                        navController = navController
                    )
                }
                composable(Screen.ForgotPassword.route) {
                    ForgotPasswordScreen(
                        analytics = analytics,
                        auth = authManager,
                        navController = navController
                    )
                }
                composable(Screen.Home.route) {
                    HomeScreen(navController = navController)
                }
                composable(
                        Screen.Details.route + "/{movieId}",
                        arguments = listOf(
                            navArgument("movieId") { type = NavType.IntType }
                        )
                    ) {
                        DetailsScreen()
                }
            }
        }
    }
}