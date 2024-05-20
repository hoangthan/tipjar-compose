package com.example.tipjar.common

import com.example.tipjar.ui.common.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.StandardTestDispatcher

class TestDispatcherProvider : DispatcherProvider {
    override val io: CoroutineDispatcher
        get() = StandardTestDispatcher()

    override val main: CoroutineDispatcher
        get() = StandardTestDispatcher()

    override val default: CoroutineDispatcher
        get() = StandardTestDispatcher()
}
