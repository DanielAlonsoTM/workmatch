package cl.ponceleiva.workmatch.utils

import android.app.Activity
import cl.ponceleiva.workmatch.model.Card
import cl.ponceleiva.workmatch.utils.Constants.FIRESTORE
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import android.content.Context
import cl.ponceleiva.workmatch.activities.home.MainActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import kotlin.collections.HashMap

val db = FirebaseFirestore.getInstance()
private val date = Date()

fun createUser(userData: HashMap<String, Any>, idUserDoc: String) {
    try {
        db.collection("Users").document(idUserDoc).set(userData)
                .addOnSuccessListener {
                    logD(FIRESTORE, "Document create : $idUserDoc")
                }
                .addOnFailureListener { e ->
                    logE(FIRESTORE, "Failed create document: $e")
                }
    } catch (e: Exception) {
        logE(FIRESTORE, "Error al crear documento. Detalle:\n$e")
    }
}

fun checkMatch(card: Card, currentUserId: String, context: Context) {
    //Checkear en la lista del usuario B (al que se dio like), si el usuario A (usuario de la sesión actual)
    db.collection("Users").document(card.userId).collection("likes").whereEqualTo("userid", currentUserId).get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
        if (task.isSuccessful) {
            logD(FIRESTORE, "Cantidad de cooincidencias: " + task.result!!.size())
            if (task.result!!.size() == 1 && task.result != null) {
                addUserActionDocument(card, "matches", "Maaatch!", currentUserId, context)
            } else if (task.result!!.size() == 0 && task.result != null) {
                addUserActionDocument(card, "likes", "Likeeeeee!", currentUserId, context)
            } else {
                logE(FIRESTORE, "Ocurrio un problema con el documento actual")
            }
        }
    })
}

private fun addUserActionDocument(card: Card, collectionName: String, message: String, currentUserId: String, context: Context) {
    val objectMap = java.util.HashMap<String, Any>()
    val timestamp = Timestamp(date)

    objectMap["userid"] = card.userId
    objectMap["date"] = timestamp

    try {
        db.collection("Users").document(currentUserId).collection(collectionName).add(objectMap)

        //Se añade documento a usuario con el que se dio match. --> Redactar mejor xd
        if (collectionName == "matches") {
            db.collection("Users").document(card.userId).collection(collectionName).add(objectMap)
        }
        logD(FIRESTORE, "Documento/s creado")
        toastMessage(context, message)
    } catch (ex: Exception) {
        logE(FIRESTORE, "Error al crear documento. Detalle: \n$ex")
    }
}
