package cl.ponceleiva.workmatch.utils

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

lateinit var db: DocumentReference

fun createUser(userData: HashMap<String, Any>, idUserDoc: String) {
    db = FirebaseFirestore.getInstance().collection("Users").document(idUserDoc)
    db.set(userData)
            .addOnSuccessListener { logD(Constants.FIRESTORE, "Document create : $idUserDoc")
            }
            .addOnFailureListener {
                e -> logE(Constants.FIRESTORE, "Failed create document: $e")
            }
}
