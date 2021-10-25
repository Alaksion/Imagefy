package br.com.alaksion.myapplication.common.extensions

import androidx.compose.runtime.MutableState

fun Boolean?.handleOptional() = this ?: false

fun Boolean.invert() = this.not()

fun MutableState<Boolean>.invert() {
    this.value = this.value.invert()
}