package com.example.primo2.screen

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Text
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.*
import com.example.primo2.PostInfo
import com.example.primo2.ui.theme.LazyColumnExampleTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import java.time.format.DateTimeFormatter
import java.util.logging.Handler



@Composable
fun HomeScreen(
    onUploadButtonClicked: () -> Unit = {},
    postList:ArrayList<PostInfo>,
    modifier: Modifier = Modifier
){
        LazyColumnExampleTheme() {
            Surface(
                modifier = Modifier, // 속성 정하는거(패딩, 크기 등)
                color = MaterialTheme.colors.background // app.build.gradle에서 색 지정 가능
            ) {
                Posts(postList)
            }
        }


}
// 게시글들을 띄우는 함수
@Composable
fun Posts(postList : ArrayList<PostInfo>,
          modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier.padding(vertical = 4.dp)) { // RecyclerView이 compose에서는 LazyColumn, LazyRow로 대체됨
        item{
            for (i in 0 until postList.size){ // UI에 for문도 가능
                Post(postList[i]) // 대충 만들어 놓은 게시글 포맷
            }
        }
    }
}
@Composable
fun Post(postInfo: PostInfo) {
    Surface(
        color = MaterialTheme.colors.primary, //primary color 내가 따로 저 연노랑으로 설정해놓음 대충
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Column(
            modifier = Modifier
        ) {
            if (postInfo.title != null) {
                Text(
                    text = postInfo.title,
                    textAlign = TextAlign.Center,
                    fontWeight = Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
            }
            if (postInfo.Contents[0] != null) // 일단 첫번째 사진만 표사ㅣ
            {
                AsyncImage(

                    model = postInfo.Contents[0], // 여기에 이미지 주소 넣으면 나옴
                    contentDescription = null,
                    modifier = Modifier.size(128.dp)
                )
            }
            if(postInfo.PostDate != null) {
                val date = postInfo.PostDate.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일"))
                Text(

                    text = date,
                    fontSize = 10.sp,
                    textAlign = TextAlign.Right,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
            }
        }
    }
}
/*
@Preview(showBackground = true) //미리보기 용
@Composable
fun DefaultPreview() {
    LazyColumnExampleTheme {
        Post()
    }
}

@Preview //미리보기 용
@Composable
fun PostsPreview() {
    LazyColumnExampleTheme {
        Posts()
    }
}*/
