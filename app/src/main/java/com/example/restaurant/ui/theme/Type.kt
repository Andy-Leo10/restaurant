package com.example.restaurant.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.restaurant.R

val CustomFontSpringlake = FontFamily(
    Font(R.font.springlake, FontWeight.Normal)
)

val Typography = Typography(
    // for text
    bodyLarge = TextStyle(
        fontFamily = CustomFontSpringlake, // FontFamily.Default
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
        lineHeight = 30.sp,
        letterSpacing = 0.sp
    ),
    // for titles
    titleLarge = TextStyle(
        fontFamily = CustomFontSpringlake, // FontFamily.Default
        fontWeight = FontWeight.Bold,
        fontSize = 30.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp
    ),
    // for buttons
    displayMedium = TextStyle(
        fontFamily = CustomFontSpringlake, // FontFamily.Default
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
)
