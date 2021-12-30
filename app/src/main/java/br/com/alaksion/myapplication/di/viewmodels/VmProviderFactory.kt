package br.com.alaksion.myapplication.di.viewmodels

import br.com.alaksion.myapplication.ui.home.authordetails.AuthorDetailsViewModel
import br.com.alaksion.myapplication.ui.home.photoviewer.PhotoViewerViewModel
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@EntryPoint
@InstallIn(ActivityComponent::class)
interface VmProviderFactory {
    fun authorDetailsVmFactory(): AuthorDetailsViewModel.Factory
    fun photoViewerVmFactory(): PhotoViewerViewModel.Factory
}