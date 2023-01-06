package com.example.primo2.screen

import PostViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import android.os.Bundle
import android.util.Log
import android.view.RoundedCorner
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavController
import com.bumptech.glide.RequestManager
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.primo2.PostInfo
import com.example.primo2.ui.theme.LazyColumnExampleTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import java.time.format.DateTimeFormatter



@Composable
fun HomeScreen(
    onUploadButtonClicked: () -> Unit = {},
    navController: NavController,
    requestManager: RequestManager,
    modifier: Modifier = Modifier
){
        LazyColumnExampleTheme() {
            Surface(
                modifier = Modifier, // 속성 정하는거(패딩, 크기 등)
                color = MaterialTheme.colors.onBackground // app.build.gradle에서 색 지정 가능
            ) {
                Scaffold(
                    bottomBar = { NavigationBar(navController) },
                    backgroundColor = Color.White
                ) { padding ->
                    Posts(requestManager, Modifier.padding(padding))
                }
            }
        }


}

// 게시글들을 띄우는 함수
@Composable
fun Posts(requestManager: RequestManager,
          modifier: Modifier = Modifier,
          viewModel: PostViewModel = viewModel()
)
{
    val uiState by viewModel.postState.collectAsState()
    viewModel.updatePostInformation()
    LazyColumn(modifier = modifier) { // RecyclerView이 compose에서는 LazyColumn, LazyRow로 대체됨
        item{
            for (i in 0 until uiState.size){ // UI에 for문도 가능
                Post(uiState[i],requestManager) // 대충 만들어 놓은 게시글 포맷
            }
        }
    }
}
@OptIn(ExperimentalGlideComposeApi::class, ExperimentalPagerApi::class)
@Composable
fun Post(postInfo: PostInfo,requestManager: RequestManager) {
    Surface(
        color = MaterialTheme.colors.onBackground, //primary color 내가 따로 저 연노랑으로 설정해놓음 대충
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .border(
                    width = 1.dp,
                    shape = RoundedCornerShape(7),
                    color = Color.LightGray
                )

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
            if (postInfo.Contents[0] != null)
            {
                HorizontalPager(modifier = Modifier.fillMaxSize(),
                    count = postInfo.Contents.size
                ) { page ->
                    GlideImage(
                        model = postInfo.Contents[page], // 여기에 이미지 주소 넣으면 나옴
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(350.dp)
                            .fillMaxWidth()
                            .align(Alignment.CenterHorizontally)
                    )
                    {
                        it
                            .thumbnail(
                                requestManager
                                    .asDrawable()
                                    .load(postInfo.Contents[0])
                                    // .signature(signature)
                                    .override(128)
                            )
                        // .signature(signature)
                    }
                }

            }
            if(postInfo.PostDate != null) {
                val date = postInfo.PostDate.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일"))
                Text(
                    text = date,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Right,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }
        }
    }

    BackHandler(true){
        Log.e("백","백")
    }

}


fun Modifier.coloredShadow(
    color: Color,
    alpha: Float = 0.2f,
    borderRadius: Dp = 0.dp,
    shadowRadius: Dp = 20.dp,
    offsetY: Dp = 0.dp,
    offsetX: Dp = 0.dp
) = composed {

    val shadowColor = color.copy(alpha = alpha).toArgb()
    val transparent = color.copy(alpha= 0f).toArgb()

    this.drawBehind {

        this.drawIntoCanvas {
            val paint = Paint()
            val frameworkPaint = paint.asFrameworkPaint()
            frameworkPaint.color = transparent

            frameworkPaint.setShadowLayer(
                shadowRadius.toPx(),
                offsetX.toPx(),
                offsetY.toPx(),
                shadowColor
            )
            it.drawRoundRect(
                0f,
                0f,
                this.size.width,
                this.size.height,
                borderRadius.toPx(),
                borderRadius.toPx(),
                paint
            )
        }
    }
}