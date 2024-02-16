package prueba.pruebamoviesfirebase.modelFireBaseBBDD
/*
import android.provider.ContactsContract.Contacts
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.core.Context
import com.google.firebase.database.getValue
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import prueba.pruebamoviesfirebase.login.utils.AuthManager


class RealtimeManager(context: Context) {
    private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference.child("favoritas")
    private val authManager = AuthManager(context)
    fun  addFavorita(favorita: Favoritas) {
        val key = databaseReference.push().key
        if (key != null) {
            databaseReference.child(key).setValue(favorita)
        }
    }

    fun  deleteFavorita(favoritaId: String) {
        databaseReference.child(favoritaId).removeValue()
    }

    fun getFavoritasFlow(): Flow<List<Favoritas>> {
        val idFilter = authManager.getCurrentUser()?.uid
        val flow = callbackFlow {
            val listener = databaseReference.addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val favoritas = snapshot.children.mapNotNull { snapshot ->
                        val favorita = snapshot.getValue(Favoritas::class.java)
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
 */