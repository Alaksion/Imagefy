package br.com.alaksion.myapplication.domain.usecase

import br.com.alaksion.myapplication.domain.repository.ImagefyRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCurrentDarkModeConfigUseCase @Inject constructor(private val repository: ImagefyRepository) {

    suspend operator fun invoke(): Flow<Boolean> {
        return repository.getCurrentDarkModeConfig()
    }

}