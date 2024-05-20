package com.example.tipjar.ui

import kotlinx.serialization.Serializable


@Serializable
sealed interface TipJarScreen {
    @Serializable
    data object Home : TipJarScreen

    @Serializable
    data object Camera : TipJarScreen {
        const val KEY_IMAGE_PATH = "imagePath"
    }

    @Serializable
    data object History : TipJarScreen
}
