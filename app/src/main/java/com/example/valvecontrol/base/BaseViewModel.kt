package com.example.valvecontrol.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.valvecontrol.ui.mutableSharedFlow
import com.example.valvecontrol.ui.value
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class BaseViewModel<Event : Any> : ViewModel(), IBaseViewModel<Event> {

    override val error = mutableSharedFlow<Throwable>()

    protected abstract suspend fun handleEvent(event: Event)

    protected fun handleError(block: (Throwable) -> Unit) =
        CoroutineExceptionHandler { _, throwable ->
            block(throwable)
        }

    protected open fun handleError(event: Event, exception: Throwable) {
        error.value = exception
    }

    override fun sendEvent(event: Event) {
        viewModelScope.launch(Dispatchers.Default) {
            try {
                handleEvent(event)
            } catch (exception: Throwable) {
                handleError(event, exception)
            }
        }
    }

    suspend fun <T> longRunning(block: suspend () -> T): T = withContext(Dispatchers.Default) {
        block()
    }

}