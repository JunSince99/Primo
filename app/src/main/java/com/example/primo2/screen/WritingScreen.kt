package com.example.primo2.screen

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bumptech.glide.RequestManager
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.primo2.*
import com.example.primo2.R
import com.example.primo2.ui.theme.spoqasans
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.ml.modeldownloader.CustomModel
import com.google.firebase.ml.modeldownloader.CustomModelDownloadConditions
import com.google.firebase.ml.modeldownloader.DownloadType
import com.google.firebase.ml.modeldownloader.FirebaseModelDownloader
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storageMetadata
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.lang.Byte
import java.lang.Float
import java.net.URL
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.Array
import kotlin.Int
import kotlin.OptIn
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.arrayOf
import kotlin.floatArrayOf
import kotlin.math.min


@Composable
fun WritingScreen(navController: NavController,requestManager: RequestManager) {
    val isSpam = ArrayList<ArrayList<Double>>()
    val isBackground  = ArrayList<ArrayList<Double>>()
    val isPerson = ArrayList<ArrayList<Double>>()
    val postImageResource = remember { mutableStateListOf<MutableList<Uri>>() }
    val articleList = remember { mutableStateListOf<String>() }
    var titlename by remember { mutableStateOf("") }
    var uploading  by remember { mutableStateOf(false) }

    articleList.clear()
    for(i in 0 until  postPlaceList.size)
    {
        postImageResource.add(arrayListOf())
        isSpam.add(arrayListOf())
        isPerson.add(arrayListOf())
        isBackground.add(arrayListOf())
        articleList.add("")
    }
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (uploading){
                CircularProgressIndicator()
            }
            //탑바
            Writingtopbar(navController, articleList, titlename,postImageResource,isSpam,isBackground,isPerson,uploading, onUploadChange = {uploading  = it})
            //제목
            Titletextfield(titlename,onTitleChange = { titlename = it})
            //내용
            placearticle(requestManager,articleList,onArticleChange = {sequence, article ->
            articleList[sequence] = article},postImageResource, onImageChange = { sequence,urlList ->
                postImageResource[sequence] = urlList
            },isSpam,isBackground,isPerson)
        }
    }
}

