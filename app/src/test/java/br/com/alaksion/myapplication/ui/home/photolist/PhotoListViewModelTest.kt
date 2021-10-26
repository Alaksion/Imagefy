package br.com.alaksion.myapplication.ui.home.photolist

import br.com.alaksion.myapplication.domain.usecase.GetPhotosUseCase
import br.com.alaksion.myapplication.domain.usecase.RatePhotoUseCase
import br.com.alaksion.myapplication.utils.ImagefyBaseViewModelTest
import io.mockk.mockk

class PhotoListViewModelTest : ImagefyBaseViewModelTest() {

    private lateinit var viewModel: PhotoListViewModel
    private val getPhotosUseCase: GetPhotosUseCase = mockk(relaxed = true)
    private val ratePhotosUseCase: RatePhotoUseCase = mockk(relaxed = true)


    override fun setUp() {
        viewModel = PhotoListViewModel(getPhotosUseCase, ratePhotosUseCase)
    }
}