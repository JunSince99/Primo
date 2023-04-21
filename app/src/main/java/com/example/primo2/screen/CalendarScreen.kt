package com.example.primo2.screen

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.bumptech.glide.RequestManager
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.primo2.DatePlanInfo
import com.example.primo2.leaderUID
import com.example.primo2.placeListHashMap
import com.example.primo2.ui.theme.LazyColumnExampleTheme
import com.example.primo2.ui.theme.Typography
import com.example.primo2.ui.theme.spoqasans
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.kizitonwose.calendar.compose.ContentHeightMode
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.*
import java.time.*
import java.time.format.TextStyle
import java.util.*
import kotlin.math.roundToInt


@Composable
fun Day(day: CalendarDay,datePlanList: SnapshotStateList<DatePlanInfo>, onVisibleChange:(Boolean) -> Unit ) { //일
    var planable = true
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .padding(8.dp)
            .clip(CircleShape)
            .clickable(
                enabled = day.position == DayPosition.MonthDate,
                onClick = {
                    if (planable) {
                        date = "${day.date.year}-%02d-%02d".format(
                            day.date.month.value,
                            day.date.dayOfMonth
                        )

                        Log.e("", "" + date)
                        onVisibleChange(true)
                    }
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = day.date.dayOfMonth.toString(),
                color = if (day.position != DayPosition.MonthDate) {
                    Color.Gray
                } else if (day.date.dayOfWeek == DayOfWeek.SUNDAY) {
                    Color.Red
                } else if (day.date.dayOfWeek == DayOfWeek.SATURDAY) {
                    Color.Red
                } else Color.Black,
                fontFamily = spoqasans,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.size(4.dp))
            for(i in 0 until datePlanList.size) {
                if(datePlanList[i].dateStartDate.split("-")[1].toInt() == day.date.month.value && datePlanList[i].dateStartDate.split("-")[2].toInt() == day.date.dayOfMonth) {
                    Box(
                        modifier = Modifier
                            .size(4.dp)
                            .background(Color.Black, CircleShape)
                    )
                    planable = false
                    break
                }
            }
        }

    }
}

