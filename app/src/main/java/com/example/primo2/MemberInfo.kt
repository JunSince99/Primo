package com.example.primo2

import android.util.Log
import android.widget.EditText
import androidx.navigation.NavController
import com.example.primo2.screen.PrimoScreen
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.update
var myName:String = ""
var rankTaste:MutableList<String> = mutableListOf()
var userOrientation:HashMap<String,Any> = HashMap()
var partnerOrientation:HashMap<String,Any> = HashMap()
var leaderUID:String = ""
var partnerName:String? = null
var partnerPhotoURL:String? = null
var startDating:String? = null

data class MemberInfo(
    val name: String? = null,
    val birthDay: String? = null,
    val phoneNumber: String? = null,
    val Address: String? = null,
    val photoUrl: String? = null,
    val partnerUID: String?  = null
)
fun getPartnerInfo(){
    val db = Firebase.firestore
    val user = Firebase.auth.currentUser
    if(user != null) {
        db.collection("users").document(user.uid)
            .get()
            .addOnSuccessListener { document ->
                myName = document.getString("name") ?: ""
                leaderUID = document.getString("leaderUID")?:""
                val partnerUID = document.getString("partnerUID") ?: ""
                if(partnerUID == "")
                {
                    partnerName = ""
                }
                else {

                    db.collection("users").document(partnerUID)
                        .get()
                        .addOnSuccessListener { document2 ->
                            partnerName = document2.getString("name") ?: ""
                            partnerPhotoURL = document2.getString("photoUrl") ?: ""
                            startDating = document2.getString("startDating")?:""
                            partnerOrientation = document2.data!!["taste"] as HashMap<String, Any>
                            Log.e("파트너 호출", "파트너 호출")
                        }

                        .addOnFailureListener { exception ->
                        }
                }
            }

            .addOnFailureListener { exception ->
            }
    }
}

fun getPartnerInfoAndMove(navController: NavController){
    val db = Firebase.firestore
    val user = Firebase.auth.currentUser
    if(user != null) {
        db.collection("users").document(user.uid)
            .get()
            .addOnSuccessListener { document ->
                myName = document.getString("name") ?: ""
                leaderUID = document.getString("leaderUID")?:""
                val partnerUID = document.getString("partnerUID") ?: ""
                if(partnerUID == "")
                {
                    partnerName = ""
                }
                else {

                    db.collection("users").document(partnerUID)
                        .get()
                        .addOnSuccessListener { document2 ->
                            partnerName = document2.getString("name") ?: ""
                            partnerPhotoURL = document2.getString("photoUrl") ?: ""
                            startDating = document2.getString("startDating")?:""
                            partnerOrientation = document2.data!!["taste"] as HashMap<String, Any>
                            navController.navigate("ManageAccount")

                        }

                        .addOnFailureListener { exception ->
                        }
                }
            }

            .addOnFailureListener { exception ->
            }
    }
}
fun getUserOrientation()
{
    val db = Firebase.firestore
    val user = Firebase.auth.currentUser
    if(user != null) {
        db.collection("users").document(user.uid)
            .get()
            .addOnSuccessListener { document ->
                if(document.exists()) {
                    rankTaste = document.get("rankTaste") as MutableList<String>
                    userOrientation = document.get("taste") as HashMap<String, Any>
                    userOrientation = userOrientation.toList()
                        .sortedByDescending { it.second.toString().toDouble() }
                        .toMap() as HashMap<String, Any>
                }
            }
            .addOnFailureListener { exception ->
                Log.e("실패","실패")
            }
    }
}

