package com.example.primo2.screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
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
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.*
import java.time.*
import java.time.format.TextStyle
import java.util.*
import kotlin.math.roundToInt


@Composable
fun Day(day: CalendarDay) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .padding(8.dp)
            .clip(CircleShape)
            .clickable(
                enabled = day.position == DayPosition.MonthDate,
                onClick = {/*TODO*/}
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

    var imageUrlList:List<String> = listOf()
//Firebase.database.reference.child("DatePlan").child(user!!.uid)
    val user = Firebase.auth.currentUser
    val db = Firebase.firestore
    var leaderUID = ""
    LaunchedEffect(true) {
        db.collection("users").document(user!!.uid)
            .get()
            .addOnSuccessListener { document ->
                leaderUID = document.getString("leaderUID") as String
                val database = Firebase.database.reference.child("DatePlan").child(leaderUID)
                val postListener = object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        datePlanList.clear()
                        imageUrlList = listOf()
                        for (datePlanSnapshot in dataSnapshot.children) {
                            val title = datePlanSnapshot.child("dateTitle").value.toString()
                            val startDate = datePlanSnapshot.child("startDate").value.toString()
                            val endDate = datePlanSnapshot.child("endDate").value.toString()
                            val course: MutableList<String> = mutableListOf()
                            val courseCount = datePlanSnapshot.child("course").childrenCount
                            for (i in 0 until courseCount) {
                                course.add(
                                    datePlanSnapshot.child("course")
                                        .child(i.toString()).value.toString()
                                )
                            }
                            course.add("")
                            datePlanList.add(
                                DatePlanInfo(
                                    title,
                                    startDate,
                                    endDate,
                                    course
                                )
                            )
                        }
                        datePlanList.sortByDescending {
                            it.dateStartDate
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        //실패
                    }
                }
                database.addValueEventListener(postListener)
            }
    }
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

        ShowCalendar(onMonthChange)
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
@Composable
fun ShowCalendar(onMonthChange: (YearMonth) -> Unit){
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
    HorizontalCalendar(
        modifier = Modifier
            .fillMaxWidth()
            .height(360.dp),
        state = state,
        dayContent = { Day(it) },
        monthHeader = {
            DaysOfWeekTitle(daysOfWeek = daysOfWeek) // Use the title as month header
        },
        calendarScrollPaged = true,
        userScrollEnabled = true,
        contentPadding = PaddingValues(8.dp),
    )
    onMonthChange(state.firstVisibleMonth.yearMonth)
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