@Composable
fun Writingtopbar(navController: NavController,
                  articleList:MutableList<String>,
                  titlename:String,
                  postImageResource: MutableList<MutableList<Uri>>,
                  isSpam: ArrayList<ArrayList<Double>>,
                  isBackground: ArrayList<ArrayList<Double>>,
                  isPerson: ArrayList<ArrayList<Double>>,
                  uploading:Boolean,
                  onUploadChange:(Boolean) -> Unit
) {
    Surface(
        color = Color.White,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 8.dp)
                    .fillMaxWidth()
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(45.dp)
                        .clip(CircleShape)
                        .clickable { navController.navigateUp() }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null,
                        modifier = Modifier
                            .size(25.dp),
                        tint = Color.Black
                    )
                }
                Text(
                    text = "게시글 작성",
                    textAlign = TextAlign.Center,
                    color = Color.Black,
                    fontFamily = spoqasans,
                    fontWeight = FontWeight.Medium,
                    fontSize = 20.sp
                )


                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(45.dp)
                        .clickable {
                            if(!uploading) {

                                onUploadChange(true)
                                if (titlename.isNotBlank()) {
                                    val firebaseFirestore = Firebase.firestore
                                    val documentReference = firebaseFirestore
                                        .collection("posts")
                                        .document()
                                    val user = Firebase.auth.currentUser
                                    val imageResource: ArrayList<String> = arrayListOf()
                                    val splitNumber: ArrayList<Int> = arrayListOf()
                                    val spam: ArrayList<Double> = arrayListOf()
                                    val background: ArrayList<Double> = arrayListOf()
                                    val person: ArrayList<Double> = arrayListOf()
                                    val storage = FirebaseStorage.getInstance()
                                    val storageRef = storage.reference
                                    var success = 0
                                    for (i in 0 until postPlaceList.size) {
                                        success += postImageResource[i].size
                                        for (j in 0 until postImageResource[i].size) {
                                            imageResource.add("")
                                        }
                                    }
                                    var indexNum = 0
                                    val placeName:ArrayList<String> = arrayListOf()
                                    for (i in 0 until postPlaceList.size) {
                                        placeName.add(postPlaceList[i])
                                        spam.addAll(isSpam[i])
                                        background.addAll(isBackground[i])
                                        person.addAll(isPerson[i])
                                        if(i == 0) {
                                            splitNumber.add(postImageResource[i].size)
                                        }
                                        else{
                                            splitNumber.add(postImageResource[i].size + splitNumber[i-1])
                                        }
                                        for (j in 0 until postImageResource[i].size) {


                                            val pathArray: Array<String> =
                                                postImageResource[i][j]
                                                    .toString()
                                                    .split("/", ".")
                                                    .toTypedArray()
                                            val mountainImagesRef: StorageReference =
                                                storageRef.child("posts/" + documentReference.id + "/" + (i * postImageResource[i].size + j) + "." + pathArray[pathArray.size - 1])
                                            val file = postImageResource[i][j]
                                            val metadata = storageMetadata {
                                                setCustomMetadata(
                                                    "index",
                                                    "" + indexNum
                                                )
                                            }

                                            val uploadTask =
                                                mountainImagesRef.putFile(file, metadata)
                                            uploadTask
                                                .addOnFailureListener {
                                                }
                                                .addOnSuccessListener { taskSnapshot ->
                                                    val index: Int = taskSnapshot.metadata
                                                        ?.getCustomMetadata("index")!!
                                                        .toInt()
                                                    mountainImagesRef.downloadUrl.addOnSuccessListener { ImageUri ->
                                                        imageResource.removeAt(index)
                                                        imageResource.add(
                                                            index,
                                                            ImageUri.toString()
                                                        )
                                                        success--
                                                        if (success == 0) {
                                                            val postInfo =
                                                                PostInfo(
                                                                    documentReference.id,
                                                                    titlename,
                                                                    ArrayList(articleList),
                                                                    splitNumber,
                                                                    imageResource,
                                                                    spam,
                                                                    background,
                                                                    person,
                                                                    placeName,
                                                                    myName,
                                                                    user!!.uid,
                                                                    LocalDate.now().format(
                                                                        DateTimeFormatter.ofPattern("yyyy-MM-dd "))+ LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")
                                                                    ),
                                                                    score = 0
                                                                )
                                                            uploader(
                                                                documentReference,
                                                                postInfo,
                                                                navController
                                                            )
                                                        }
                                                    }
                                                }

                                            indexNum++
                                        }
                                    }
                                }
                            }
                        }
                ) {
                        Text(
                            text = "완료",
                            textAlign = TextAlign.Center,
                            color = Color.Black,
                            fontFamily = spoqasans,
                            fontWeight = FontWeight.Normal
                        )
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun placearticle(requestManager:RequestManager,
                 articleList: MutableList<String>,
                 onArticleChange:(Int, String) -> Unit,
                 postImageResource:MutableList<MutableList<Uri>>,
                 onImageChange:(Int,MutableList<Uri>) -> Unit,isSpam: ArrayList<ArrayList<Double>>,
                 isBackground: ArrayList<ArrayList<Double>>,
                 isPerson: ArrayList<ArrayList<Double>>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        items(postPlaceList.size) { item ->
            PostPlaceWrite(item,requestManager,articleList, onArticleChange,postImageResource,onImageChange,isSpam,isBackground,isPerson)
            Spacer(modifier = Modifier.padding(vertical = 10.dp))
        }
    }

}

