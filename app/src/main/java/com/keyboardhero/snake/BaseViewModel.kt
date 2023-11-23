package com.keyboardhero.snake

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

abstract class BaseViewModel<S : Any, E> : ViewModel() {
    abstract fun createInitialState(): S

    val currentState: S get() = state.value

    private val initState: S by lazy { createInitialState() }
    private val _state: MutableStateFlow<S> = MutableStateFlow(value = initState)

    val state: StateFlow<S>
        get() = _state

    fun setState(state: S) {
        _state.value = state
    }

    private val _event: MutableSharedFlow<E> = MutableSharedFlow()

    val event: SharedFlow<E>
        get() = _event

    fun setEvent(event: E) {
        _event.tryEmit(event)
    }
}