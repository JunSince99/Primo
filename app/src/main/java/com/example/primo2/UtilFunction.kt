package com.example.primo2

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.net.URLConnection

class UtilFunction {

}

fun isImageFile(path: String?): Boolean {
    val mimeType = URLConnection.guessContentTypeFromName(path)
    return mimeType != null && mimeType.startsWith("image")
}

fun isVideoFile(path: String?): Boolean {
    val mimeType = URLConnection.guessContentTypeFromName(path)
    return mimeType != null && mimeType.startsWith("video")
}

fun getWriterInfomation(writerID: String): MemberInfo {
    val db = Firebase.firestore
    val docRef = db.collection("users").document(writerID)


    var name: String? = null
    var birthDay: String? = null
    var phoneNumber: String? = null
    var address: String? = null
    var photoUrl: String? = null
    db.runTransaction { transaction ->
        val snapshot = transaction.get(docRef)
        name = snapshot.getString("name")
        birthDay = snapshot.getString("birthDay")
        phoneNumber = snapshot.getString("phoneNumber")
        address = snapshot.getString("address")
        photoUrl = snapshot.getString("photoUrl")
    }.addOnSuccessListener {

    }
    val test:MemberInfo = MemberInfo(name, birthDay, phoneNumber, address, photoUrl)

    return MemberInfo(name, birthDay, phoneNumber, address, photoUrl)
}