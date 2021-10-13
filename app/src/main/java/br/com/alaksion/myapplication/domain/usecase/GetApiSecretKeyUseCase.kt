package br.com.alaksion.myapplication.domain.usecase

import br.com.alaksion.myapplication.BuildConfig
import javax.inject.Inject

class GetApiSecretKeyUseCase @Inject constructor() {

    operator fun invoke(): String {
        return BuildConfig.SECRET_KEY
    }

}