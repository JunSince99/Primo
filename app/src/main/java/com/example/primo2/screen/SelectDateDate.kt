package com.example.primo2.screen

import android.app.Activity
import android.app.DatePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.util.Pair
import androidx.navigation.NavController

import com.example.primo2.ui.theme.Typography
import com.google.android.material.datepicker.MaterialDatePicker
import java.time.LocalDate
import java.util.*

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
    val calendar = Calendar.getInstance()
    val dateState = remember{ mutableStateOf("") }
    val context = LocalContext.current
    val datePickerDialog = DatePickerDialog(
        context,
        { view, year, month, dayOfMonth ->
        dateState.value = "{${year}년 ${month + 1}월 ${dayOfMonth}일"
    },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH))
    datePickerDialog.show()
}

