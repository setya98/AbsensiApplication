package com.pertamina.absensiapplication.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel

abstract class BaseViewModel : ViewModel() {
    protected val uiScope = CoroutineScope(Dispatchers.Main)
    protected val ioScope = CoroutineScope(Dispatchers.IO)

    override fun onCleared() {
        super.onCleared()
        uiScope.coroutineContext.cancel()
        ioScope.coroutineContext.cancel()
    }
}