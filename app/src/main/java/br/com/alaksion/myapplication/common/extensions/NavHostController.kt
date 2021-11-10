package br.com.alaksion.myapplication.common.extensions

import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavHostController

fun NavHostController.getVmStoreOwner(): ViewModelStoreOwner {
    return this.getViewModelStoreOwner(this.graph.id)
}