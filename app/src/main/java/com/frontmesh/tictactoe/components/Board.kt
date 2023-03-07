package com.frontmesh.tictactoe.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable

@Composable
fun Board(board: List<Int>, onClick: (Int) -> Unit) {
    Column {
        for (i in 0..2) {
            Row {
                for (j in 0..2) {
                    Cell(
                        value = board[i * 3 + j],
                        onClick = { onClick(i * 3 + j) }
                    )
                }
            }
        }
    }
}