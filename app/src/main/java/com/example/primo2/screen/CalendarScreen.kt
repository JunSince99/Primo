package com.example.primo2.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.bumptech.glide.RequestManager
import com.bumptech.glide.integration.compose.GlideImage
import com.example.primo2.DatePlanInfo
import com.example.primo2.placeListHashMap
import com.example.primo2.ui.theme.Typography
import com.example.primo2.ui.theme.spoqasans
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.VerticalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.*
import java.time.*
import java.time.format.TextStyle
import java.util.*
import kotlin.math.roundToInt

fun Dateform(year : YearMonth) : String {
    var temp = year.toString().replace("-","년 ")
    if (year.monthValue < 10) temp = temp.substring(0,6) + temp.substring(7) + "월"
    return temp
}

@Composable
fun Day(day: CalendarDay) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .padding(8.dp)
            .clip(CircleShape)
            .clickable(
                enabled = day.position == DayPosition.MonthDate,
                onClick = { }
            ), // This is important for square sizing!
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day.date.dayOfMonth.toString(),
            color = if (day.position != DayPosition.MonthDate) {Color.White}
                    else if ( day.date.dayOfWeek == DayOfWeek.SUNDAY) {Color.Red}
                    else if ( day.date.dayOfWeek == DayOfWeek.SATURDAY) {Color.Red}
                    else Color.Black,
            fontFamily = spoqasans,
            fontWeight = FontWeight.Medium
        )

    }
}

@Composable
fun DaysOfWeekTitle(daysOfWeek: List<DayOfWeek>) {
    Row(modifier = Modifier.fillMaxWidth()) {
        for (dayOfWeek in daysOfWeek) {
            Text(
                modifier = Modifier
                    .weight(1f),
                textAlign = TextAlign.Center,
                text = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                color = Color.Gray,
                fontFamily = spoqasans,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun CalendarScreen(
    month:String,
    onMonthChange:(String) -> Unit,
    modifier: Modifier = Modifier
) {
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(3) } // Adjust as needed
    val endMonth = remember { currentMonth.plusMonths(4) } // Adjust as needed
    val firstDayOfWeek = remember { firstDayOfWeekFromLocale() } // Available from the library
    val daysOfWeek = daysOfWeek()
    val state = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = firstDayOfWeek,
        outDateStyle = OutDateStyle.EndOfRow
    )
    Column {
        HorizontalCalendar(
            modifier = Modifier,
            state = state,
            dayContent = { Day(it) },
            monthHeader = {
                DaysOfWeekTitle(daysOfWeek = daysOfWeek) // Use the title as month header
            },
            calendarScrollPaged = true,
            userScrollEnabled = true,
            contentPadding = PaddingValues(8.dp)
        )
    }
    onMonthChange(Dateform(state.firstVisibleMonth.yearMonth))
}

