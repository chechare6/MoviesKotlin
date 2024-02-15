package prueba.pruebamoviesfirebase.login.utils

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.database.core.Context
import kotlinx.coroutines.tasks.await

//ASÍ ES LA ESTRUCTURA DE MOVIESDB
/*
sealed class AuthManager <T> (
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T?) : AuthManager<T>(data)
    class Error<T>(message: String, data: T? = null) : AuthManager<T>(data, message)
}
*/
//ASI ES LA DE FIREBASE

sealed class AuthRes<out T> {
    data class Success<T>(val data: T): AuthRes<T>()
    data class Error(val errorMessage: String): AuthRes<Nothing>()
}
/*
Es lo mismo de maneras distintas, igual podríamos adaptar la clase
para utilizar 'Resources' y ahorrarnos esta clase??
 */

class AuthManager(private val context: Context) {
    private val auth: FirebaseAuth by lazy { Firebase.auth }

    suspend fun signInAnonymously(): AuthRes<FirebaseUser> {
        return try {
            val result = auth.signInAnonymously().await()
            AuthRes.Success(result.user ?: throw Exception("Error al iniciar sesión"))
        } catch (e: Exception) {
            AuthRes.Error(e.message ?: "Error al iniciar sesión")
        }
    }

    /* EJEMPLO DE COMO SE USARIA CON RESOURCE
    NO SE SI FUNCIONA, HABRÍA QUE PROBAR **DESPUÉS** DE QUE EJECUTE Y FUNCIONE LA APP
    suspend fun signInWithEmailAndPassword(email: String, password: String): Resource<FirebaseUser?> {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            Resource.Success(authResult.user)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error al iniciar sesión")
        }
    }
    */
    //AQUI LA VERSION NORMAL
    suspend fun signInWithEmailAndPassword(email: String, password: String): AuthRes<FirebaseUser?> {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            AuthRes.Success(authResult.user)
        } catch (e: Exception) {
            AuthRes.Error(e.message ?: "Error al iniciar sesión")
        }
    }

    suspend fun createUserWithEmailAndPassword(email: String, password: String): AuthRes<FirebaseUser?> {
        return try{
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            AuthRes.Success(authResult.user)
        } catch (e: Exception) {
            AuthRes.Error(e.message ?: "Error al crear un usuario nuevo")
        }
    }

    suspend fun resetPassword(email: String): AuthRes<Unit> {
        return try {
            auth.sendPasswordResetEmail(email).await()
            AuthRes.Success(Unit)
        } catch (e: Exception) {
            AuthRes.Error(e.message?: "Error al restablecer la contraseña")
        }
    }

    fun signOut() {
        auth.signOut()
    }

    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

}
