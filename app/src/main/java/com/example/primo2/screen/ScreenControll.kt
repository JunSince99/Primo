package com.example.primo2.screen

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bumptech.glide.RequestManager
import com.example.primo2.PostInfo
import com.example.primo2.activity.MainActivity
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
    Search,
    Favorites,
    ManageAccount
}
@Composable
fun PrimoApp(activity: Activity, requestManager: RequestManager,modifier: Modifier = Modifier) {
    val auth: FirebaseAuth = Firebase.auth
    val navController = rememberNavController()
    Log.e("호출","호출")
    Scaffold() { innerPadding ->
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
                val user = Firebase.auth.currentUser
                if(user == null) {
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
                            }
                        },
                        onRegisterScreenButtonClicked = {
                            navController.navigate(PrimoScreen.Register.name)
                        },
                        auth, activity
                    )
                }
                else {
                        HomeScreen(
                            onUploadButtonClicked = {
                                navController.navigate(PrimoScreen.UploadPost.name)
                                {
                                    popUpTo("Home")
                                }
                            },
                            navController,
                            requestManager
                        )
                }
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


            //검색 화면
            composable(route = PrimoScreen.Search.name) {
                SearchScreen(
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
