package prueba.pruebamoviesfirebase.movieList.util

sealed class Screen(val route: String) {
    //Lista de pantallas que tendremos, aqui debemos "declarar" las pantallas que vamos a utilizar/hacer
    object Home : Screen("home")
    object PopularMovieList : Screen("popularMovie")
    object UpcomingMovieList : Screen("upcomingMovie") //Esta sería la que modificamos según nuestro proyecto?
    object Details : Screen("details")
    object LogIn : Screen("logIn")
    object SignUp : Screen("signUp")
    object ForgotPassword : Screen("forgotPassword")
}