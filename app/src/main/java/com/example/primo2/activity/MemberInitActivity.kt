package com.example.primo2.activity

import android.Manifest
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.Layout
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.primo2.MemberInfo
import com.example.primo2.R
import com.example.primo2.fragment.Camera2BasicFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.FileInputStream
import kotlin.system.exitProcess

class MemberInitActivity : AppCompatActivity() {
    private val user = Firebase.auth.currentUser
    private var profilePath:String? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var profileImageView: ImageView
    private lateinit var loaderLayout: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize Firebase Auth
        setContentView(R.layout.activity_member_init)
        profileImageView = findViewById(R.id.profileimageView)
        loaderLayout = findViewById<View>(R.id.loader_layout)
        auth = Firebase.auth


        findViewById<Button>(R.id.checkButton).setOnClickListener{
            profileUpdate()
        }

        findViewById<Button>(R.id.picture).setOnClickListener{
            takePicture()
        }
        findViewById<Button>(R.id.gallery).setOnClickListener{
            showGallery()
        }
        findViewById<ImageView>(R.id.profileimageView).setOnClickListener{
            selectWays()
        }

        findViewById<View>(R.id.backLayout).setOnClickListener{
            hideButton()
        }

    }
    private var activityResultLauncher : ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode == RESULT_OK){
            val intent = it.data
            profilePath = intent!!.getStringExtra("profilePath")
            Glide.with(this).load(profilePath).centerCrop().override(500).into(profileImageView);
        }
    }

    private fun hideButton(){
        val backLayout:View = findViewById<View>(R.id.backLayout)
        if(backLayout.visibility == View.VISIBLE)
        {
            backLayout.visibility = View.GONE
        }
    }
    private fun showGallery(){
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                val intent = Intent(this, GalleryActivity::class.java)
                activityResultLauncher.launch(intent)
            }
            shouldShowRequestPermissionRationale(READ_EXTERNAL_STORAGE) -> {
                requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),1)
            }
            else -> {
                requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),1)
            }
        }
    }
    private fun selectWays(){
        val backLayout:View = findViewById<View>(R.id.backLayout)
        if(backLayout.visibility == View.VISIBLE)
        {
            backLayout.visibility = View.GONE
        }
        else{
            backLayout.visibility = View.VISIBLE
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    val intent = Intent(this, GalleryActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(
                        baseContext, "갤러리 접근 권한을 허용해주셔야 합니다.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }
    private fun profileUpdate(){
        val name:String = findViewById<EditText>(R.id.nameEditText).text.toString()
        val birthDay:String = findViewById<EditText>(R.id.birthDayEditText).text.toString()
        val phoneNumber:String = findViewById<EditText>(R.id.phoneNumberEditText).text.toString()
        val address:String = findViewById<EditText>(R.id.addressEditText).text.toString()
        if(name.isNotEmpty() && phoneNumber.length > 9 && birthDay.length == 8 && address.length > 5)
        {

            val storage = FirebaseStorage.getInstance()
            val storageRef = storage.reference

            val mountainImagesRef: StorageReference =
                storageRef.child("users/" + user!!.uid + "/profileImage.jpg")
56
            loaderLayout.visibility = View.VISIBLE

            if(profilePath == null)
            {
                val memberInfo =
                    MemberInfo(name, birthDay, phoneNumber, address)

                uploader(memberInfo)
            }
            else {
                val stream = FileInputStream(profilePath)
                val uploadTask = mountainImagesRef.putStream(stream)
                uploadTask.continueWithTask { task ->
                    if (!task.isSuccessful) {
                        throw task.exception!!
                    }

                    // Continue with the task to get the download URL
                    mountainImagesRef.downloadUrl
                }.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val downloadUri = task.result

                        val memberInfo =
                            MemberInfo(name, birthDay, phoneNumber, address, downloadUri.toString())

                        uploader(memberInfo)

                    } else {
                        Log.e("실패", "실패")
                    }
                }
            }
        }
        else{
            Toast.makeText(
                baseContext, "입력 정보를 확인하세요.",
                Toast.LENGTH_SHORT
            ).show()
        }

    }
    private fun uploader(memberInfo: MemberInfo){
        val db = Firebase.firestore
        db.collection("users").document(user!!.uid).set(memberInfo)
            .addOnSuccessListener {
                Toast.makeText(
                    baseContext, "회원정보가 등록됐습니다.",
                    Toast.LENGTH_SHORT
                ).show()

                loaderLayout.visibility = View.GONE


                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }
            .addOnFailureListener {
                Toast.makeText(
                    baseContext, "회원정보 등록에 실패했습니다.",
                    Toast.LENGTH_SHORT
                ).show()

                loaderLayout.visibility = View.GONE

            }
    }
    private fun takePicture() {
        val intent = Intent(this, CameraActivity::class.java)
        activityResultLauncher.launch(intent)
    }
    override fun onBackPressed(){
        super.onBackPressed()
        moveTaskToBack(true)
        android.os.Process.killProcess(android.os.Process.myPid())
        exitProcess(1)
    }
    companion object {
        private const val TAG = "EmailPassword"
    }
}

