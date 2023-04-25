package com.example.primo2

import android.net.Uri
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

val weatherInfo = WeatherInfo()

data class WeatherInfo(
        val dateList:ArrayList<String> = arrayListOf(),
        val timeList:ArrayList<String> = arrayListOf(),
        val rainPercent:ArrayList<Int> = arrayListOf(),
        val typeList:ArrayList<Int> = arrayListOf(),
        val humidity:ArrayList<Int> = arrayListOf(),
        val skyList:ArrayList<Int> = arrayListOf(),
        val maxTmp:ArrayList<Float> = arrayListOf(),
        val minTmp:ArrayList<Float> = arrayListOf(),
        val windSpeed:ArrayList<Float> = arrayListOf()
    )