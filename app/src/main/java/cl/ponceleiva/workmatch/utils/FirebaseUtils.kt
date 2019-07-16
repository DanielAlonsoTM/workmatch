package cl.ponceleiva.workmatch.utils

import cl.ponceleiva.workmatch.model.Card
import cl.ponceleiva.workmatch.utils.Constants.FIRESTORE
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import android.content.Context
import cl.ponceleiva.workmatch.utils.Constants.ERROR
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.QuerySnapshot
import kotlin.collections.HashMap

val PREFS_NAME = "workmatch"
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
    db.collection("Users").document(card.announceId).collection("likes").whereEqualTo("userid", currentUserId).get().addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
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

    objectMap["userid"] = card.announceId
    objectMap["date"] = timestamp

    try {
        db.collection("Users").document(currentUserId).collection(collectionName).add(objectMap)

        //Se añade documento a usuario con el que se dio match. --> Redactar mejor xd
        if (collectionName == "matches") {
            db.collection("Users").document(card.announceId).collection(collectionName).add(objectMap)
        }
        logD(FIRESTORE, "Documento/s creado")
        toastMessage(context, message)
    } catch (ex: Exception) {
        logE(FIRESTORE, "Error al crear documento. Detalle: \n$ex")
    }
}

fun createChat(context: Context, professionalId: String, announceId: String, employerId: String) {
    val data = java.util.HashMap<String, Any>()
    val timestamp = Timestamp(date)
    try {
        db.collection("Chats").document("$announceId+$professionalId").set(data)

        data["userid"] = employerId
        data["date"] = timestamp
        db.collection("Users").document(professionalId).collection("matches").document("$announceId+$professionalId").set(data)
        data.clear()

        data["userid"] = professionalId
        data["date"] = timestamp
        db.collection("Users").document(employerId).collection("matches").document("$announceId+$professionalId").set(data)
        toastMessage(context, "Chat creado")
    } catch (ex: Exception) {
        logE(ERROR, "Error when try to create document. Details: \n$ex")
    }
}