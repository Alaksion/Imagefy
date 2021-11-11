package br.com.alaksion.myapplication.di

import android.content.Context
import android.content.SharedPreferences
import br.com.alaksion.myapplication.data.datasource.ImagefyLocalDataSource
import br.com.alaksion.myapplication.data.datasource.ImagefyRemoteDataSource
import br.com.alaksion.myapplication.data.local.AppLocalConfig
import br.com.alaksion.myapplication.data.local.ImagefyLocalDataSourceImpl
import br.com.alaksion.myapplication.data.remote.AppRemoteConfig
import br.com.alaksion.myapplication.data.remote.ImagefyRemoteDataSourceImpl
import br.com.alaksion.myapplication.data.remote.services.UnsplashAuthService
import br.com.alaksion.myapplication.data.remote.services.UnsplashService
import br.com.alaksion.myapplication.data.repository.ImagefyRepositoryImpl
import br.com.alaksion.myapplication.domain.repository.ImagefyRepository
import br.com.alaksion.myapplication.domain.usecase.*
import br.com.alaksion.myapplication.ui.model.CurrentUserData
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ImagefyDi {

    /* General DI */

    @Provides
    @Singleton
    fun provideGetApiKeyUseCase(): GetApiKeyUseCase {
        return GetApiKeyUseCase()
    }

    @Provides
    @Singleton
    fun provideGetClientIdUseCase(getApiKeyUseCase: GetApiKeyUseCase): GetClientIdUseCase {
        return GetClientIdUseCase(getApiKeyUseCase)
    }

    @Provides
    @Singleton
    fun provideGetAuthorizationHeaderUseCase(sharedPreferences: SharedPreferences): GetAuthorizationHeaderUseCase {
        return GetAuthorizationHeaderUseCase(sharedPreferences)
    }

    @Provides
    @Singleton
    fun provideGetAuthUrlUseCase(getApiKeyUseCase: GetApiKeyUseCase): GetAuthUrlUseCase {
        return GetAuthUrlUseCase(getApiKeyUseCase)
    }

    @Provides
    @Singleton
    fun provideUnsplashApi(getAuthorizationHeaderUseCase: GetAuthorizationHeaderUseCase): UnsplashService {
        return UnsplashService.create(
            getAuthorizationHeaderUseCase,
            AppRemoteConfig.SERVICE_BASE_URL
        )
    }

    @Provides
    @Singleton
    fun provideUnsplashAuthApi(): UnsplashAuthService {
        return UnsplashAuthService.create(null, AppRemoteConfig.AUTH_SERVICE_BASE_URL)
    }

    @Provides
    @Singleton
    fun provideSharedPrefs(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(AppLocalConfig.SHARED_PREFS_KEY, Context.MODE_PRIVATE)
    }


    @Provides
    @Singleton
    fun provideUnsplashRemoteDataSource(
        service: UnsplashService,
        authService: UnsplashAuthService
    ): ImagefyRemoteDataSource {
        return ImagefyRemoteDataSourceImpl(service, authService)
    }

    @Provides
    @Singleton
    fun provideImagefyLocalDataSource(
        sharedPreferences: SharedPreferences,
        @ApplicationContext context: Context
    ): ImagefyLocalDataSource {
        return ImagefyLocalDataSourceImpl(sharedPreferences, context)
    }

    @Provides
    @Singleton
    fun provideUnsplashRepository(
        remoteDataSource: ImagefyRemoteDataSource,
        localDataSource: ImagefyLocalDataSource
    ): ImagefyRepository {
        return ImagefyRepositoryImpl(remoteDataSource, localDataSource)
    }

    @Provides
    @Singleton
    fun provideCurrentUserData(): CurrentUserData {
        return CurrentUserData()
    }

    /* Use Cases */
    @Provides
    @Singleton
    fun provideGetPhotosUseCase(repository: ImagefyRepository): GetPhotosUseCase {
        return GetPhotosUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetUserProfileUseCase(repository: ImagefyRepository): GetAuthorProfileUseCase {
        return GetAuthorProfileUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetAuthorPhotosUseCase(repository: ImagefyRepository): GetAuthorPhotosUseCase {
        return GetAuthorPhotosUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetPhotoDetails(repository: ImagefyRepository): GetPhotoDetailsUseCase {
        return GetPhotoDetailsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideValidateLoginUseCase(
        repository: ImagefyRepository,
        getApiKeyUseCase: GetApiKeyUseCase,
        getApiSecretKeyUseCase: GetApiSecretKeyUseCase
    ): ValidateLoginUseCase {
        return ValidateLoginUseCase(repository, getApiKeyUseCase, getApiSecretKeyUseCase)
    }

    @Provides
    @Singleton
    fun provideRatePhotoUseCase(repository: ImagefyRepository): RatePhotoUseCase {
        return RatePhotoUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetCurrentUsernameUseCasse(repository: ImagefyRepository): GetCurrentUsernameUseCase {
        return GetCurrentUsernameUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetCurrentDarkModeConfig(repository: ImagefyRepository): GetCurrentDarkModeConfigUseCase {
        return GetCurrentDarkModeConfigUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideStoreDarkModeConfig(repository: ImagefyRepository): StoreDarkModeConfigUseCase {
        return StoreDarkModeConfigUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetStoredUserUseCase(repository: ImagefyRepository): GetStoredUserDataUseCase {
        return GetStoredUserDataUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetStoreUserDataUseCase(repository: ImagefyRepository): StoreUserDataUseCase {
        return StoreUserDataUseCase(repository)
    }

}