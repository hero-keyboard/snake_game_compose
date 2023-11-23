package com.keyboardhero.snake.main

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.keyboardhero.snake.R
import com.keyboardhero.snake.model.Cell
import com.keyboardhero.snake.model.Direction

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(modifier: Modifier = Modifier, viewModel: MainViewModel = hiltViewModel()) {
    val viewState = viewModel.state.collectAsState().value

    LaunchedEffect(true){
        viewModel.startGame()
    }

    Scaffold(modifier = modifier) {
        Content(
            modifier = Modifier.fillMaxWidth(),
            state = viewState,
            onControlClick = { direction ->
                viewModel.rotation(direction)
            }
        )
    }
}

@Composable
fun Content(
    modifier: Modifier = Modifier,
    state: MainViewState,
    onControlClick: (Direction) -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GameBoard(
            size = state.gridSize,
            cellSize = 20.dp,
            snakePosition = state.snakePositions,
            foodPosition = state.foodPosition
        )
        Box(modifier = Modifier.height(20.dp))
        ControlView(
            onClick = onControlClick
        )
    }
}

@Composable
fun GameBoard(
    modifier: Modifier = Modifier,
    size: Int,
    cellSize: Dp,
    snakePosition: List<Cell>,
    foodPosition: Cell,
) {
    Row(modifier = modifier) {
        for (column in 0..size) {
            Column {
                for (row in 0..size) {
                    val color = when (Cell(column, row)) {
                        foodPosition -> Color.Red
                        in snakePosition -> Color.Yellow
                        else -> Color.Gray
                    }
                    Box(
                        modifier = Modifier
                            .size(cellSize)
                            .padding(2.dp)
                            .background(color = color)
                    )
                }
            }
        }
    }
}

@Composable
fun ControlView(modifier: Modifier = Modifier, onClick: ((Direction) -> Unit)? = null) {
    val modifierButton = Modifier
        .wrapContentSize()
        .background(color = Color.Yellow, shape = CircleShape)
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        ControlButton(
            modifier = modifierButton,
            resId = R.drawable.ic_arrow_up_24,
            onClick = {
                onClick?.invoke(Direction.UP)
            }
        )
        Row(modifier = Modifier.wrapContentSize()) {
            ControlButton(
                modifier = modifierButton,
                resId = R.drawable.ic_arrow_left_24,
                onClick = {
                    onClick?.invoke(Direction.LEFT)
                }
            )
            Box(modifier = Modifier.width(20.dp))
            ControlButton(
                modifier = modifierButton,
                resId = R.drawable.ic_arrow_right_24,
                onClick = {
                    onClick?.invoke(Direction.RIGHT)
                }
            )
        }
        ControlButton(
            modifier = modifierButton,
            resId = R.drawable.ic_arrow_down_24,
            onClick = {
                onClick?.invoke(Direction.DOWN)
            }
        )
    }
}

@Composable
fun ControlButton(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    resId: Int,
    contentDescription: String? = null
) {
    IconButton(modifier = modifier, onClick = { onClick?.invoke() }) {
        Icon(painter = painterResource(id = resId), contentDescription = contentDescription)
    }
}
