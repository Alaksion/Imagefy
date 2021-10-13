package br.com.alaksion.myapplication.domain.usecase

import br.com.alaksion.myapplication.BuildConfig

class GetApiKeyUseCase {

    operator fun invoke(): String {
        return BuildConfig.PUBLIC_KEY
    }

}