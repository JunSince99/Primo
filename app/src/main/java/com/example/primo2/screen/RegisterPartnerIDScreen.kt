package com.example.primo2.screen

import android.app.Activity
import android.util.Log
import android.widget.CalendarView
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.example.primo2.activity.MainActivity
import com.example.primo2.getPartnerInfo
import com.example.primo2.ui.theme.Typography
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
//테스트 코드 gmKp4nBf5LQMB1nbsdZ7uHC6xBp2
@Composable
fun RegisterPartnerIDScreen(
    onSubmitButtonClicked: () -> Unit = {},
    activity: Activity,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Column {
        var date = ""
        TopAppBar(
            modifier
                .fillMaxWidth()
                .height(100.dp), backgroundColor = Color.White) {
            Column() {
                Text(
                    text = "커플 등록",
                    style = Typography.h5.copy(fontSize = 24.sp),
                    modifier = Modifier
                        .padding(12.dp),
                    color = Color.Black
                )
                Text(
                    text = "연인의 Code를 입력해주세요.",
                    style = Typography.h5.copy(fontSize = 12.sp),
                    modifier = Modifier
                        .padding(30.dp,0.dp,0.dp,0.dp)
                )
            }
        }
        Scaffold(content = {
            modifier.padding(it)


            var partnerCode by remember { mutableStateOf("") }
            Column(modifier = Modifier,
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.End) {
                TextField(
                    value = partnerCode,
                    onValueChange = { partnerCode = it },
                    modifier = Modifier
                        .fillMaxWidth().padding(50.dp),
                    textStyle = TextStyle.Default.copy(fontSize = 20.sp,),
                    label = { Text("Code") },
                    singleLine = true,
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.White,
                        cursorColor = Color.Black,
                        focusedIndicatorColor = Color.Black,
                        focusedLabelColor = Color.Black
                    )
                )

                Button(
                    modifier = modifier.padding(0.dp,0.dp,40.dp,0.dp), onClick = {
                        val db = Firebase.firestore
                        val user = Firebase.auth.currentUser
                        db.collection("users").document(partnerCode)
                            .get()
                            .addOnSuccessListener { document ->
                                db.collection("users").document(user!!.uid)
                                    .get()
                                    .addOnSuccessListener { document2 ->
                                        val startDating = document2["startDating"]
                                        if(document["partnerUID"] != "")
                                        {
                                            Toast.makeText(
                                                activity,"신청을 받을 수 없는 상태입니다.",
                                                Toast.LENGTH_SHORT).show()
                                        }
                                        else {
                                            savePartner(partnerCode,startDating.toString())
                                            getPartnerInfo(navController)
                                        }
                                    }
                            }
                            .addOnFailureListener { exception ->
                            }
                                                                                 },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Black,
                        contentColor = Color.White
                    ),
                )
                { Text("다음") }
            }
        }
        )

    }

}


fun savePartner(partnerUID:String,startDating:String) {
    val user = Firebase.auth.currentUser
    val db = Firebase.firestore
    val docRef = db.collection("users").document(user!!.uid)

    db.runTransaction { transaction ->
        val snapshot = transaction.get(docRef)
        transaction.update(docRef, "partnerUID", partnerUID)
        partnerUID
    }

    val docRef2 = db.collection("users").document(partnerUID)

    db.runTransaction { transaction ->
        val snapshot = transaction.get(docRef)
        transaction.update(docRef2, "partnerUID", user.uid)
        transaction.update(docRef2, "startDating", startDating)
        user.uid
    }
}

