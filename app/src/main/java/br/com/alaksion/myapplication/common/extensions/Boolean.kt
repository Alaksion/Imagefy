package br.com.alaksion.myapplication.common.extensions

import androidx.compose.runtime.MutableState
import kotlinx.coroutines.flow.MutableStateFlow

fun Boolean?.handleOptional() = this ?: false

fun Boolean.invert() = this.not()

fun MutableState<Boolean>.invert() {
    this.value = this.value.invert()
}

fun MutableStateFlow<Boolean>.invert() {
    this.value = this.value.invert()
}