package com.example.primo2.activity

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.example.primo2.screen.PrimoApp
import com.example.primo2.screen.PrimoScreen
import com.example.primo2.screen.Search
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

val num_of_rows = 1000
val page_no = 1
val data_type = "JSON"
val base_time = 1100
val base_data = 20230423
val nx = "55"
val ny = "127"

data class WEATHER (
    val response : RESPONSE
)
data class RESPONSE (
    val header : HEADER,
    val body : BODY
)
data class HEADER(
    val resultCode : Int,
    val resultMsg : String
)
data class BODY(
    val dataType : String,
    val items : ITEMS
)
data class ITEMS(
    val item : List<ITEM>
)
data class ITEM(
    val baseDate : Int,
    val baseTime : Int,
    val category : String,
    val fcstDate : String,
    val fcstTime : String,
    val fcstValue : String
)

interface WeatherInterface {
    @GET("getVilageFcst?serviceKey=03CPR7byhzPvUucGTMnB8gYLFbeIvj2BdTcOYpdPdnE4hC0pTm4DcHzyo7DWcH6dR1N3gCLQylpqCjY2Acwtqg%3D%3D")
    fun GetWeather(
        @Query("dataType") data_type : String,
        @Query("numOfRows") num_of_rows : Int,
        @Query("pageNo") page_no : Int,
        @Query("base_date") base_date : Int,
        @Query("base_time") base_time : Int,
        @Query("nx") nx : String,
        @Query("ny") ny : String
    ): Call<WEATHER>
}


private val retrofit = Retrofit.Builder()
    .baseUrl("https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/") // 마지막 / 반드시 들어가야 함
    .addConverterFactory(GsonConverterFactory.create()) // converter 지정
    .build() // retrofit 객체 생성

object ApiObject {
    val retrofitService: WeatherInterface by lazy {
        retrofit.create(WeatherInterface::class.java)
    }
}


class MainActivity : AppCompatActivity() {
    private lateinit var mGlideRequestManager: RequestManager
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        mGlideRequestManager = Glide.with(this);
        setContent {
            //val intent = Intent(this, UploadPostActivity::class.java)
            //this.setStatusBarTransparent()
            PrimoApp(this, mGlideRequestManager, modifier = Modifier)

            //MapScreen()
        }
    }

}


/*
fun Activity.setStatusBarTransparent() {
    window.apply {
        setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
    }
    if(Build.VERSION.SDK_INT >= 30) {	// API 30 에 적용\
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }
}
*/
//@Preview
//@Composable
//fun DefaultPreview() {
//    LazyColumnExampleTheme() {
//        PrimoApp()
//    }
//}

/*
class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = Firebase.auth
        val user = Firebase.auth.currentUser
        if(user == null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        else{
                val db = Firebase.firestore
                val docRef = db.collection("users").document(user.uid)
                docRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        if(document.exists()){

                        }
                        else{
                            val intent = Intent(this, MemberInitActivity::class.java)
                            startActivity(intent)
                        }
                    }
                    else {

                    }
                }
                .addOnFailureListener {
                }
            val recyclerView: RecyclerView = findViewById<RecyclerView>(R.id.postRecyclerView)

            val linearLayout = LinearLayoutManager(applicationContext)

            db.collection("posts")
                //.whereEqualTo("capital", true)
                .get()
                .addOnSuccessListener { documents ->
                    val postList:ArrayList<PostInfo> = arrayListOf()

                    for (document in documents) {
                        postList.add(
                            PostInfo(
                                document.getString("title"),
                                document.data["contents"] as ArrayList<String?>,
                                document.get("comments").toString(),
                                document.getString("writer"),
                                document.getString("postDate")
                            )
                        )
                    }



                    recyclerView.layoutManager = linearLayout
                    recyclerView.adapter = PostAdapter(this,postList)
                }
                .addOnFailureListener { exception ->
                    //Log.w(TAG, "Error getting documents: ", exception)
                }


        }
        findViewById<FloatingActionButton>(R.id.uploadPostButton).setOnClickListener{
            uploadPost()
        }
        findViewById<Button>(R.id.logoutButton).setOnClickListener{
            logout()
        }
    }
    private fun uploadPost()
    {
        val intent = Intent(this, UploadPostActivity::class.java)
        startActivity(intent)
    }
    private fun logout(){
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }



}
 */