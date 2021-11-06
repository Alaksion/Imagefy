package br.com.alaksion.myapplication.domain.usecase

import br.com.alaksion.myapplication.domain.repository.ImagefyRepository
import javax.inject.Inject

class StoreDarkModeConfigUseCase @Inject constructor(private val repository: ImagefyRepository) {

    suspend operator fun invoke(value: Boolean) {
        repository.storeDarkModeConfig(value)
    }

}