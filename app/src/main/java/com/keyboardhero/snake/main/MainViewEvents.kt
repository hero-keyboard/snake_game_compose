package com.keyboardhero.snake.main

sealed interface MainViewEvents {
    object GameOverEvent : MainViewEvents
}