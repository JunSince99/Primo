package com.example.primo2.screen

import PostViewModel
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.bumptech.glide.RequestManager
import com.example.primo2.*
import com.example.primo2.activity.*
import com.google.android.material.behavior.HideBottomViewOnScrollBehavior.ScrollState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.*


enum class PrimoScreen() {
    Home,
    Login,
    Register,
    Register2,
    MemberInit,
    UploadPost,
    Map,
    DatePlans,
    Favorites,
    ManageAccount,
    RegisterPartner,
    RegisterPartnerID,
    SelectDateDate,
    Search,
    Test,
    PlaceBattle,
    SelectWritingCourse,
    WritingScreen,
    PostScreen,
    PlaceDetailScreen
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrimoApp(activity: Activity, requestManager: RequestManager,modifier: Modifier = Modifier,viewModel: PostViewModel = viewModel()) {
    val datePlanList = remember { mutableStateListOf<DatePlanInfo>() }
    InitailLoading(datePlanList)
    val homeListState:LazyListState = rememberLazyListState() // 홈 화면 스크롤 상태 저장
    val datePlanListState:LazyListState = rememberLazyListState() // 데이트 플랜 스크롤 상태 저장
    val auth: FirebaseAuth = Firebase.auth
    val navController = rememberNavController()
    val bottomBarState = rememberSaveable { (mutableStateOf(false)) } // 바텀 네비게이션바 보이게 할지 말지
    val topBarState = rememberSaveable { (mutableStateOf(false)) } // 탑바 보이게 할지 말지
    val navName = rememberSaveable { (mutableStateOf("")) }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    var month by rememberSaveable {(mutableStateOf(YearMonth.of(2023,3)))}
    Scaffold(
        topBar = { TopBar(navController,name = navName.value , TopBarState = topBarState.value, topBarText = "Primo",homeListState, datePlanListState,scrollBehavior,month)},
        bottomBar = { NavigationBar(navController,bottomBarState.value) },
        backgroundColor = Color.White
    )  { innerPadding ->
        navName.value = navController.currentDestination?.route ?: ""
        bottomBarState.value = checkBottomVisible(navController)
        topBarState.value = checkTopVisible(navController)
        NavHost(
            navController = navController,
            startDestination = PrimoScreen.Home.name,
            modifier = modifier
                .padding(innerPadding)
                .nestedScroll(scrollBehavior.nestedScrollConnection),
        ) {

            //홈 화면
            composable(route = PrimoScreen.Home.name) {
                BackHandler(navController.previousBackStackEntry?.destination?.route == "Home") { // backstack이 Home 일 때만 앱종료
                    activity.finish()
                }
                HomeScreen(
                    onUploadButtonClicked = {
                        navController.navigate(PrimoScreen.UploadPost.name)
                        {
                            popUpTo("Home")
                        }
                    },
                    navController,
                    requestManager,
                    modifier = Modifier,
                    viewModel,
                    homeListState
                )
            }

            //테스트
            composable(route = PrimoScreen.Test.name) {
                TestDeep()
            }

            //계정 관리 화면
            composable(route = PrimoScreen.ManageAccount.name) {
                ManageAccountScreen(
                    onLogoutButton = {
                        FirebaseAuth.getInstance().signOut()
                        navController.navigate(PrimoScreen.Login.name)
                        {
                            popUpTo("Home")
                        }
                    },
                    navController,requestManager
                )
            }

            //지도
            val mapName = PrimoScreen.Map.name
            composable(route = "$mapName/{datePlanName}/{leaderUID}",
            arguments = listOf(
                navArgument("datePlanName"){
                    type = NavType.StringType
                },
                navArgument("leaderUID"){
                    type = NavType.StringType
                }
            )
            ) { entry->
                val datePlanName = entry.arguments?.getString("datePlanName")
                val leaderUID = entry.arguments?.getString("leaderUID")
                MapScreen(
                    navController,
                    requestManager,
                    datePlanName,
                    leaderUID,
                    onSearchButtonClicked = {
                        navController.navigate(PrimoScreen.Search.name)
                    }
                )
            }

            //즐겨찾기 화면

            composable(route = PrimoScreen.Favorites.name) {
                FavoritesScreen(
                    navController,
                    requestManager
                )
            }


            //데이트 계획 관리
            composable(route = PrimoScreen.DatePlans.name) {
                /*DatePlanScreen(
                    navController,
                    requestManager,
                    datePlanListState,
                    datePlanList
                )*/
                CalendarScreen(
                    month,
                    onMonthChange = {month = it},
                    requestManager,
                    datePlanList,
                    navController,
                    datePlanListState
                    )
            }


            //로그인 화면
            composable(route = PrimoScreen.Login.name) {
                BackHandler() { // .로그인일땐 그냥 꺼버리기
                    activity.finish()
                }
                LoginScreen(
                    onLoginButtonClicked = {isMember:Boolean ->
                        if(!isMember){
                            navController.navigate(PrimoScreen.MemberInit.name)
                            {
                                popUpTo("Home")
                            }
                        }
                        else{
                            navController.navigate(PrimoScreen.Home.name)
                            {
                                popUpTo("Home")
                            }
                            bottomBarState.value = true
                            topBarState.value = true
                        }
                    },
                    onRegisterScreenButtonClicked = {
                        navController.navigate(PrimoScreen.Register.name)
                    },
                    auth, activity
                )
            }


            //가입 화면
            composable(route = PrimoScreen.Register.name) {
                RegisterEmail_1(
                    onRegisterButtonClicked = { userEmail ->
                        navController.navigate("${PrimoScreen.Register2.name}/$userEmail")
                    }
                )
            }
            val pass2 = PrimoScreen.Register2.name
            composable(route = "$pass2/{userEmail}",
                arguments = listOf(
                    navArgument("userEmail"){
                        type = NavType.StringType
                    }
                )
            ) { entry->
                val userEmail = entry.arguments?.getString("userEmail")!!
                RegisterPass_2(
                    onRegisterButtonClicked = { userPassword ->
                        auth.createUserWithEmailAndPassword(userEmail, userPassword!!)
                            .addOnSuccessListener { task ->
                                navController.navigate(PrimoScreen.Home.name)
                            }
                    }
                ,userEmail
                )
            }


            // 회원 등록 화면
            composable(route = PrimoScreen.MemberInit.name) {
                BackHandler(true) { // backstack이 Home 일 때만 앱종료
                    activity.finish()
                }
                MemberInitScreen(
                    onSubmitButtonClicked = {
                        navController.navigate(PrimoScreen.RegisterPartnerID.name)
                        {
                            popUpTo("Home")
                        }
                    }
                )
            }

            composable(route = PrimoScreen.RegisterPartnerID.name){
                RegisterPartnerIDScreen(
                    onSubmitButtonClicked = {
                        navController.navigate(PrimoScreen.ManageAccount.name)
                    },
                    activity,
                    navController
                )
            }


            composable(route = PrimoScreen.RegisterPartner.name){
                RegisterPartnerScreen(
                onSubmitButtonClicked = {
                    navController.navigate(PrimoScreen.RegisterPartnerID.name)
                    {
                        popUpTo("Home")
                    }
                },activity,navController
                )

            }

            composable(route = PrimoScreen.SelectDateDate.name){
                SelectDateDateScreen(
                    onSubmitButtonClicked = {
                        //navController.navigate(PrimoScreen.RegisterPartnerID.name)
                    },activity,navController,datePlanList
                )

            }


            //검색
            composable(route = PrimoScreen.Search.name) {
                Search(requestManager,navController)
            }


            // 장소 취향 월드컵
            composable(route = PrimoScreen.PlaceBattle.name) {
                PlaceBattle(requestManager)
            }

            composable(route = PrimoScreen.SelectWritingCourse.name) {
                SelectCourse(navController,requestManager,datePlanList)
            }


            composable(route = PrimoScreen.WritingScreen.name) {
                WritingScreen(
                    navController,
                    requestManager
                )
            }

            val postscreen = PrimoScreen.PostScreen.name
            composable(route = "$postscreen/{item}",
                arguments = listOf(
                    navArgument("item"){
                        type = NavType.IntType
                    }
                )
            ) { entry->
                val item = entry.arguments?.getInt("item")!!
                Postdetail(
                    navController,
                    item,
                    requestManager,
                    viewModel
                )
            }

            val placeDetailScreen = PrimoScreen.PlaceDetailScreen.name
            composable(route = "$placeDetailScreen/{item}",
                arguments = listOf(
                    navArgument("item"){
                        type = NavType.IntType
                    }
                )
            ) { entry->
                val item = entry.arguments?.getInt("item")!!
                Placedetail(
                    navController,
                    item,
                    requestManager
                )
            }
        }
    }
}

@Composable
fun checkBottomVisible (navController:NavController): Boolean{
    val user = Firebase.auth.currentUser
    var bottomBarState:Boolean = false
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    if(user == null) {
        bottomBarState = false
    }
    else {
        when (navBackStackEntry?.destination?.route) {
            "Home" -> {
                bottomBarState = true
            }
            "Login" -> {
                bottomBarState = false
            }
            "Register2" -> {
                bottomBarState = false
            }
            "Register" -> {
                bottomBarState = false
            }
            "MemberInit" -> {
                bottomBarState = false
            }
            "UploadPost" -> {
                bottomBarState = false
            }
            "Map" -> {
                bottomBarState = false
            }
            "Favorites" -> {
                bottomBarState = true
            }
            "ManageAccount" -> {
                bottomBarState = true
            }
            "RegisterPartnerID" ->{
                bottomBarState = false
            }
            "RegisterPartner" ->{
                bottomBarState = false
            }
            "SelectDateDate" ->{
                bottomBarState = false
            }
            "DatePlans" ->{
                bottomBarState = true
            }
            "Search" ->{
                bottomBarState = false
            }
            "PlaceBattle" ->{
                bottomBarState = true
            }
            "SelectWritingCourse" ->{
                bottomBarState = false
            }
            "WritingScreen" ->{
                bottomBarState = false
            }
            "PostScreen" ->{
                bottomBarState = false
            }
            "PlaceDetailScreen" ->{
                bottomBarState = false
            }

        }
    }
    return bottomBarState
}

@Composable
fun checkTopVisible (navController:NavController): Boolean{
    val user = Firebase.auth.currentUser
    var TopBarState:Boolean = false
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    if(user == null) {
        TopBarState = false
    }
    else {
        when (navBackStackEntry?.destination?.route) {
            "Home" -> {
                TopBarState = true
            }
            "Login" -> {
                TopBarState = false
            }
            "Register" -> {
                TopBarState = false
            }
            "Register2" -> {
                TopBarState = false
            }
            "MemberInit" -> {
                TopBarState = false
            }
            "UploadPost" -> {
                TopBarState = false
            }
            "Map" -> {
                TopBarState = false
            }
            "Favorites" -> {
                TopBarState = true
            }
            "ManageAccount" -> {
                TopBarState = true
            }
            "RegisterPartnerID" ->{
                TopBarState = false
            }
            "RegisterPartner" ->{
                TopBarState = false
            }
            "SelectDateDate" ->{
                TopBarState = false
            }
            "DatePlans" ->{
                TopBarState = true
            }
            "Search" ->{
                TopBarState = false
            }
            "PlaceBattle" ->{
                TopBarState = false
            }
            "SelectWritingCourse" ->{
                TopBarState = false
            }
            "WritingScreen" ->{
                TopBarState = false
            }
            "PostScreen" ->{
                TopBarState = false
            }
            "PlaceDetailScreen" ->{
                TopBarState = false
            }
        }
    }
    return TopBarState
}

@Composable
fun InitailLoading(datePlanList: SnapshotStateList<DatePlanInfo>){

    getPlaceInfo() // 장소 정보
    getPartnerInfo() // 연인 정보
    getUserOrientation() // 유저 정보


    val user = Firebase.auth.currentUser
    val db = Firebase.firestore
    var leaderUID = ""
    LaunchedEffect(true) {
        if (user != null) {
            db.collection("users").document(user!!.uid)
                .get()
                .addOnSuccessListener { document ->
                    leaderUID = document.getString("leaderUID") as String
                    val database = Firebase.database.reference.child("DatePlan").child(leaderUID)
                    val postListener = object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            Log.e("하하","하하")
                            datePlanList.clear()
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

                                if (startDate != "null") {
                                    datePlanList.add(
                                        DatePlanInfo(
                                            title,
                                            startDate,
                                            endDate,
                                            course
                                        )
                                    )
                                }
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

    }
    LaunchedEffect(true) {
        if (weatherInfo.dateList.isEmpty()) {
            val call = ApiObject.retrofitService.GetWeather(
                data_type,
                num_of_rows,
                page_no,
                base_data,
                base_time,
                nx,
                ny
            )
            call.enqueue(object : retrofit2.Callback<WEATHER> {
                override fun onResponse(call: Call<WEATHER>, response: Response<WEATHER>) {
                    if (response.isSuccessful) {
                        val info = response.body()!!.response.body.items.item
                        for (i in 0 until info.size) {
                            if (info[i].category == "POP") {
                                weatherInfo.dateList.add(info[i].fcstDate)
                                weatherInfo.timeList.add(info[i].fcstTime)
                                weatherInfo.rainPercent.add(info[i].fcstValue.toInt())
                            }
                            if(info[i].category == "PTY"){
                                weatherInfo.typeList.add(info[i].fcstValue.toInt())
                            }
                            if(info[i].category == "REH"){
                                weatherInfo.humidity.add(info[i].fcstValue.toInt())
                            }
                            if(info[i].category == "SKY"){
                                weatherInfo.skyList.add(info[i].fcstValue.toInt())
                            }
                            if(info[i].category == "TMX"){
                                weatherInfo.maxTmp.add(info[i].fcstValue.toFloat())
                            }
                            if(info[i].category == "TMN"){
                                weatherInfo.minTmp.add(info[i].fcstValue.toFloat())
                            }
                            if(info[i].category == "WSD"){
                                weatherInfo.windSpeed.add(info[i].fcstValue.toFloat())
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<WEATHER>, t: Throwable) {
                    Log.d("api fail : ", t.message.toString())
                }
            })
        }
    }
}