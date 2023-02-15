package com.example.primo2.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.primo2.R

// Set of Material typography styles to start with
val spoqasans = FontFamily(
    Font(R.font.spoqasans_bold, FontWeight.Bold, FontStyle.Normal),
    Font(R.font.spoqasans_medium, FontWeight.Medium, FontStyle.Normal),
    Font(R.font.spoqasans_regular, FontWeight.Normal, FontStyle.Normal),
    Font(R.font.spoqasans_light, FontWeight.Light, FontStyle.Normal),
    Font(R.font.spoqasans_thin, FontWeight.Thin, FontStyle.Normal),
)

val Typography = Typography(
    h1 = TextStyle(
        fontFamily = spoqasans,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp
    ),
    body1 = TextStyle(
        fontFamily = spoqasans,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    h5 = TextStyle(
        fontFamily = spoqasans,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp
    )
    /* Other default text styles to override
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
    */
)