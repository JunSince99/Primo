package com.example.primo2.screen

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
    UploadPost
}

@Composable
fun PrimoApp(activity: Activity, modifier: Modifier = Modifier) {
    var postList:ArrayList<PostInfo> = arrayListOf()
    val auth: FirebaseAuth = Firebase.auth
    val navController = rememberNavController()
    Scaffold() { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = PrimoScreen.Login.name,
            modifier = modifier.padding(innerPadding)
        ) {

            composable(route = PrimoScreen.Home.name) {
                val user = Firebase.auth.currentUser
                if(user == null) { // 로그인 x이면 로그인화면으로 가기
                    navController.navigate(PrimoScreen.Login.name)
                }
                else {
                        HomeScreen(
                            onUploadButtonClicked = {
                                navController.navigate(PrimoScreen.UploadPost.name)
                            },
                            postList
                        )
                }
            }
            composable(route = PrimoScreen.Login.name) {
                LoginScreen(
                    onLoginButtonClicked = {isMember:Boolean, _postList:ArrayList<PostInfo> ->
                        postList = _postList
                        if(!isMember){
                            navController.navigate(PrimoScreen.MemberInit.name)
                        }
                        else{
                            navController.navigate(PrimoScreen.Home.name)
                        }
                    },
                    onRegisterScreenButtonClicked = {
                        navController.navigate(PrimoScreen.Register.name)
                    },
                    auth, activity
                )
            }
            composable(route = PrimoScreen.Register.name) {
                RegisterScreen(
                    onRegisterButtonClicked = {
                        navController.navigate(PrimoScreen.Home.name)
                    }
                )
            }
            composable(route = PrimoScreen.MemberInit.name) {
                MemberInitScreen(
                    onSubmitButtonClicked = {
                        navController.navigate(PrimoScreen.Home.name)
                    }
                )
            }




        }

    }
}