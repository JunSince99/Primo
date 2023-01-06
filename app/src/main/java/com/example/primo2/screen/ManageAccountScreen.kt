package com.example.primo2.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier

@Composable
fun ManageAccountScreen(
    onLogoutBotton: () -> Unit = {},
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier.padding(16.dp).fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ){
        Text(text = "계정 관리 페이지", style = MaterialTheme.typography.h4)
    }

    Button(
        modifier = Modifier.fillMaxWidth(),
        onClick = { onLogoutBotton() }
    ){
        Text("로그아웃")
    }
}

