package com.rksrtx76.flex_ai.presentation

import androidx.compose.foundation.background
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    title: String,
    scrollBehavior: TopAppBarScrollBehavior,
    modifier : Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        scrollBehavior = scrollBehavior,
        title = {
            Text(
                text = title,
                color = Color(0xFFB1B1F2),
                style = MaterialTheme.typography.titleLarge
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            titleContentColor = Color(0xFFB1B1F2),
            navigationIconContentColor = Color(0xFFB1B1F2)
        ),
        modifier = modifier
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF4A4ABF),
                        Color(0xFF2B2B8F),
                        Color(0xFF1A1A5A),
//                        Color(0xFF2B2B8F),
//                        Color(0xFF4A4ABF),
//                        Color(0xFF2B2B8F),
//                        Color.Transparent,
//                        Color(0xFF1A1A5A),
//                        Color(0xFF2B2B8F),
//                        Color(0xFF4A4ABF),
//                        Color(0xFF2B2B8F),
//                        Color(0xFF1A1A5A)
                    ),
                    startY = 0f,
                    endY = 260f
                )
            )
    )
}


