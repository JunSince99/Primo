package com.example.primo2

import android.util.Log
import android.widget.EditText
import androidx.navigation.NavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.update

val userOrientation:HashMap<String,Double> = HashMap()

data class MemberInfo(
    val name: String? = null,
    val birthDay: String? = null,
    val phoneNumber: String? = null,
    val Address: String? = null,
    val photoUrl: String? = null,
    val IE: Double = 0.0,
    val NS: Double = 0.0,
    val FT: Double = 0.0,
    val PJ: Double = 0.0
)

fun getUserOrientation(navController: NavController)
{
    val db = Firebase.firestore
    val user = Firebase.auth.currentUser
    if(user != null) {
        db.collection("users").document(user.uid)
            .get()
            .addOnSuccessListener { document ->
                userOrientation["IE"] = document.getDouble("IE") ?: 0.0
                userOrientation["NS"] = document.getDouble("NS") ?: 0.0
                userOrientation["FT"] = document.getDouble("FT") ?: 0.0
                userOrientation["PJ"] = document.getDouble("PJ") ?: 0.0
                navController.navigate("Map") { popUpTo("Home") }
            }
            .addOnFailureListener { exception ->
            }
    }
}