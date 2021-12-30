package br.com.alaksion.myapplication.di.viewmodels

import android.app.Activity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.alaksion.myapplication.ui.home.authordetails.AuthorDetailsViewModel
import br.com.alaksion.myapplication.ui.home.photoviewer.PhotoViewerViewModel
import dagger.hilt.android.EntryPointAccessors

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@ExperimentalFoundationApi
object VmFactories {

    @Composable
    fun authorDetailsViewModel(authorUsername: String): AuthorDetailsViewModel {
        val factory = EntryPointAccessors.fromActivity(
            LocalContext.current as Activity,
            VmProviderFactory::class.java
        ).authorDetailsVmFactory()

        return viewModel(factory = AuthorDetailsViewModel.provideFactory(factory, authorUsername))
    }

    @Composable
    fun photoViewerViewModel(photoId: String): PhotoViewerViewModel {
        val factory = EntryPointAccessors.fromActivity(
            LocalContext.current as Activity,
            VmProviderFactory::class.java
        ).photoViewerVmFactory()

        return viewModel(factory = PhotoViewerViewModel.provideFactory(factory, photoId))
    }

}

