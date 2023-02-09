package com.example.primo2.screen

import android.app.Activity
import android.app.DatePickerDialog
import android.graphics.fonts.FontStyle
import android.widget.CalendarView
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAbsoluteAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.util.Pair
import androidx.navigation.NavController
import com.example.primo2.DatePlanInfo
import com.example.primo2.*
import com.example.primo2.ui.theme.HideColor

import com.example.primo2.ui.theme.Typography
import com.google.android.exoplayer2.util.Log
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

var date:String = ""

@Composable
fun SelectDateDateScreen(
    onSubmitButtonClicked: () -> Unit = {},
    activity: Activity,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    /*
        날짜선택
        밑에껀 임시
    */
    var visible by remember { mutableStateOf(false) }
    Scaffold(content = {
        modifier.padding(it)


        AndroidView(factory = { CalendarView(it) }, update = {
            it.setOnDateChangeListener { calendarView, year, month, day ->
                date = "$year-${month+1}-$day"
            }
        }, modifier = modifier.fillMaxSize())

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.End
        ) {
            Button(
                modifier = modifier.padding(20.dp),
                onClick = {
                    if (date == "") {
                        Toast.makeText(
                            activity, "날짜를 선택해주세요.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        val date2 = SimpleDateFormat("yyyy-MM-dd").parse(date)
                        val today = Calendar.getInstance()
                        val calculateDate = (today.time.time - date2!!.time) / (1000 * 60 * 60)
                        if (calculateDate > 0) {
                            Toast.makeText(
                                activity, "과거는 선택할 수 없습니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            onSubmitButtonClicked()
                            visible = true
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Black,
                    contentColor = Color.White
                ),
            )
            { Text("등록")    }
        }
    }
    )
    if(visible)
    {
        Dialog(onDismissRequest = { visible = false }) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clip(RoundedCornerShape(12.dp))
                    .background(color = Color.White)
            ) {
                Text(
                    modifier = Modifier
                        .padding(top = 24.dp)
                        .padding(horizontal = 24.dp),
                    text = "데이트 제목",
                    style = Typography.h5.copy(fontSize = 17.sp),
                )
                Text(
                    modifier = Modifier
                        .padding(horizontal = 24.dp,5.dp),
                    text = "데이트의 이름을 지어주세요.",
                    style = Typography.h5.copy(fontSize = 12.sp),
                    color = Color.LightGray
                )
                var dateTitle by remember { mutableStateOf("") }
                TextField(
                    value = dateTitle,
                    onValueChange = { dateTitle = it },
                    modifier = Modifier
                        .padding(20.dp,25.dp,20.dp,70.dp)
                        .fillMaxWidth(),
                    textStyle = TextStyle.Default.copy(fontSize = 20.sp,),
                    label = {Text("데이트 제목")},
                    singleLine = true,
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.White,
                        cursorColor = Color.Black,
                        focusedIndicatorColor = Color.Black,
                        focusedLabelColor = Color.Black
                    )
                )
                Row(modifier = Modifier
                    .fillMaxWidth().height(40.dp)
                    .drawBehind {
                        drawLine(Color.LightGray, Offset(0f,0f),Offset(size.width,0f), strokeWidth = 1.dp.toPx())
                        drawLine(Color.LightGray, Offset(size.width/2,0f),Offset(size.width/2,size.height), strokeWidth = 1.dp.toPx())
                }, horizontalArrangement = Arrangement.Center)
                {
                    Box(modifier = Modifier.weight(1f).clickable {
                        writeDatePlan(date,dateTitle)
                        navController.navigate(PrimoScreen.DatePlans.name)
                        {
                            popUpTo("DatePlans")
                        }
                    })
                    {
                        Text(
                            text = AnnotatedString("등록"),
                            style = TextStyle(
                                fontSize = 14.sp
                            ),
                            modifier = Modifier.align(Alignment.Center).padding(10.dp)
                        )
                    }
                    Box(modifier = Modifier.weight(1f).clickable { visible = false })
                    {
                        Text(
                            text = AnnotatedString("취소"),
                            style = TextStyle(
                                fontSize = 14.sp,
                            ),
                            modifier = Modifier.align(Alignment.Center).padding(10.dp)
                        )
                    }

                }
            }
        }
    }
}
fun writeDatePlan(startDate:String, dateTitle:String){
    val user = Firebase.auth.currentUser
    val database = Firebase.database.reference

    database.child("DatePlan").child(leaderUID)
        .child(dateTitle)
        .child("dateTitle").setValue(dateTitle)
    database.child("DatePlan").child(leaderUID)
        .child(dateTitle)
        .child("startDate").setValue(startDate)
    database.child("DatePlan").child(leaderUID)
        .child(dateTitle)
        .child("endDate")
        .setValue("")

    val insertList = listOf<String>()

    database.child("DatePlan").child(leaderUID)
        .child(dateTitle)
        .child("course")
        .setValue(insertList)

}


