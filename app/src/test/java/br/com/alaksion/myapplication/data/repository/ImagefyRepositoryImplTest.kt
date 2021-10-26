package br.com.alaksion.myapplication.data.repository

import br.com.alaksion.myapplication.utils.ImagefyBaseTest
import br.com.alaksion.myapplication.common.network.Source
import br.com.alaksion.myapplication.data.datasource.ImagefyLocalDataSource
import br.com.alaksion.myapplication.data.datasource.ImagefyRemoteDataSource
import br.com.alaksion.myapplication.domain.repository.ImagefyRepository
import br.com.alaksion.myapplication.testdata.*
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertNotNull

class ImagefyRepositoryImplTest : ImagefyBaseTest() {

    private lateinit var repository: ImagefyRepository
    private val remoteDataSource: ImagefyRemoteDataSource = mockk()
    private val localDataSource: ImagefyLocalDataSource = mockk()

    override fun setUp() {
        repository = ImagefyRepositoryImpl(remoteDataSource, localDataSource)
    }

    @Test
    fun `Should validate login in data source layer`() = runBlocking {
        coEvery {
            remoteDataSource.validateLogin(
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns Source.Success(LoginAuthorizationTestData.DATA_RESPONSE)

        val result = repository.validateLogin("clientId", "authCode", "uri", "authCode", "granType")

        assertNotNull(result)
        coVerify(exactly = 1) {
            remoteDataSource.validateLogin(
                "clientId",
                "authCode",
                "uri",
                "authCode",
                "granType"
            )
        }
        confirmVerified(remoteDataSource)
    }

    @Test
    fun `Should get photos in data source layer`() = runBlocking {
        coEvery {
            remoteDataSource.getPhotos(any())
        } returns Source.Success(GetImagesTestData.DATA_RESPONSE)

        val result = repository.getPhotos(1)

        assertNotNull(result)
        coVerify(exactly = 1) { remoteDataSource.getPhotos(1) }
        confirmVerified(remoteDataSource)
    }

    @Test
    fun `Should get author profile in data source layer`() = runBlocking {
        coEvery {
            remoteDataSource.getAuthorProfile(any())
        } returns Source.Success(AuthorProfileTestData.DATA_RESPONSE)

        val result = repository.getAuthorProfile("profile")

        assertNotNull(result)
        coVerify(exactly = 1) { remoteDataSource.getAuthorProfile("profile") }
        confirmVerified(remoteDataSource)
    }

    @Test
    fun `Should get author photos in data source layer`() = runBlocking {
        coEvery {
            remoteDataSource.getAuthorPhotos(any(), any())
        } returns Source.Success(AuthorPhotosTestData.DATA_RESPONSE)

        val result = repository.getAuthorPhotos("profile", 1)

        assertNotNull(result)
        coVerify(exactly = 1) { remoteDataSource.getAuthorPhotos("profile", 1) }
        confirmVerified(remoteDataSource)
    }

    @Test
    fun `Should get photo details in data source layer`() = runBlocking {
        coEvery {
            remoteDataSource.getPhotoDetails(any())
        } returns Source.Success(PhotoDetailsTestData.DATA_RESPONSE)

        val result = repository.getPhotoDetails("photoId")

        assertNotNull(result)
        coVerify(exactly = 1) { remoteDataSource.getPhotoDetails("photoId") }
        confirmVerified(remoteDataSource)
    }

    @Test
    fun `Should like photo in data source layer`() = runBlocking {
        coEvery {
            remoteDataSource.likePhoto(any())
        } returns Source.Success(Unit)

        val result = repository.likePhoto("photoId")

        assertNotNull(result)
        coVerify(exactly = 1) { remoteDataSource.likePhoto("photoId") }
        confirmVerified(remoteDataSource)
    }

    @Test
    fun `Should unlike photo in data source layer`() = runBlocking {
        coEvery {
            remoteDataSource.unlikePhoto(any())
        } returns Source.Success(Unit)

        val result = repository.unlikePhoto("photoId")

        assertNotNull(result)
        coVerify(exactly = 1) { remoteDataSource.unlikePhoto("photoId") }
        confirmVerified(remoteDataSource)
    }

    @Test
    fun `Should get current username in data source layer`() = runBlocking {
        coEvery {
            remoteDataSource.getCurrentUsername()
        } returns Source.Success(UserNameTestData.DATA_RESPONSE)

        val result = repository.getCurrentUsername()

        assertNotNull(result)
        coVerify(exactly = 1) { remoteDataSource.getCurrentUsername() }
        confirmVerified(remoteDataSource)
    }

    @Test
    fun `Should store auth token in data source layer`() = runBlocking {
        coEvery {
            localDataSource.storeAuthorizationHeader(any())
        } returns Unit

        val result = repository.storeAuthorizationHeader("value")

        assertNotNull(result)
        coVerify(exactly = 1) { localDataSource.storeAuthorizationHeader("value") }
        confirmVerified(localDataSource)
    }

    @Test
    fun `Should clear auth token in data source layer`() = runBlocking {
        coEvery {
            localDataSource.clearAuthorizationHeader()
        } returns Unit

        val result = repository.clearAuthorizationHeader()

        assertNotNull(result)
        coVerify(exactly = 1) { localDataSource.clearAuthorizationHeader() }
        confirmVerified(localDataSource)
    }

    @Test
    fun `Should search photos in data source layer`() = runBlocking {
        coEvery {
            remoteDataSource.searchPhotos(any())
        } returns Source.Success(SearchPhotosTestData.DATA_RESPONSE)

        val result = repository.searchPhotos(SearchPhotosTestData.DOMAIN_REQUEST)

        assertNotNull(result)
        coVerify(exactly = 1) { remoteDataSource.searchPhotos(SearchPhotosTestData.DATA_REQUEST) }
        confirmVerified(remoteDataSource)
    }

}