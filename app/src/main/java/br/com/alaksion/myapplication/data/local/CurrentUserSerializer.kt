package br.com.alaksion.myapplication.data.local

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import br.com.alaksion.myapplication.CurrentUser
import java.io.InputStream
import java.io.OutputStream

object CurrentUserSerializer : Serializer<CurrentUser> {

    override val defaultValue: CurrentUser
        get() = CurrentUser.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): CurrentUser {
        try {
            return CurrentUser.parseFrom(input)
        } catch (e: Exception) {
            throw CorruptionException("Cannot read proto CurrentUser", e)
        }
    }

    override suspend fun writeTo(t: CurrentUser, output: OutputStream) {
        t.writeTo(output)
    }

}

val Context.currentUserDataStore: DataStore<CurrentUser> by dataStore(
    fileName = "CurrentUser.pb",
    serializer = CurrentUserSerializer
)