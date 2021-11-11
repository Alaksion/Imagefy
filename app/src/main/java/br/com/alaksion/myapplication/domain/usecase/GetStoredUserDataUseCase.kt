package br.com.alaksion.myapplication.domain.usecase

import br.com.alaksion.myapplication.domain.model.StoredUser
import br.com.alaksion.myapplication.domain.repository.ImagefyRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetStoredUserDataUseCase @Inject constructor(private val repository: ImagefyRepository) {

    suspend operator fun invoke(): Flow<StoredUser> {
        return repository.getCurrentUser()
    }

}