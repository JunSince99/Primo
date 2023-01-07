package com.example.primo2

import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList

data class PostInfo(
    val title: String? = null,
    val Contents: ArrayList<String?> = arrayListOf(),
    val Format: ArrayList<String?> = arrayListOf(),
    val Comments: String? = null,
    val Writer: String? = null,
    val PostDate: String? = null,
    val Like: Int? = null
)