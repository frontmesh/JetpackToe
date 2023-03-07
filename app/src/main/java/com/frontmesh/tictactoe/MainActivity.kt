package com.frontmesh.tictactoe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.frontmesh.tictactoe.components.Board
import com.frontmesh.tictactoe.ui.theme.TicTacToeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TicTacToeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    TicTacToeScreen()
                }
            }
        }
    }
}

fun isWinner(board: List<Int>, player: Int): Boolean {
    // check if a player has won
    val winningPositions = listOf(
        // rows
        listOf(0, 1, 2), listOf(3, 4, 5), listOf(6, 7, 8),
        // columns
        listOf(0, 3, 6), listOf(1, 4, 7), listOf(2, 5, 8),
        // diagonals
        listOf(0, 4, 8), listOf(2, 4, 6)
    )

    return winningPositions.any { positions ->
        positions.all { board[it] == player }
    }
}


enum class Player(val value: Int) {
    X(1),
    O(2)
}

enum class Winner(val value: Int) {
    X(Player.X.value),
    O(Player.O.value),
    DRAW(0),
    NONE(-1)
}

// Check if there is a winner or a draw
fun isGameOver(board: List<Int>): Winner =
    when {
        isWinner(board, Player.X.value) -> Winner.X
        isWinner(board, Player.O.value) -> Winner.O
        board.none { it == 0 } -> Winner.DRAW
        else -> Winner.NONE
    }

fun suggestMove(board: List<Int>, player: Int): Int {
    // Check if there's a winning move for the AI
    for (i in board.indices) {
        if (board[i] == 0) {
            val newBoard = board.toMutableList()
            newBoard[i] = player
            if (isWinner(newBoard, player)) {
                return i
            }
        }
    }

    // Check if there's a winning move for the opponent
    val opponent = if (player == 1) 2 else 1
    for (i in board.indices) {
        if (board[i] == 0) {
            val newBoard = board.toMutableList()
            newBoard[i] = opponent
            if (isWinner(newBoard, opponent)) {
                return i
            }
        }
    }

    // If there's no winning move, choose a random available cell
    val availableMoves = board.indices.filter { board[it] == 0 }
    return availableMoves.random()
}


@Composable
fun ResetButton(onClick: () -> Unit) {
    Button(
        modifier = Modifier.size(120.dp, 48.dp),
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
    ) {
        Text(
            text = "Reset",
            fontSize = 16.sp,
        )
    }
}

@Composable
fun TicTacToeScreen() {
    val board = remember {
        mutableStateListOf(
            0, 0, 0,
            0, 0, 0,
            0, 0, 0
        )
    }
    var currentPlayer by remember { mutableStateOf(Player.X.value) }
    var winner by remember { mutableStateOf(Winner.NONE) }
    var winsX by remember { mutableStateOf(0) }
    var winsO by remember { mutableStateOf(0) }

    // Create UI
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(
                // use colors from the Material theme
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colors.primary,
                        MaterialTheme.colors.secondary
                    )
                )
            )
    ) {

        Row {
            Text(
                text = "Jetpack Toe",
                modifier = Modifier.padding(vertical = 16.dp),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,
                color = Color.Black
            )
        }
        Row {
            Text(
                text = "Turn: Player ${if (currentPlayer == Player.X.value) "X" else "O"}",
                modifier = Modifier.padding(vertical = 16.dp),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,
                color = Color.Black
            )
        }
        Row() {
            Text(
                text = if (winner.value != Winner.NONE.value) "Winner: ${
                    when(winner) {
                        Winner.X -> "X"
                        Winner.O -> "O"
                        else -> "Draw"
                    }
                } \uD83C\uDF88" else "",
                fontSize = 24.sp,
                color = Color.Black
            )
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .aspectRatio(1.05f)
        ) {
            Board(board = board) {
                if (winner == Winner.NONE) {
                    // if the cell is empty
                    if (board[it] == 0) {
                        board[it] = currentPlayer

                        currentPlayer = if (currentPlayer == Player.X.value) Player.O.value else Player.X.value
                        winner = isGameOver(board)

                        if (currentPlayer == Player.O.value && winner == Winner.NONE) {
                            // AI move
                            val aiMove = suggestMove(board, Player.O.value)
                            board[aiMove] = currentPlayer
                            currentPlayer = Player.X.value
                            winner = isGameOver(board)
                        }

                        if (winner != Winner.NONE) {
                            when(winner) {
                                Winner.X -> winsX++
                                Winner.O -> winsO++
                                else -> {}
                            }
                        }
                    }
                }
            }
        }
        Row {
            Text(
                text = "Wins:",
                modifier = Modifier.padding(vertical = 16.dp),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,
                color = Color.Black
            )
            Text(
                text = "PX - $winsX",
                modifier = Modifier.padding(16.dp),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,
                color = Color.Black
            )
            Text(
                text = "PO - $winsO",
                modifier = Modifier.padding(16.dp),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,
                color = Color.Black
            )
        }
        ResetButton {
            winner = Winner.NONE
            currentPlayer = Player.X.value
            board.replaceAll { List(9) { 0 }[it] }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TicTacToeTheme {
        TicTacToeScreen()
    }
}