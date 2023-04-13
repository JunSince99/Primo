package com.example.primo2

import android.net.Uri
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
val postPlaceList:ArrayList<Int> = arrayListOf()
data class PostInfo(
    val postID: String? = null,
    val title: String? = null,
    val Contents: ArrayList<String?> = arrayListOf(),
    val SplitNumber: ArrayList<Int> = arrayListOf(),
    val ImageResources:MutableList<String> = arrayListOf(),
    val isSpam: ArrayList<Float> = arrayListOf(),
    val isBackground: ArrayList<Float> = arrayListOf(),
    val isPerson: ArrayList<Float> = arrayListOf(),
    val Writer: String? = null,
    val WriterID: String? = null,
    val PostDate: String? = null,
    val Like: HashMap<String,Boolean> = HashMap()
)