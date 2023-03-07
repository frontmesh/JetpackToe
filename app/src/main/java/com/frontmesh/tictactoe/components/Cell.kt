package com.frontmesh.tictactoe.components

import android.view.MotionEvent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.frontmesh.tictactoe.Player
import kotlinx.coroutines.delay

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Cell(value: Int, onClick: () -> Unit) {
    var wobble by remember {
        mutableStateOf(false)
    }
    val boxSize by animateDpAsState(
        targetValue = if (wobble) 102.dp else 96.dp,
        spring(
            Spring.DampingRatioHighBouncy,
            stiffness = Spring.StiffnessLow
        )
    )
    val boxColor by animateColorAsState(
        targetValue = if (wobble)
            MaterialTheme.colors.secondary
        else MaterialTheme.colors.primary.copy(alpha = 0.1f),
        spring(
            Spring.DampingRatioHighBouncy,
            stiffness = Spring.StiffnessLow
        )
    )

    LaunchedEffect(wobble) {
        if (wobble) {
            delay(50)
            wobble = false
        }
    }


    Box(
        modifier = Modifier
            .size(boxSize)
            .padding(2.dp)
            .border(
                width = 2.dp,
                color = Color.Black.copy(alpha = 0.5f),
                shape = RoundedCornerShape(8.dp)
            )
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        boxColor,
                        boxColor.copy(alpha = 0.15f)
                    )
                ),
                shape = RoundedCornerShape(8.dp)
            )
            .clickable(onClick = {
                wobble = true
                onClick()
            },
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            )
            .pointerInteropFilter {
                when (it.action) {
                    MotionEvent.ACTION_UP -> {
                        wobble = false
                        true
                    }
                    else -> false
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = when (value) {
                Player.X.value -> "X"
                Player.O.value -> "O"
                else -> ""
            },
            fontSize = 50.sp,
            fontWeight = FontWeight.Bold,
            color = when (value) {
                Player.X.value -> Color.DarkGray
                Player.O.value -> Color.Green
                else -> Color.Black
            }
        )
    }
}