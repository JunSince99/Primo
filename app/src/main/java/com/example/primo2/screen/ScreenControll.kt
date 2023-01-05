package com.example.primo2.screen

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
    UploadPost
}

@Composable
fun PrimoApp(activity: Activity, requestManager: RequestManager,modifier: Modifier = Modifier) {
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
                            postList,
                            requestManager
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

@Preview
@Composable
fun LoginPreview(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(16.dp).fillMaxWidth(),
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