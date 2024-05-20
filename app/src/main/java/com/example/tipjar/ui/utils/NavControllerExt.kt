package com.example.tipjar.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController

@Composable
fun <T> NavController.CollectDataResult(
    key: String,
    initialValue: T? = null,
    result: (T?) -> Unit
) {
    val resultState = currentBackStackEntry
        ?.savedStateHandle
        ?.getStateFlow(key, initialValue)
        ?.collectAsStateWithLifecycle()

    LaunchedEffect(resultState) {
        resultState?.value?.let {
            result(it)
            currentBackStackEntry?.savedStateHandle?.remove<T>(key)
        }
    }
}
