package com.example.primo2.screen

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.primo2.ui.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navController: NavController, name: String?, TopBarState:Boolean, topBarText:String, homeListState: LazyListState, datePlanListState: LazyListState, scrollBehavior: TopAppBarScrollBehavior,month:String) {
    if(TopBarState) {
        val topAppBarElementColor = Color.Black
        if (name == "DatePlans") {
            TopAppBar(
                title = {
                        Text(
                            text = month,
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
        } else {
            TopAppBar(
                title = {
                    Text(
                        "Primo",
                        maxLines = 1,
                        style = Typography.h1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Color.White,
                    scrolledContainerColor = Color.White,
                    navigationIconContentColor = topAppBarElementColor,
                    titleContentColor = Color.Black,
                    actionIconContentColor = topAppBarElementColor,
                ),
                scrollBehavior = scrollBehavior,
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