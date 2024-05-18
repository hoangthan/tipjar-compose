package com.example.tipjar.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.tipjar.R


private val RobotoCondensed = FontFamily(
    Font(R.font.robotocondensed_regular, FontWeight.Medium),
    Font(R.font.robotocondensed_light, FontWeight.Light),
    Font(R.font.robotocondensed_bold, FontWeight.Bold)
)

val TipJarTypography = Typography(
    defaultFontFamily = RobotoCondensed,

    body1 = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        color = Black,
    ),

    subtitle1 = TextStyle(
        fontWeight = FontWeight.Light,
        fontSize = 16.sp,
        lineHeight = 17.6.sp,
    ),
)
