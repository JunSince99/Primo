package com.example.primo2.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController

@Composable
fun SearchScreen(
    naviController: NavController,
    modifier: Modifier = Modifier
) {
    Scaffold(
        bottomBar = {
            NavigationBar(naviController)
        },
        backgroundColor = Color.White
    )
    {
        modifier.padding(it)
        Text(text = "서치 페이지", style = MaterialTheme.typography.h4)

    }
}

