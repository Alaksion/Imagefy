package br.com.alaksion.myapplication.domain.usecase

import br.com.alaksion.myapplication.common.network.Source
import br.com.alaksion.myapplication.domain.model.CurrentUserResponse
import br.com.alaksion.myapplication.domain.repository.ImagefyRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCurrentUsernameUseCase @Inject constructor(private val repository: ImagefyRepository) {

    suspend operator fun invoke(): Flow<Source<CurrentUserResponse>> {
        return repository.getCurrentUsername()
    }
}