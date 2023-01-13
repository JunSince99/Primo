package com.example.primo2.screen

import PostViewModel
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
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.bumptech.glide.RequestManager
import com.example.primo2.PostInfo
import com.example.primo2.activity.MainActivity
import com.example.primo2.getPlaceInfo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


enum class PrimoScreen() {
    Home,
    Login,
    Register,
    MemberInit,
    UploadPost,
    Map,
    Favorites,
    ManageAccount
}
@Composable
fun PrimoApp(activity: Activity, requestManager: RequestManager,modifier: Modifier = Modifier,viewModel: PostViewModel = viewModel()) {
    val auth: FirebaseAuth = Firebase.auth
    val navController = rememberNavController()
    getPlaceInfo()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val bottomBarState = rememberSaveable { (mutableStateOf(false)) } // 바텀 네비게이션바 보이게 할지 말지


    Scaffold(
        bottomBar = { NavigationBar(navController,bottomBarState.value) },
        backgroundColor = Color.White
    )  { innerPadding ->

        bottomBarState.value = checkBottomVisible(navController)
        val uiState by viewModel.postState.collectAsState()
        NavHost(
            navController = navController,
            startDestination = PrimoScreen.Home.name,
            modifier = modifier.padding(innerPadding)
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
                        viewModel
                    )
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
                    navController
                )
            }


            //지도
            composable(route = PrimoScreen.Map.name) {
                MapScreen(
                    navController
                )
            }

            //즐겨찾기 화면
            composable(route = PrimoScreen.Favorites.name) {
                FavoritesScreen(
                    navController
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
                RegisterScreen(
                    onRegisterButtonClicked = {
                        navController.navigate(PrimoScreen.Home.name)
                        {
                            popUpTo("Home")
                        }
                    }
                )
            }


            // 회원 등록 화면
            composable(route = PrimoScreen.MemberInit.name) {
                MemberInitScreen(
                    onSubmitButtonClicked = {
                        navController.navigate(PrimoScreen.Home.name)
                        {
                            popUpTo("Home")
                        }
                    }
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
                bottomBarState = true
            }
            "Favorites" -> {
                bottomBarState = true
            }
            "ManageAccount" -> {
                bottomBarState = true
            }
        }
    }
    return bottomBarState
}


@Preview
@Composable
fun LoginPreview(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ){
        Text(text = "로그인 화면", style = MaterialTheme.typography.h4)

        var email by remember { mutableStateOf("") }

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("이메일") }
        )

        var password by remember { mutableStateOf("") }

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("패스워드") }
        )

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {}
        ) {
            Text("로그인")
        }
    }
}



