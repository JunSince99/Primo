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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.example.primo2.partnerName
import com.example.primo2.ui.theme.Typography
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@Composable
fun RegisterPartnerScreen(
    onSubmitButtonClicked: () -> Unit = {},
    activity: Activity,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Column {
        var date:String = ""
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
                    text = "우리의 인연이 시작된 날",
                    style = Typography.h5.copy(fontSize = 12.sp),
                    modifier = Modifier
                        .padding(30.dp,0.dp,0.dp,0.dp)
                )
            }
        }
        Scaffold(content = {
            modifier.padding(it)


            AndroidView(factory = { CalendarView(it) }, update = {
                it.setOnDateChangeListener { calendarView, year, month, day ->
                    date = "%04d-%02d-%02d".format(year,month+1,day)
 //                   date = "$year-${month+1}-$day".format()
                }
            }, modifier = modifier.fillMaxSize())

            Column(
                modifier = Modifier.fillMaxWidth().fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.End
            ) {
                Button(modifier = modifier.padding(20.dp),onClick = {
                    if(date == "")
                    {
                        Toast.makeText(
                            activity, "날짜를 선택해주세요.",
                            Toast.LENGTH_SHORT).show()
                    }
                    else {
                        val date2 = SimpleDateFormat("yyyy-MM-dd").parse(date)
                        val today = Calendar.getInstance()
                        val calculateDate = (today.time.time - date2!!.time) / (1000 * 60 * 60)
                        if (calculateDate <= 0) {
                            Toast.makeText(
                                activity, "미래는 선택할 수 없습니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        else{
                            saveStartDating(date)
                            onSubmitButtonClicked()
                        }
                    }},
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black, contentColor = Color.White),)
                { Text("등록")    }
            }
        }
        )
    }

}

fun saveStartDating(startDating:String) {
    val user = Firebase.auth.currentUser
    val db = Firebase.firestore
    val docRef = db.collection("users").document(user!!.uid)

    db.runTransaction { transaction ->
        val snapshot = transaction.get(docRef)
        transaction.update(docRef, "startDating", startDating)
        startDating
    }
}

