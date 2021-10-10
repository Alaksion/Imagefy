package br.com.alaksion.myapplication.di

import android.content.Context
import android.content.SharedPreferences
import br.com.alaksion.myapplication.data.datasource.ImagefyLocalDataSource
import br.com.alaksion.myapplication.data.datasource.UnsplashRemoteDataSource
import br.com.alaksion.myapplication.data.local.AppLocalConfig
import br.com.alaksion.myapplication.data.local.ImagefyLocalDataSourceImpl
import br.com.alaksion.myapplication.data.remote.AppRemoteConfig
import br.com.alaksion.myapplication.data.remote.UnsplashRemoteDataSourceImpl
import br.com.alaksion.myapplication.data.remote.services.UnsplashAuthService
import br.com.alaksion.myapplication.data.remote.services.UnsplashService
import br.com.alaksion.myapplication.data.repository.UnsplashRepositoryImpl
import br.com.alaksion.myapplication.domain.repository.UnsplashRepository
import br.com.alaksion.myapplication.domain.usecase.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UnsplashDI {

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
    fun provideGetAuthUrlUseCase(getApiKeyUseCase: GetApiKeyUseCase): GetAuthUrlUseCase {
        return GetAuthUrlUseCase(getApiKeyUseCase)
    }

    @Provides
    @Singleton
    fun provideUnsplashApi(getClientIdUseCase: GetClientIdUseCase): UnsplashService {
        return UnsplashService.create(getClientIdUseCase, AppRemoteConfig.SERVICE_BASE_URL)
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
    ): UnsplashRemoteDataSource {
        return UnsplashRemoteDataSourceImpl(service, authService)
    }

    @Provides
    @Singleton
    fun provideImagefyLocalDataSource(
        sharedPreferences: SharedPreferences
    ): ImagefyLocalDataSource {
        return ImagefyLocalDataSourceImpl(sharedPreferences)
    }

    @Provides
    @Singleton
    fun provideUnsplashRepository(
        remoteDataSource: UnsplashRemoteDataSource,
        localDataSource: ImagefyLocalDataSource
    ): UnsplashRepository {
        return UnsplashRepositoryImpl(remoteDataSource, localDataSource)
    }

    /* Use Cases */
    @Provides
    @Singleton
    fun provideGetPhotosUseCase(repository: UnsplashRepository): GetPhotosUseCase {
        return GetPhotosUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetUserProfileUseCase(repository: UnsplashRepository): GetAuthorProfileUseCase {
        return GetAuthorProfileUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetAuthorPhotosUseCase(repository: UnsplashRepository): GetAuthorPhotosUseCase {
        return GetAuthorPhotosUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetPhotoDetails(repository: UnsplashRepository): GetPhotoDetailsUseCase {
        return GetPhotoDetailsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideValidateLoginUseCase(
        repository: UnsplashRepository,
        getApiKeyUseCase: GetApiKeyUseCase,
        getApiSecretKeyUseCase: GetApiSecretKeyUseCase
    ): ValidateLoginUseCase {
        return ValidateLoginUseCase(repository, getApiKeyUseCase, getApiSecretKeyUseCase)
    }

}