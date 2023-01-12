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
fun ManageAccountScreen(
    onLogoutButton: () -> Unit = {},
    naviController: NavController,
    modifier: Modifier = Modifier
) {
    Scaffold()
    {
        modifier.padding(it)
        Text(text = "계정 관리 페이지", style = MaterialTheme.typography.h4)

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { onLogoutButton() }
        ) {
            Text("로그아웃")
        }
    }
}

