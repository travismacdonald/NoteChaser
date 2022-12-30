package com.cannonballapps.notechaser.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.cannonballapps.notechaser.R

private val Rubik = FontFamily(
    Font(R.font.rubik, weight = FontWeight.Bold),
)

// todo font weight
// todo migrate to m3 typography system
val NoteChaserTypography = Typography(
    h4 = TextStyle(
        fontFamily = Rubik,
        fontSize = 3.sp
    ),
    h5 = TextStyle(
        fontFamily = Rubik,
        fontSize = 24.sp
    ),
    h6 = TextStyle(
        fontFamily = Rubik,
        fontSize = 20.sp
    ),
    subtitle1 = TextStyle(
        fontFamily = Rubik,
        fontSize = 16.sp
    ),
    subtitle2 = TextStyle(
        fontFamily = Rubik,
        fontSize = 14.sp
    ),
    body1 = TextStyle(
        fontFamily = Rubik,
        fontSize = 16.sp
    ),
    body2 = TextStyle(
        fontFamily = Rubik,
        fontSize = 14.sp
    ),
    button = TextStyle(
        fontFamily = Rubik,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = Rubik,
        fontSize = 12.sp
    ),
    overline = TextStyle(
        fontFamily = Rubik,
        fontSize = 12.sp
    )
)
