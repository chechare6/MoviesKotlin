package prueba.pruebamoviesfirebase.favorites

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import prueba.pruebamoviesfirebase.login.utils.AuthManager


class RealtimeManager {
    private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance("https://movieskotlin-b4fae-default-rtdb.europe-west1.firebasedatabase.app").reference.child("favoritas")
    private val authManager = AuthManager()

    fun addFavorita(favorita: Favorita) {
        val key = databaseReference.push().key
        if (key != null) {
            databaseReference.child(key).setValue(favorita)
        }
    }

    fun  deleteFavorita(favoritaId: String) {
        databaseReference.child(favoritaId).removeValue()
    }

    fun getFavoritasFlow(): Flow<List<Favorita>> {
        val idFilter = authManager.getCurrentUser()?.uid
        val flow = callbackFlow {
            val listener = databaseReference.addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val favoritas = snapshot.children.mapNotNull { snapshot ->
                        val favorita = snapshot.getValue(Favorita::class.java)
                        snapshot.key?.let { favorita?.copy(key = it) }
                    }
                    trySend(favoritas.filter { it.uid == idFilter }).isSuccess
                }
                override fun onCancelled(error: DatabaseError) {
                    close(error.toException())
                }
            })
            awaitClose { databaseReference.removeEventListener(listener) }
        }
        return flow
    }
}