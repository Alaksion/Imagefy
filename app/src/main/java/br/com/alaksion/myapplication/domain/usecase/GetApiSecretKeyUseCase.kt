package br.com.alaksion.myapplication.domain.usecase

import javax.inject.Inject

class GetApiSecretKeyUseCase @Inject constructor() {

    operator fun invoke(): String {
        return "X83o_oQoqXpFejgz2u0VE94485bt5flNBf6C_nIB8lQ"
    }

}