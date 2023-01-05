package com.example.primo2.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI
import android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
import android.provider.MediaStore.Downloads.EXTERNAL_CONTENT_URI
import android.provider.MediaStore.MediaColumns
import android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.primo2.R
import com.example.primo2.adapter.GalleryAdapter


class GalleryActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                recyclerStart()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),1)
            }
            else -> {
                requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),1)
            }
        }

    }
    private fun recyclerStart(){
        val recyclerView:RecyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        val gridLayoutManager = GridLayoutManager(applicationContext,3)
        recyclerView.layoutManager = gridLayoutManager

        recyclerView.adapter = GalleryAdapter(this,getImagesPath(this))
    }
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    recyclerStart()
                } else {
                    finish()
                    Toast.makeText(
                        baseContext, "저장공간 접근 권한을 허용해주세요.",
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

    private fun getImagesPath(activity: Activity): ArrayList<String> {
        val uri: Uri
        val listOfAllImages = ArrayList<String>()
        val cursor: Cursor?
        val column_index_data: Int
        var PathOfImage: String? = null
        val projection: Array<String>
        if(intent.getStringExtra("media").equals("video")) {
            uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            projection = arrayOf(
                MediaColumns.DATA,
                MediaStore.Video.Media.BUCKET_DISPLAY_NAME)
        }else{
            uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            projection = arrayOf(
                MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME
            )
        }

        cursor = activity.contentResolver.query(
            uri, projection, null,
            null, null
        )
        column_index_data = cursor!!.getColumnIndexOrThrow(MediaColumns.DATA)
        while (cursor.moveToNext()) {
            PathOfImage = cursor.getString(column_index_data)
            listOfAllImages.add(PathOfImage)
        }
        cursor.close()
        return listOfAllImages
    }

}