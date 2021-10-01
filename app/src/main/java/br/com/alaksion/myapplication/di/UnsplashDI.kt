package br.com.alaksion.myapplication.di

import br.com.alaksion.myapplication.data.datasource.UnsplashRemoteDataSource
import br.com.alaksion.myapplication.data.remote.UnsplashApi
import br.com.alaksion.myapplication.data.remote.UnsplashRemoteDataSourceImpl
import br.com.alaksion.myapplication.data.repository.UnsplashRepositoryImpl
import br.com.alaksion.myapplication.domain.repository.UnsplashRepository
import br.com.alaksion.myapplication.domain.usecase.GetApiKeyUseCase
import br.com.alaksion.myapplication.domain.usecase.GetAuthorPhotosUseCase
import br.com.alaksion.myapplication.domain.usecase.GetAuthorProfileUseCase
import br.com.alaksion.myapplication.domain.usecase.GetPhotosUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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
    fun provideUnsplashApi(getApiKeyUseCase: GetApiKeyUseCase): UnsplashApi {
        return UnsplashApi.create(getApiKeyUseCase)
    }

    @Provides
    @Singleton
    fun provideUnsplashRemoteDataSource(api: UnsplashApi): UnsplashRemoteDataSource {
        return UnsplashRemoteDataSourceImpl(api)
    }

    @Provides
    @Singleton
    fun provideUnsplashRepository(remoteDataSource: UnsplashRemoteDataSource): UnsplashRepository {
        return UnsplashRepositoryImpl(remoteDataSource)
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

}