@Composable
fun DaysOfWeekTitle(daysOfWeek: List<DayOfWeek>) { //요일
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

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun CalendarScreen(
    month: YearMonth,
    onMonthChange:(YearMonth) -> Unit,
    requestManager: RequestManager,
    datePlanList: SnapshotStateList<DatePlanInfo>,
    navController: NavController,
    listState: LazyListState = LazyListState(),
    modifier: Modifier = Modifier
) {
    Column {
        Surface(
            modifier = Modifier, // 속성 정하는거(패딩, 크기 등)
            color = MaterialTheme.colors.onBackground // app.build.gradle에서 색 지정 가능
        ) {
            Scaffold() { padding ->
                DatePlans(requestManager, Modifier.padding(padding), datePlanList,navController,listState,onMonthChange,month)
            }
            Column(modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                if(datePlanList.isEmpty()){
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun DatePlans(requestManager: RequestManager,
              modifier: Modifier = Modifier,
              datePlanList: SnapshotStateList<DatePlanInfo>,
              navController: NavController,
              listState: LazyListState = LazyListState(),
              onMonthChange: (YearMonth) -> Unit,
              month: YearMonth
)
{
    Column {
        ShowCalendar(onMonthChange,datePlanList,navController)
        LazyColumn(modifier = modifier, state = listState) {
            items(datePlanList.size) {
                if (datePlanList[it].dateStartDate.split("-")[1].toInt() == month.month.value) {
                    DatePlan(
                        datePlanList[it],
                        requestManager,
                        it,
                        navController,
                        leaderUID,
                    )
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ShowCalendar(onMonthChange: (YearMonth) -> Unit, datePlanList: SnapshotStateList<DatePlanInfo>,navController: NavController){
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
        outDateStyle = OutDateStyle.EndOfGrid
    )
    var calendarheight = 360.dp
    var visible by remember { mutableStateOf(false) }
    HorizontalCalendar(
        modifier = Modifier
            .fillMaxWidth(),
        state = state,
        dayContent = {
                Day(it,datePlanList, onVisibleChange = { vis->
                    visible = vis
                } )
                     },
        monthHeader = {
            DaysOfWeekTitle(daysOfWeek = daysOfWeek) // Use the title as month header
        },
        calendarScrollPaged = true,
        userScrollEnabled = true,
        contentPadding = PaddingValues(8.dp),
        )
    if(visible)
    {
        val context = LocalContext.current
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
                        .padding(20.dp, 25.dp, 20.dp, 70.dp)
                        .fillMaxWidth(),
                    textStyle = androidx.compose.ui.text.TextStyle.Default.copy(fontSize = 20.sp,),
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
                    .fillMaxWidth()
                    .height(40.dp)
                    .drawBehind {
                        drawLine(
                            Color.LightGray,
                            Offset(0f, 0f),
                            Offset(size.width, 0f),
                            strokeWidth = 1.dp.toPx()
                        )
                        drawLine(
                            Color.LightGray,
                            Offset(size.width / 2, 0f),
                            Offset(size.width / 2, size.height),
                            strokeWidth = 1.dp.toPx()
                        )
                    }, horizontalArrangement = Arrangement.Center)
                {
                    Box(modifier = Modifier
                        .weight(1f)
                        .clickable {
                            var isDuplication = false
                            for (i in 0 until datePlanList.size) {
                                if (datePlanList[i].dateTitle == dateTitle) {
                                    isDuplication = true
                                    break
                                }
                            }
                            if (isDuplication) {
                                Toast
                                    .makeText(
                                        context, "중복 타이틀 입니다.",
                                        Toast.LENGTH_SHORT
                                    )
                                    .show()
                            } else {
                                writeDatePlan(date, dateTitle, navController)
                            }
                        })
                    {
                        Text(
                            text = AnnotatedString("등록"),
                            style = androidx.compose.ui.text.TextStyle(
                                fontSize = 14.sp
                            ),
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(10.dp)
                        )
                    }
                    Box(modifier = Modifier
                        .weight(1f)
                        .clickable { visible = false })
                    {
                        Text(
                            text = AnnotatedString("취소"),
                            style = androidx.compose.ui.text.TextStyle(
                                fontSize = 14.sp,
                            ),
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(10.dp)
                        )
                    }

                }
            }
        }
    }
    val calendarIndex = state.layoutInfo.visibleMonthsInfo.lastIndex

    if (calendarIndex != -1) {
        LaunchedEffect(state.layoutInfo.visibleMonthsInfo[calendarIndex]) {
            if (calendarIndex == 1) {
                if (state.layoutInfo.visibleMonthsInfo[0].month.yearMonth == startMonth) {
                    onMonthChange(state.layoutInfo.visibleMonthsInfo[0].month.yearMonth)
                } else if (state.layoutInfo.visibleMonthsInfo[1].month.yearMonth == endMonth) {
                    onMonthChange(state.layoutInfo.visibleMonthsInfo[1].month.yearMonth)
                }
            } else {
                onMonthChange(state.layoutInfo.visibleMonthsInfo[1].month.yearMonth)
            }
        }
    }

}

@OptIn(
    ExperimentalGlideComposeApi::class, ExperimentalPagerApi::class,
    ExperimentalMaterialApi::class
)
@Composable
fun DatePlan(datePlanInfo: DatePlanInfo,requestManager: RequestManager,num:Int,navController: NavController,leaderUID:String) {
    val swipeSize = 75.dp
    val swipeableState = rememberSwipeableState(0)
    val sizePx = with(LocalDensity.current) { swipeSize.toPx() }
    val anchors = mapOf(0f to 0, -sizePx to 1)
    var visible by remember { mutableStateOf(false) }
    Column {
        Text(
            text = datePlanInfo.dateStartDate.substring(8, 10) + "일",
            color = Color.Black,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = spoqasans,
            modifier = Modifier
                .padding(horizontal = 16.dp)
        )
        Surface(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .shadow(
                    elevation = 1.dp,
                    shape = RoundedCornerShape(20)
                )
                .aspectRatio(16f / 4f)
                .clickable {
                    val datePlanName = datePlanInfo.dateTitle
                    navController.navigate("${PrimoScreen.Map.name}/$datePlanName/$leaderUID")
                }
                .swipeable(
                    swipeableState,
                    anchors = anchors,
                    thresholds = { _, _ -> FractionalThreshold(0.8f) },
                    orientation = Orientation.Horizontal
                )
        ) {
            if (visible) {
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
                                .padding(24.dp),
                            text = "삭제한 일정은 복구할 수 없습니다, 삭제하시겠습니까?",
                            style = Typography.h5.copy(fontSize = 12.sp),
                        )
                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .drawBehind {
                                drawLine(
                                    Color.LightGray, Offset(0f, 0f),
                                    Offset(size.width, 0f), strokeWidth = 1.dp.toPx()
                                )
                                drawLine(
                                    Color.LightGray,
                                    Offset(size.width / 2, 0f),
                                    Offset(size.width / 2, size.height),
                                    strokeWidth = 1.dp.toPx()
                                )
                            }, horizontalArrangement = Arrangement.Center
                        )
                        {
                            val database =
                                Firebase.database.reference.child("DatePlan").child(leaderUID)
                                    .child(datePlanInfo.dateTitle)
                            Box(modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    visible = false
                                    database.removeValue()
                                })
                            {
                                Text(
                                    text = AnnotatedString("삭제"),
                                    style = androidx.compose.ui.text.TextStyle(
                                        fontSize = 14.sp
                                    ),
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                        .padding(10.dp)
                                )
                            }
                            Box(modifier = Modifier
                                .weight(1f)
                                .clickable { visible = false })
                            {
                                Text(
                                    text = AnnotatedString("취소"),
                                    style = androidx.compose.ui.text.TextStyle(
                                        fontSize = 14.sp,
                                    ),
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                        .padding(10.dp)
                                )
                            }

                        }
                    }
                }
            }
            Row(
                modifier = Modifier
                    .background(color = Color.Red),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically

            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    modifier = Modifier
                        .size(30.dp)
                        .clickable {
                            visible = true
                        },
                    tint = Color.White,
                )
                Spacer(modifier = Modifier.padding(10.dp, 0.dp))
            }
            Row(
                modifier = Modifier
                    .offset {
                        if (swipeableState.offset.value.roundToInt() < 0) {
                            IntOffset(swipeableState.offset.value.roundToInt(), 0)
                        } else {
                            IntOffset(0, 0)
                        }
                    }
                    .background(Color.White),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.padding(6.dp))
                var url = ""
                if (datePlanInfo.course.isNotEmpty()) {
                    url = placeListHashMap[datePlanInfo.course[0]]?.imageResource
                        ?: "https://firebasestorage.googleapis.com/v0/b/primo-92b68.appspot.com/o/places%2F%ED%95%98%EB%8A%98.jpg?alt=media&token=dce6f873-3c4c-46e5-bf27-72bcc2a7ddcc"
                }
                GlideImage(
                    model = url,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(60.dp)
                )
                {
                    it
                        .thumbnail(
                            requestManager
                                .asDrawable()
                                .load(url)
                                .override(64)
                        )
                    // .signature(signature)
                }
                Spacer(modifier = Modifier.padding(6.dp))
                Column(
                    modifier = Modifier,
                ) {
                    Text(
                        text = datePlanInfo.dateTitle,
                        color = Color.Black,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                    )
                    Spacer(modifier = Modifier.padding(2.dp))
                    Text(
                        text = "인천광역시 송도동",
                        color = Color.DarkGray,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier
                    )
                }
            }
        }
    }
}

private fun writeDatePlan(startDate:String, dateTitle:String, navController: NavController){
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
        .addOnSuccessListener {
            navController.navigate("${PrimoScreen.Map.name}/$dateTitle/$leaderUID")
        }

}

