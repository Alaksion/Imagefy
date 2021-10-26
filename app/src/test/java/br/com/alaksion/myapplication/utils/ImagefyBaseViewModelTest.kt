package br.com.alaksion.myapplication.utils

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule

@ExperimentalCoroutinesApi
abstract class ImagefyBaseViewModelTest : ImagefyBaseTest() {

    @get: Rule
    var mainDispatcherRule = MainDispatcherRule()

}