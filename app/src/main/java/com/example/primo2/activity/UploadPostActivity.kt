package com.example.primo2.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.primo2.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storageMetadata
import java.io.FileInputStream
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


class UploadPostActivity: AppCompatActivity() {
    private val user = Firebase.auth.currentUser
    private val pathList : ArrayList<String?> = arrayListOf()
    var profilePath: String? = null
    private lateinit var parent:LinearLayout
    private lateinit var loaderLayout: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_post)

        parent = findViewById(R.id.contentsLayout)

        loaderLayout = findViewById<View>(R.id.loader_layout)

        findViewById<Button>(R.id.postUploadBotton).setOnClickListener {
            postUpload()
        }
        findViewById<Button>(R.id.imageButton).setOnClickListener {
            mediaUpload("image")
        }
        findViewById<Button>(R.id.videoButton).setOnClickListener {
            mediaUpload("video")
        }
    }

    private fun mediaUpload(media:String){
        val intent = Intent(this, GalleryActivity::class.java)
        intent.putExtra("media",media)
        activityResultLauncher.launch(intent)
    }

    private fun postUpload(){
        val title:String = findViewById<EditText>(R.id.titleEditText).text.toString()
        val comments:String = findViewById<EditText>(R.id.commentsEditText).text.toString()
        if(title.isNotEmpty() && comments.isNotEmpty()) {

            val storage = FirebaseStorage.getInstance()
            val storageRef = storage.reference

            val contentsList: ArrayList<String?> = arrayListOf()
            val formatList: ArrayList<String?> = arrayListOf()


            var count:Int = 0
            var checkSuccess:Int = 0;


            val firebaseFirestore = Firebase.firestore
            val documentReference = firebaseFirestore.collection("posts").document()


            loaderLayout.visibility = View.VISIBLE

            for(i: Int in 0 until parent.childCount){
                val view: View = parent.getChildAt(i)
                if(view is EditText){
                    val text:String = view.text.toString()
                    if(text.isNotEmpty()) {
                        contentsList.add(view.text.toString())
                    }
                }
                else {
                    checkSuccess ++
                    contentsList.add(pathList[count])

                    if (isImageFile(pathList[count])) {
                        formatList.add("image")
                    } else if (isVideoFile(pathList[count])) {
                        formatList.add("video")
                    } else {
                        formatList.add("error")
                    }
                    val pathArray: Array<String> = pathList[count]!!.split("/",".").toTypedArray()
                    val mountainImagesRef: StorageReference =
                        storageRef.child("posts/" + documentReference.id + "/" + count + "."+ pathArray[pathArray.size - 1])


                    val stream = FileInputStream(pathList[count])

                    val metadata = storageMetadata {
                        setCustomMetadata("index", "" + (contentsList.size - 1))
                    }

                    val uploadTask = mountainImagesRef.putStream(stream, metadata)
                    uploadTask.addOnFailureListener {
                    }.addOnSuccessListener { taskSnapshot ->
                        val index: Int = taskSnapshot.metadata?.getCustomMetadata("index")!!.toInt()
                        mountainImagesRef.downloadUrl.addOnSuccessListener { ImageUri ->
                            contentsList[index] = ImageUri.toString()
                            checkSuccess--
                            if (checkSuccess == 0) {
                                val postInfo =
                                    PostInfo(title, contentsList, formatList,comments,user!!.uid, LocalDate.now().format(
                                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))

                                uploader(documentReference,postInfo)
                            }
                        }
                    }
                    count ++
                }
            }

            if(pathList.size == 0)
            {
                val postInfo =
                    PostInfo(title, contentsList, formatList,comments,user!!.uid, LocalDate.now().format(
                        DateTimeFormatter.ofPattern("yyyy-MM-dd")))

                uploader(documentReference,postInfo)
            }


        }
        else{
            Toast.makeText(
                baseContext, "제목 혹은 내용을 확인해주세요.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun uploader(documentReference: DocumentReference, postInfo: PostInfo){
        documentReference.set(postInfo)
            .addOnSuccessListener {
                Toast.makeText(
                baseContext, "포스트를 업로드 했습니다.",
                Toast.LENGTH_SHORT
            ).show()
                loaderLayout.visibility = View.GONE
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(
                baseContext, "포스트 업로드에 실패했습니다.",
                Toast.LENGTH_SHORT
            ).show()
                loaderLayout.visibility = View.GONE
            }
    }

    private var activityResultLauncher : ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode == RESULT_OK){
            val intent = it.data
            profilePath = intent!!.getStringExtra("profilePath")
            pathList.add(profilePath)

            val layoutParams:ViewGroup.LayoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
            val imageView:ImageView = ImageView(this)
            imageView.layoutParams = layoutParams
            parent.addView(imageView)
            Glide.with(this).load(profilePath).override(1000).into(imageView);
/*
            val editText:EditText = EditText(this)
            editText.layoutParams = layoutParams
            editText.inputType = InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE or InputType.TYPE_CLASS_TEXT
            parent.addView(editText)

 */
        }
    }
}