package br.com.alaksion.myapplication.testdata

import br.com.alaksion.myapplication.data.model.currentuser.CurrentUserResponseData
import br.com.alaksion.myapplication.data.model.currentuser.mapToCurrentUserResponse

object UserNameTestData {

    val DATA_RESPONSE = CurrentUserResponseData(
        username = ""
    )

    val DOMAIN_RESPONSE = DATA_RESPONSE.mapToCurrentUserResponse()

}