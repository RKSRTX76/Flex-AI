package com.rksrtx76.flex_ai.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CircularBubblePattern() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(300.dp)) {
            val canvasWidth = size.width
            val canvasHeight = size.height
            val bubbleRadius = 30.dp.toPx()

            val bubbleColor = Color.White.copy(alpha = 0.2f)

            drawCircle(
                color = bubbleColor,
                radius = bubbleRadius,
                center = Offset(canvasWidth * 0.5f, canvasHeight * 0.2f)
            )
            drawCircle(
                color = bubbleColor,
                radius = bubbleRadius * 0.7f,
                center = Offset(canvasWidth * 0.15f, canvasHeight * 0.4f)
            )
            drawCircle(
                color = bubbleColor,
                radius = bubbleRadius * 0.6f,
                center = Offset(canvasWidth * 0.85f, canvasHeight * 0.5f)
            )
            drawCircle(
                color = bubbleColor,
                radius = bubbleRadius * 0.8f,
                center = Offset(canvasWidth * 0.2f, canvasHeight * 0.65f)
            )
            drawCircle(
                color = bubbleColor,
                radius = bubbleRadius * 0.5f,
                center = Offset(canvasWidth * 0.8f, canvasHeight * 0.7f)
            )

            // Add smaller bubbles in scattered positions
            drawCircle(
                color = bubbleColor,
                radius = bubbleRadius * 0.4f,
                center = Offset(canvasWidth * 0.3f, canvasHeight * 0.3f)
            )
            drawCircle(
                color = bubbleColor,
                radius = bubbleRadius * 0.3f,
                center = Offset(canvasWidth * 0.7f, canvasHeight * 0.35f)
            )
            drawCircle(
                color = bubbleColor,
                radius = bubbleRadius * 0.25f,
                center = Offset(canvasWidth * 0.1f, canvasHeight * 0.6f)
            )
            drawCircle(
                color = bubbleColor,
                radius = bubbleRadius * 0.2f,
                center = Offset(canvasWidth * 0.9f, canvasHeight * 0.6f)
            )
            drawCircle(
                color = bubbleColor,
                radius = bubbleRadius * 0.15f,
                center = Offset(canvasWidth * 0.7f, canvasHeight * 0.8f)
            )
            drawCircle(
                color = bubbleColor,
                radius = bubbleRadius * 0.1f,
                center = Offset(canvasWidth * 0.3f, canvasHeight * 0.75f)
            )
            drawCircle(
                color = bubbleColor,
                radius = bubbleRadius * 0.5f,
                center = Offset(canvasWidth * 0.5f, canvasHeight * 0.7f)
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 32.dp)
        ) {
            Text(
                text = "Hi, can I",
                style = TextStyle(
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                text = "help you?",
                style = TextStyle(
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(bottom = 24.dp)
            )
        }
    }
}
