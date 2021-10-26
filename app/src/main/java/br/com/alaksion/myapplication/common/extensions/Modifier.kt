package br.com.alaksion.myapplication.common.extensions

import androidx.compose.ui.Modifier

/**
 * Append @param modifier to the original modifier if the passed condition is true
 *
 * @param condition condition to be evaluated
 *
 * @param other Modifiers to be conditionally appended
 *
 * **/
fun Modifier.ifThen(condition: Boolean, other: Modifier): Modifier {
    return if (condition) this.then(other) else this
}