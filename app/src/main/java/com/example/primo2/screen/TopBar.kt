package com.example.primo2.screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import androidx.navigation.NavController
import com.example.primo2.R
import com.example.primo2.myName
import com.example.primo2.ui.theme.LightPink
import com.example.primo2.ui.theme.LightRed
import com.example.primo2.ui.theme.Typography
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.YearMonth

fun Dateform(year : YearMonth) : String {
    var temp = year.toString().replace("-","년 ")
    if (year.monthValue < 10) temp = temp.substring(0,6) + temp.substring(7) + "월"
    return temp
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navController: NavController, name: String?, TopBarState:Boolean, topBarText:String, homeListState: LazyListState, datePlanListState: LazyListState, scrollBehavior: TopAppBarScrollBehavior,month:YearMonth,) {
    if(TopBarState) {
        val topAppBarElementColor = Color.Black
        if (name == "DatePlans") {
            TopAppBar(
                title = {
                        Text(
                            text = Dateform(month),
                            maxLines = 1,
                            style = Typography.h1,
                            overflow = TextOverflow.Ellipsis
                        )
                },
                actions = {
                    /*
                        IconButton(onClick = { navController.navigate(PrimoScreen.SelectDateDate.name) }) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Add,
                                    contentDescription = "데이트 추가"
                                )
                                Text(
                                    text = "데이트생성",
                                    color = Color.Black,
                                    fontSize = 8.sp
                                )
                            }
                        }

                     */
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Color.White,
                    scrolledContainerColor = Color.White,
                    navigationIconContentColor = topAppBarElementColor,
                    titleContentColor = Color.Black,
                    actionIconContentColor = topAppBarElementColor,
                ),
            )
        } else if(name == "Home") {
            TopAppBar(
                title = {
                    Row() {
                        Spacer(modifier = Modifier.width(20.dp))
                        Image(
                            painter = painterResource(id = R.drawable.logo),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(65.dp)
                        )
                    }
                },
                actions = {
                          IconButton(onClick = { navController.navigate(PrimoScreen.SelectWritingMethod.name) }) {
                              Icon(
                                  imageVector = Icons.Filled.Add,
                                  contentDescription = "데이트 추가",
                              )
                          }

                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Color.White,
                    scrolledContainerColor = Color.White,
                    navigationIconContentColor = topAppBarElementColor,
                    titleContentColor = Color.Black,
                    actionIconContentColor = topAppBarElementColor,
                ),
                scrollBehavior = scrollBehavior,
                modifier = Modifier,
            )
        } else if(name == "ManageAccount") {
            var isExpanded by remember { mutableStateOf(false)}
            TopAppBar(
                title = {
                    Row() {
                        Spacer(modifier = Modifier.width(20.dp))
                        Image(
                            painter = painterResource(id = R.drawable.logo),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(65.dp)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { isExpanded = true }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "설정",
                        )
                    }
                    DropdownMenuNoPaddingVeitical(
                        expanded = isExpanded,
                        onDismissRequest = { isExpanded = false },
                        modifier = Modifier.background(Color.White)
                    ) {
                        androidx.compose.material.DropdownMenuItem(
                            onClick = {
                                myName = ""
                                FirebaseAuth.getInstance().signOut()
                                navController.navigate(PrimoScreen.Login.name)
                                {
                                    popUpTo("Home")
                                }
                            }
                        ) {
                            Text(text = "로그아웃")
                        }
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Color.White,
                    scrolledContainerColor = Color.White,
                    navigationIconContentColor = topAppBarElementColor,
                    titleContentColor = Color.Black,
                    actionIconContentColor = topAppBarElementColor,
                ),
                modifier = Modifier,
            )
        }
    }
/*
    if(TopBarState) {
        if (name == "DatePlans") {
            TopAppBar(
                backgroundColor = Color.White,
                contentColor = Color.Black,
                elevation = shadow,
            ) {
                Row(modifier = Modifier.fillMaxHeight(), horizontalArrangement = Arrangement.Start)
                {
                    Text(
                        text = topBarText,
                        modifier = Modifier
                            .padding(12.dp),
                        color = Color.Black,
                        style = Typography.h5.copy(fontSize = 20.sp),
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                )
                {
                    Column(
                        modifier = Modifier.clickable { navController.navigate(PrimoScreen.SelectDateDate.name) },
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            modifier = Modifier
                                .size(30.dp),
                            tint = Color.Black
                        )
                        Text(
                            text = "데이트생성",
                            color = Color.Black,
                            style = Typography.h5.copy(fontSize = 10.sp)
                        )
                    }
                }
            }
        } else {
            TopAppBar(
                backgroundColor = Color.White,
                contentColor = Color.Black,
                elevation = shadow
            )
            {
                Row(modifier = Modifier.fillMaxHeight(), horizontalArrangement = Arrangement.Start)
                {
                    Text(
                        text = topBarText,
                        modifier = Modifier
                            .padding(12.dp),
                        color = Color.Black,
                        style = Typography.h5,
                    )
                }
            }
        }
    }*/

}