package br.com.alaksion.network.client.di

import android.content.Context
import android.content.SharedPreferences
import br.com.alaksion.network.client.NetworkClientConfig.SHARED_PREFS_KEY
import br.com.alaksion.network.client.data.datasource.NetworkClientLocalDataSource
import br.com.alaksion.network.client.data.local.NetworkClientLocalDataSourceImpl
import br.com.alaksion.network.client.data.repository.NetworkClientRepositoryImpl
import br.com.alaksion.network.client.domain.repository.NetworkClientRepository
import br.com.alaksion.network.client.domain.usecase.ClearAuthTokenUseCase
import br.com.alaksion.network.client.domain.usecase.GetAuthorizationHeaderUseCase
import br.com.alaksion.network.client.domain.usecase.StoreAuthTokenUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkDIModule {

    @Singleton
    @Provides
    fun provideSharedPrefs(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(SHARED_PREFS_KEY, Context.MODE_PRIVATE)
    }

    @Singleton
    @Provides
    fun provideNetworkClientDataSource(sharedPreferences: SharedPreferences): NetworkClientLocalDataSource {
        return NetworkClientLocalDataSourceImpl(sharedPreferences)

    }

    @Singleton
    @Provides
    fun provideNetworkClientRepository(dataSource: NetworkClientLocalDataSource): NetworkClientRepository {
        return NetworkClientRepositoryImpl(dataSource)
    }

    @Singleton
    @Provides
    fun provideClearAuthTokenUseCase(repository: NetworkClientRepository): ClearAuthTokenUseCase {
        return ClearAuthTokenUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideStoreAuthTokenUseCase(repository: NetworkClientRepository): StoreAuthTokenUseCase {
        return StoreAuthTokenUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetAuthorizationHeaderUseCase(sharedPreferences: SharedPreferences): GetAuthorizationHeaderUseCase {
        return GetAuthorizationHeaderUseCase(sharedPreferences)
    }

}