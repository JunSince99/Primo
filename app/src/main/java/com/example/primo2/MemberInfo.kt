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
var myPhotoURL:String = ""
var userOrientation:HashMap<String,Int> = HashMap()
var rankTaste:MutableList<String> = mutableListOf()
var partnerOrientation:HashMap<String,Int> = HashMap()
var leaderUID:String = ""
var partnerName:String = ""
var partnerPhotoURL:String = ""
var startDating:String? = null
var myBirthDay:String = ""
var partnerBirthDay:String = ""

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
                myPhotoURL = document.getString("photoUrl") ?: ""
                leaderUID = document.getString("leaderUID")?:""
                val partnerUID = document.getString("partnerUID") ?: ""
                myBirthDay = document.getString("birthDay") ?: "19990228"
                if(partnerUID == "") {
                    partnerName = "없음"
                }
                else {

                    db.collection("users").document(partnerUID)
                        .get()
                        .addOnSuccessListener { document2 ->
                            partnerName = document2.getString("name") ?: "error"
                            partnerPhotoURL = document2.getString("photoUrl") ?: ""
                            startDating = document2.getString("startDating")?: ""
                            partnerBirthDay = document2.getString("birthDay") ?: "19990223"

                            val rankTastePartner = document2.get("rankTaste") as MutableList<String>
                            val favSize = (rankTastePartner.size * 0.3).toInt()
                            for (i in 0 until favSize) {
                                for ((key, value) in placeListHashMap[rankTastePartner[i]]!!.placeHashMap!!) {
                                    if (!partnerOrientation.containsKey(key)) {
                                        partnerOrientation[key] = value.toString().toInt()
                                    } else {
                                        partnerOrientation.replace(
                                            key,
                                            partnerOrientation[key]!! + value.toString().toInt()
                                        )
                                    }
                                }
                            }

                            for ((key, value) in partnerOrientation) {
                                partnerOrientation.replace(key, value / favSize)
                            }
                            Log.e("",""+ partnerOrientation)
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
                            //                            partnerOrientation = document2.data!!["taste"] as HashMap<String, Any>
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
                if (document.exists()) {
                    rankTaste = document.get("rankTaste") as MutableList<String>
                    val favSize = (rankTaste.size * 0.3).toInt()
                    for (i in 0 until favSize) {
                        for ((key, value) in placeListHashMap[rankTaste[i]]!!.placeHashMap!!) {
                            if (!userOrientation.containsKey(key)) {
                                userOrientation[key] = value.toString().toInt()
                            } else {
                                userOrientation.replace(
                                    key,
                                    userOrientation[key]!! + value.toString().toInt()
                                )
                            }
                        }
                    }

                    for ((key, value) in userOrientation) {
                        userOrientation.replace(key, value / favSize)
                    }
                    Log.e("",""+ userOrientation)
                }
            }
            .addOnFailureListener { exception ->
                Log.e("실패", "실패")
            }
    }
}

