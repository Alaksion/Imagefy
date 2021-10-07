package br.com.alaksion.myapplication.common.extensions

fun Int?.handleOptional() = this ?: 0

fun Int.formatNumber(): String {
    if (this < 10000) {
        return this.toString()
    }

    val getVisibleValue = this / 1000
    return getVisibleValue.toString() + "K"
}
