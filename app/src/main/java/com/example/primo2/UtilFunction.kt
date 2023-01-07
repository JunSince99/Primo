package com.example.primo2

import java.net.URLConnection

class UtilFunction {

}

fun isImageFile(path: String?): Boolean {
    val mimeType = URLConnection.guessContentTypeFromName(path)
    return mimeType != null && mimeType.startsWith("image")
}

fun isVideoFile(path: String?): Boolean {
    val mimeType = URLConnection.guessContentTypeFromName(path)
    return mimeType != null && mimeType.startsWith("video")
}