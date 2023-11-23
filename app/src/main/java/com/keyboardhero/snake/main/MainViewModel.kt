package com.keyboardhero.snake.main

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.keyboardhero.snake.BaseViewModel
import com.keyboardhero.snake.model.Cell
import com.keyboardhero.snake.model.Direction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class MainViewModel @Inject constructor() : BaseViewModel<MainViewState, MainViewEvents>() {
    override fun createInitialState(): MainViewState {
        return MainViewState(
            snakePositions = listOf(Cell(0, 0)),
            score = 0,
            currentDirection = Direction.RIGHT,
            foodPosition = Cell(x = 0, y = 0),
            running = false,
            gridSize = 20
        )
    }

    fun startGame() {
        viewModelScope.launch {
            setState(currentState.copy(running = true))
            createNewFood()
            while (currentState.running) {
                val nextCell = getNextCell(
                    direction = currentState.currentDirection,
                    currentPosition = currentState.snakePositions.first(),
                    gridWith = currentState.gridSize,
                    gridHeight = currentState.gridSize
                )
                if (nextCell in currentState.snakePositions) {
                    setState(currentState.copy(running = false))
                    setEvent(MainViewEvents.GameOverEvent)
                    Log.d("AAA", "End Game : ${currentState.score}")
                }
                val isAppend = nextCell == currentState.foodPosition
                moveSnake(nextCell = nextCell, append = isAppend)

                if (isAppend) {
                    createNewFood()
                    setState(currentState.copy(score = currentState.score + 1))
                }
                delay(DELAY_TIME)
            }
        }
    }

    private fun moveSnake(nextCell: Cell, append: Boolean) {
        val snakePositions = currentState.snakePositions.toMutableList()
        snakePositions.add(0, nextCell)

        if (!append) {
            snakePositions.removeLast()
        }
        setState(currentState.copy(snakePositions = snakePositions))
    }

    private fun getNextCell(
        direction: Direction,
        currentPosition: Cell,
        gridWith: Int,
        gridHeight: Int
    ): Cell {
        return when (direction) {
            Direction.UP -> {
                if (currentPosition.y > 0) {
                    Cell(currentPosition.x, currentPosition.y - 1)
                } else {
                    Cell(currentPosition.x, gridHeight)
                }
            }

            Direction.DOWN -> {
                if (currentPosition.y < gridHeight) {
                    Cell(currentPosition.x, currentPosition.y + 1)
                } else {
                    Cell(currentPosition.x, 0)
                }
            }

            Direction.LEFT -> {
                if (currentPosition.x > 0) {
                    Cell(currentPosition.x - 1, currentPosition.y)
                } else {
                    Cell(gridWith, currentPosition.y)
                }
            }

            Direction.RIGHT -> {
                if (currentPosition.x < gridWith) {
                    Cell(currentPosition.x + 1, currentPosition.y)
                } else {
                    Cell(0, currentPosition.y)
                }
            }
        }
    }

    private fun createNewFood() {
        val emptyPositions = mutableListOf<Cell>()

        for (column in 0..currentState.gridSize) {
            for (row in 0..currentState.gridSize) {
                val cell = Cell(column, row)
                if (cell !in currentState.snakePositions) {
                    emptyPositions.add(cell)
                }
            }
        }
        setState(currentState.copy(foodPosition = emptyPositions[Random.nextInt(emptyPositions.size)]))
    }

    fun rotation(direction: Direction) {
        when (direction) {
            Direction.UP -> {
                if (currentState.currentDirection != Direction.DOWN) {
                    setState(currentState.copy(currentDirection = direction))
                }
            }

            Direction.DOWN -> {
                if (currentState.currentDirection != Direction.UP) {
                    setState(currentState.copy(currentDirection = direction))
                }
            }

            Direction.LEFT -> {
                if (currentState.currentDirection != Direction.RIGHT) {
                    setState(currentState.copy(currentDirection = direction))
                }
            }

            Direction.RIGHT -> {
                if (currentState.currentDirection != Direction.LEFT) {
                    setState(currentState.copy(currentDirection = direction))
                }
            }
        }
    }

    companion object {
        const val DELAY_TIME = 500L
    }
}