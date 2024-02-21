package prueba.pruebamoviesfirebase.login.utils

import android.content.Context
import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import com.google.firebase.analytics.FirebaseAnalytics

class AnalyticsManager(context: Context) {

    // Instancia de FirebaseAnalytics inicializada de forma perezosa
    private val firebaseAnalytics: FirebaseAnalytics by lazy { FirebaseAnalytics.getInstance(context) }

    // Método privado para registrar un evento en Firebase Analytics
    private fun logEvent(eventName: String, params: Bundle) {
        firebaseAnalytics.logEvent(eventName, params)
    }

    // Método para registrar un evento de botón clicado en Firebase Analytics
    fun logButtonClicked(buttonName: String) {
        val params = Bundle().apply {
            putString("button_name", buttonName)
        }
        logEvent("button_clicked", params)
    }

    // Composable para registrar la vista de una pantalla en Firebase Analytics
    @Composable
    fun LogScreenView(screenName: String) {
        DisposableEffect(Unit) {
            onDispose {
                val params = Bundle().apply {
                    putString(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
                    putString(FirebaseAnalytics.Param.SCREEN_CLASS, screenName)
                }
                logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, params)
            }
        }
    }

    // Método para registrar un evento de error en Firebase Analytics
    fun logError(error: String) {
        val params = Bundle().apply {
            putString("error", error)
        }
        logEvent("error", params)
    }
}