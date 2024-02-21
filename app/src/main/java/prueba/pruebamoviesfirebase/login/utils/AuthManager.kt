package prueba.pruebamoviesfirebase.login.utils

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import kotlinx.coroutines.tasks.await

// Sealed class que representa el resultado de las operaciones de autenticación.
sealed class AuthRes<out T> {
    // Representa el resultado exitoso con datos de tipo T.
    data class Success<T>(val data: T): AuthRes<T>()
    // Representa un resultado de error con un mensaje de error.
    data class Error(val errorMessage: String): AuthRes<Nothing>()
}

// Clase que gestiona la autenticación de Firebase.
class AuthManager {
    // Instancia de FirebaseAuth inicializada de forma lazy.
    private val auth: FirebaseAuth by lazy { Firebase.auth }

    // Método para iniciar sesión de forma anónima y suspendido.
    suspend fun signInAnonymously(): AuthRes<FirebaseUser> {
        return try {
            val result = auth.signInAnonymously().await()
            // Retorna un resultado exitoso con el usuario anonimo de Firebase.
            AuthRes.Success(result.user ?: throw Exception("Error al iniciar sesión"))
        } catch (e: Exception) {
            // Retorna un resultado de error con el mensaje de error.
            AuthRes.Error(e.message ?: "Error al iniciar sesión")
        }
    }

    // Método para iniciar sesión con correo electrónico y contraseña.
    suspend fun signInWithEmailAndPassword(email: String, password: String): AuthRes<FirebaseUser?> {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            AuthRes.Success(authResult.user)
        } catch (e: Exception) {
            AuthRes.Error(e.message ?: "Error al iniciar sesión")
        }
    }

    // Método para crear un usuario con correo electrónico y contraseña.
    suspend fun createUserWithEmailAndPassword(email: String, password: String): AuthRes<FirebaseUser?> {
        return try{
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            AuthRes.Success(authResult.user)
        } catch (e: Exception) {
            AuthRes.Error(e.message ?: "Error al crear un usuario nuevo")
        }
    }

    // Método para restablecer la contraseña por correo electrónico.
    suspend fun resetPassword(email: String): AuthRes<Unit> {
        return try {
            auth.sendPasswordResetEmail(email).await()
            AuthRes.Success(Unit)
        } catch (e: Exception) {
            AuthRes.Error(e.message?: "Error al restablecer la contraseña")
        }
    }

    // Método para cerrar sesión.
    fun signOut() {
        auth.signOut()
    }

    // Método para obtener el usuario actualmente autenticado.
    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

}