@Composable
fun Titletextfield(titlename: String, onTitleChange:(String) -> Unit) {

    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        value = titlename,
        onValueChange = { text ->
            onTitleChange(text)
        },
        placeholder = {
            Text(
                modifier = Modifier
                    .alpha(ContentAlpha.medium),
                text = "제목을 입력해주세요.",
                color = Color.Gray,
                fontSize = 25.sp,
                fontFamily = spoqasans,
                fontWeight = FontWeight.Bold
            )
        },
        textStyle = TextStyle(
            fontSize = 25.sp,
            fontFamily = spoqasans,
            fontWeight = FontWeight.Bold
        ),
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
        ),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.White,
            cursorColor = Color.Black,
            focusedIndicatorColor = Color.White,
            unfocusedIndicatorColor = Color.White
        )
    )
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PostPlaceWrite(item:Int,
                   requestManager: RequestManager,
                   articleList: MutableList<String>,
                   onArticleChange:(Int, String) -> Unit,
                   postImageResource:MutableList<MutableList<Uri>>,
                   onImageChange:(Int,MutableList<Uri>) -> Unit,
                   isSpam:ArrayList<ArrayList<Double>>,
                   isBackground: ArrayList<ArrayList<Double>>,
                   isPerson: ArrayList<ArrayList<Double>>)
{
    val areaImageList = remember { mutableStateListOf<Uri>() }
    val conditions = CustomModelDownloadConditions.Builder()
        .requireWifi()
        .build()

    val context = LocalContext.current
    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) { uriList ->
            for(i in 0 until uriList.size){
                areaImageList.add(uriList[i])
                val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver,uriList[i])
                val bitmap2 = Bitmap.createScaledBitmap(bitmap, 150, 150, true)
                val input = ByteBuffer.allocateDirect(150*150*3*4).order(ByteOrder.nativeOrder())
                for (y in 0 until 150) {
                    for (x in 0 until 150) {
                        val px = bitmap2.getPixel(x, y)

                        // Get channel values from the pixel value.
                        val r = android.graphics.Color.red(px)
                        val g = android.graphics.Color.green(px)
                        val b = android.graphics.Color.blue(px)

                        // Normalize channel values to [-1.0, 1.0]. This requirement depends on the model.
                        // For example, some models might require values to be normalized to the range
                        // [0.0, 1.0] instead.
                        val rf = (r - 127) / 255f
                        val gf = (g - 127) / 255f
                        val bf = (b - 127) / 255f

                        input.putFloat(rf)
                        input.putFloat(gf)
                        input.putFloat(bf)
                    }
                }
                FirebaseModelDownloader.getInstance()
                    .getModel("primodeep", DownloadType.LOCAL_MODEL_UPDATE_IN_BACKGROUND,
                        conditions)
                    .addOnSuccessListener { model: CustomModel? ->
                        val modelFile = model?.file
                        var interpreter: Interpreter? = null
                        if (modelFile != null) {
                            interpreter = Interpreter(modelFile)
                        }
                        val bufferSize = 1000 * Float.SIZE / Byte.SIZE
                        val modelOutput = ByteBuffer.allocateDirect(bufferSize).order(ByteOrder.nativeOrder())
                        interpreter?.run(input,modelOutput)
                        modelOutput.rewind()
                        isSpam[item].add(modelOutput.float.toDouble())
                        isBackground[item].add(modelOutput.float.toDouble())
                        isPerson[item].add(modelOutput.float.toDouble())
                    }
                    .addOnFailureListener{
                        Log.e("실패",""+it)
                    }
            }
            onImageChange(item,areaImageList)
        }

    Surface(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .shadow(
                elevation = 1.dp,
                shape = RoundedCornerShape(10)
            )
    ) {
        Column {
            Spacer(modifier = Modifier.size(16.dp))
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                //장소
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ){
                    val url = placeListHashMap[postPlaceList[item]]?.imageResource
                    GlideImage(
                        model = url,
                        contentDescription = "",
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(60.dp),
                        contentScale = ContentScale.Crop

                    )
                    {
                        it
                            .thumbnail(
                                requestManager
                                    .asDrawable()
                                    .load(url)
                                    // .signature(signature)
                                    .override(64)
                            )
                    }
                    Spacer(modifier = Modifier.padding(6.dp))
                    Column(
                        modifier = Modifier,
                    ) {
                        var placeName = ""
                        if (postPlaceList.size != 0) {
                            placeName = placeListHashMap[postPlaceList[item]]!!.placeName
                        }
                        Text(
                            text = placeName,
                            color = Color.Black,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                        )
                        Spacer(modifier = Modifier.padding(2.dp))
                        Text(
                            text = "인천광역시 송도동",
                            color = Color.DarkGray,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier
                        )
                    }
                }
                Button(
                    onClick = {
                        galleryLauncher.launch("image/*")
                    },
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.White,
                        contentColor = Color.Black
                    ),
                    modifier = Modifier
                        .size(width = 50.dp, height = 50.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_image_24),
                        contentDescription = null,
                        modifier = Modifier
                            .size(20.dp),
                    )
                }
            }
            //내용
            Spacer(modifier = Modifier.size(8.dp))
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(10.dp))
            ) {
                val contrast = 1f // 0f..10f (1 should be default)
                val brightness = -50f // -255f..255f (0 should be default)
                val colorMatrix = floatArrayOf(
                    contrast, 0f, 0f, 0f, brightness,
                    0f, contrast, 0f, 0f, brightness,
                    0f, 0f, contrast, 0f, brightness,
                    0f, 0f, 0f, 1f, 0f
                )
                val loopValue = min(areaImageList.size,5)
                for(i in 0 until loopValue) {
                    if (i == 4) {
                        Box(
                            modifier = Modifier
                        ) {
                            GlideImage(
                                model = areaImageList[i],
                                contentDescription = "",
                                modifier = Modifier
                                    .clip(RectangleShape)
                                    .size(65.dp),
                                colorFilter = ColorFilter.colorMatrix(ColorMatrix(colorMatrix)),
                                contentScale = ContentScale.Crop

                            )
                            {
                                it
                                    .thumbnail(
                                        requestManager
                                            .asDrawable()
                                            .load(areaImageList[i])
                                            // .signature(signature)
                                            .override(64)
                                    )
                            }
                            Icon(
                                painter = painterResource(id = R.drawable.ic_baseline_more_horiz_24),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(30.dp)
                                    .align(Alignment.Center),
                                tint = Color.White
                            )
                        }
                    } else {
                        GlideImage(
                            model = areaImageList[i],
                            contentDescription = "",
                            modifier = Modifier
                                .clip(RectangleShape)
                                .size(65.dp),
                            contentScale = ContentScale.Crop

                        )
                        {
                            it
                                .thumbnail(
                                    requestManager
                                        .asDrawable()
                                        .load(areaImageList[i])
                                        // .signature(signature)
                                        .override(64)
                                )
                        }

                    }
                }
            }

            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = articleList[item],
                onValueChange = { text ->
                    onArticleChange(item, text)
                },
                placeholder = {
                    Text(
                        modifier = Modifier
                            .alpha(ContentAlpha.medium),
                        text = "내용을 입력해주세요.",
                        color = Color.Gray,
                        fontSize = 16.sp,
                        fontFamily = spoqasans,
                        fontWeight = FontWeight.Medium
                    )
                },
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = spoqasans,
                    fontWeight = FontWeight.Medium
                ),
                singleLine = false,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                ),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.White,
                    cursorColor = Color.Black,
                    focusedIndicatorColor = Color.White,
                    unfocusedIndicatorColor = Color.White
                )
            )
        }
    }
}

@Suppress("DEPRECATION", "NewApi")
private fun Uri.parseBitmap(context: Context): Bitmap {
    return when (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) { // 28
        true -> {
            val source = ImageDecoder.createSource(context.contentResolver, this)
            ImageDecoder.decodeBitmap(source)
        }
        else -> {
            MediaStore.Images.Media.getBitmap(context.contentResolver, this)
        }
    }
}

private fun uploader(documentReference: DocumentReference, postInfo: PostInfo,navController: NavController){
    documentReference.set(postInfo)
        .addOnSuccessListener {
            navController.navigate(PrimoScreen.Home.name)
           Log.e("","포스트 업로드 성공")
        }
    }

