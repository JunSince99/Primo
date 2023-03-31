package com.example.primo2.screen

import android.graphics.Bitmap
import android.graphics.Color
import android.provider.MediaStore
import android.provider.MediaStore.Images
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.firebase.ml.modeldownloader.CustomModel
import com.google.firebase.ml.modeldownloader.CustomModelDownloadConditions
import com.google.firebase.ml.modeldownloader.DownloadType
import com.google.firebase.ml.modeldownloader.FirebaseModelDownloader
import org.tensorflow.lite.Interpreter
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.ByteBuffer
import java.nio.ByteOrder


@Composable
fun TestDeep(
    modifier: Modifier = Modifier
){

    val conditions = CustomModelDownloadConditions.Builder()
        .requireWifi()
        .build()

    val context = LocalContext.current
    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) { uriList ->
            val bitmap = Images.Media.getBitmap(context.contentResolver,uriList[0])
            val bitmap2 = Bitmap.createScaledBitmap(bitmap, 150, 150, true)
            val input = ByteBuffer.allocateDirect(150*150*3*4).order(ByteOrder.nativeOrder())
            for (y in 0 until 150) {
                for (x in 0 until 150) {
                    val px = bitmap2.getPixel(x, y)

                    // Get channel values from the pixel value.
                    val r = Color.red(px)
                    val g = Color.green(px)
                    val b = Color.blue(px)

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
                    var interpreter:Interpreter? = null
                    if (modelFile != null) {
                        interpreter = Interpreter(modelFile)
                    }
                    val bufferSize = 1000 * java.lang.Float.SIZE / java.lang.Byte.SIZE
                    val modelOutput = ByteBuffer.allocateDirect(bufferSize).order(ByteOrder.nativeOrder())
                    interpreter?.run(input,modelOutput)
                    modelOutput.rewind()
                    Log.e("","광고 : "+modelOutput.float +" 배경 : "+modelOutput.float + " 사람 : " + modelOutput.float)
                }
                .addOnFailureListener{
                    Log.e("실패",""+it)
                }
        }

    Button(
        onClick = { galleryLauncher.launch("image/*") },
        modifier = Modifier
            .wrapContentSize()
            .padding(10.dp)
    ) {
        Text(text = "Pick Image From Gallery")
    }

}
