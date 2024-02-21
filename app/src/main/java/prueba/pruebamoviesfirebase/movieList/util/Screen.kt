package prueba.pruebamoviesfirebase.movieList.util

/**
 * sealed class que representa las diferentes rutas de navegación
 * en la aplicación
 * @property route Nombre de la ruta de la navegación
 */
sealed class Screen(val route: String) {
    object Home : Screen("home")
    object PopularMovieList : Screen("popularMovie")
    object NowPlayingMovieList : Screen("nowPlayingMovie")
    object FavoritesMovieList : Screen("favoritesMovies")
    object Details : Screen("details")
    object LogIn : Screen("logIn")
    object SignUp : Screen("signUp")
    object ForgotPassword : Screen("forgotPassword")
}