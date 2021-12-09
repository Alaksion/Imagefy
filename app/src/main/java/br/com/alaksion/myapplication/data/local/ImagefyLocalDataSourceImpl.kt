package br.com.alaksion.myapplication.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import br.com.alaksion.myapplication.common.extensions.handleOptional
import br.com.alaksion.myapplication.data.datasource.ImagefyLocalDataSource
import br.com.alaksion.myapplication.data.model.storeduser.StoredUserData
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class ImagefyLocalDataSourceImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : ImagefyLocalDataSource {

    private val dataStore = context.dataStore
    val userDataStore = context.currentUserDataStore

    override suspend fun storeDarkModeConfig(value: Boolean) {
        dataStore.edit { prefs ->
            prefs[AppLocalConfig.DARK_MODE_KEY] = value
        }
    }

    override fun getCurrentDarkModeConfig(): Flow<Boolean> {
        return dataStore.data.map { prefs ->
            prefs[AppLocalConfig.DARK_MODE_KEY].handleOptional()
        }
    }

    override suspend fun storeCurrentUser(user: StoredUserData) {
        userDataStore.updateData {
            it.toBuilder()
                .setFollowersCount(user.followersCount)
                .setFollowingCount(user.followingCount)
                .setName(user.name)
                .setUserName(user.userName)
                .setProfileImageUrl(user.profileImageUrl)
                .build()
        }
    }

    override suspend fun getCurrentUser(): Flow<StoredUserData> {
        return userDataStore.data.map { storedUser ->
            StoredUserData(
                userName = storedUser.userName,
                name = storedUser.name,
                followersCount = storedUser.followersCount,
                followingCount = storedUser.followingCount,
                profileImageUrl = storedUser.profileImageUrl
            )
        }
    }

}