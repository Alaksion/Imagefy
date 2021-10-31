package br.com.alaksion.myapplication.common.ui.providers

import androidx.compose.runtime.*

@Composable
fun LocalBottomNavProvider(
    content: @Composable () -> Unit
) {
    val showBottomSheet = remember { mutableStateOf(true) }

    CompositionLocalProvider(LocalBottomSheetVisibility provides showBottomSheet) {
        content()
    }

}

val LocalBottomSheetVisibility = compositionLocalOf<MutableState<Boolean>> {
    noValueProvided()
}

private fun noValueProvided(): Nothing {
    error("LocalBottomSheet visibility must be initialized with a default value")
}