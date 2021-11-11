package br.com.alaksion.myapplication.domain.usecase

import br.com.alaksion.myapplication.domain.model.StoredUser
import br.com.alaksion.myapplication.domain.repository.ImagefyRepository
import javax.inject.Inject

class StoreUserDataUseCase @Inject constructor(private val repository: ImagefyRepository) {

    suspend operator fun invoke(user: StoredUser) {
        repository.storeCurrentUser(user)
    }

}