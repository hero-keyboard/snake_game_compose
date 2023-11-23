package com.keyboardhero.snake.main

import com.keyboardhero.snake.model.Cell
import com.keyboardhero.snake.model.Direction

data class MainViewState(
    val snakePositions: List<Cell>,
    val score: Int,
    val currentDirection: Direction,
    val foodPosition: Cell,
    val running: Boolean,
    val gridSize:Int
)