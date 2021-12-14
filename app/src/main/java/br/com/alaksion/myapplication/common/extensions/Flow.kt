package br.com.alaksion.myapplication.common.extensions

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun CoroutineScope.safeFlowCollect(
    lifecycleOwner: LifecycleOwner,
    collectionBlock: suspend (Scope: CoroutineScope) -> Unit
) {
    launch {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            collectionBlock(this)
        }
    }
}