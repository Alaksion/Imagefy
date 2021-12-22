package br.com.alaksion.myapplication.data.remote

import br.com.alaksion.myapplication.data.datasource.ImagefyRemoteDataSource
import br.com.alaksion.myapplication.data.remote.services.UnsplashAuthService
import br.com.alaksion.myapplication.data.remote.services.UnsplashService
import br.com.alaksion.myapplication.testdata.*
import br.com.alaksion.myapplication.utils.ImagefyBaseTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Test
import retrofit2.Response
import kotlin.test.assertNotNull

class ImagefyRemoteDataSourceImplTest : ImagefyBaseTest() {

    private val authService: UnsplashAuthService = mockk()
    private val unsplashService: UnsplashService = mockk()
    private lateinit var dataSource: ImagefyRemoteDataSource

    override fun setUp() {
        dataSource = ImagefyRemoteDataSourceImpl(unsplashService, authService)
    }

    @Test
    fun `Should validate login in unsplash service`() = runBlocking {
        coEvery {
            authService.validateLogin(
                "clientId",
                "secret",
                "uri",
                "code",
                "grant"
            )
        } returns Response.success(LoginAuthorizationTestData.DATA_RESPONSE)

        val result = dataSource.validateLogin("clientId", "secret", "uri", "code", "grant").first()

        assertNotNull(result)
        coVerify(exactly = 1) {
            authService.validateLogin(
                "clientId",
                "secret",
                "uri",
                "code",
                "grant"
            )
        }
        confirmVerified(unsplashService)
    }

    @Test
    fun `Should get images from unsplash service`() = runBlocking {
        coEvery { unsplashService.getPhotos(any()) } returns Response.success(GetImagesTestData.DATA_RESPONSE)

        val result = dataSource.getPhotos((1)).first()

        assertNotNull(result)
        coVerify(exactly = 1) { unsplashService.getPhotos(1) }
        confirmVerified(unsplashService)
    }

    @Test
    fun `Should get author profile from unsplash service`() = runBlocking {
        coEvery { unsplashService.getAuthorProfile(any()) } returns Response.success(
            AuthorProfileTestData.DATA_RESPONSE
        )

        val result = dataSource.getAuthorProfile("username").first()

        assertNotNull(result)
        coVerify(exactly = 1) { unsplashService.getAuthorProfile("username") }
        confirmVerified(unsplashService)
    }

    @Test
    fun `Should get author photos from unsplash service`() = runBlocking {
        coEvery { unsplashService.getAuthorPhotos(any(), any(), any()) } returns Response.success(
            AuthorPhotosTestData.DATA_RESPONSE
        )

        val result = dataSource.getAuthorPhotos("username", 13).first()

        assertNotNull(result)
        coVerify(exactly = 1) { unsplashService.getAuthorPhotos("username", 12, 13) }
        confirmVerified(unsplashService)
    }

    @Test
    fun `Should get photo details from unsplash service`() = runBlocking {
        coEvery { unsplashService.getPhotoDetails(any()) } returns Response.success(
            PhotoDetailsTestData.DATA_RESPONSE
        )

        val result = dataSource.getPhotoDetails("photoId").first()

        assertNotNull(result)
        coVerify(exactly = 1) { unsplashService.getPhotoDetails("photoId") }
        confirmVerified(unsplashService)
    }

    @Test
    fun `Should like photo in unsplash service`() = runBlocking {
        coEvery { unsplashService.likePhoto(any()) } returns Response.success(Unit)

        val result = dataSource.likePhoto("photoId").first()

        assertNotNull(result)
        coVerify(exactly = 1) { unsplashService.likePhoto("photoId") }
        confirmVerified(unsplashService)
    }

    @Test
    fun `Should unlike photo in unsplash service`() = runBlocking {
        coEvery { unsplashService.unlikePhoto(any()) } returns Response.success(Unit)

        val result = dataSource.unlikePhoto("photoId").first()

        assertNotNull(result)
        coVerify(exactly = 1) { unsplashService.unlikePhoto("photoId") }
        confirmVerified(unsplashService)
    }

    @Test
    fun `Should get current username from unsplash service`() = runBlocking {
        coEvery { unsplashService.getCurrentUsername() } returns Response.success(UserNameTestData.DATA_RESPONSE)

        val result = dataSource.getCurrentUsername().first()

        assertNotNull(result)
        coVerify(exactly = 1) { unsplashService.getCurrentUsername() }
        confirmVerified(unsplashService)
    }

    @Test
    fun `Should search photos in unsplash service`() = runBlocking {
        coEvery { unsplashService.searchPhotos(any(), any()) } returns Response.success(
            SearchPhotosTestData.DATA_RESPONSE
        )

        val result = dataSource.searchPhotos(SearchPhotosTestData.DATA_REQUEST).first()

        assertNotNull(result)
        coVerify(exactly = 1) {
            unsplashService.searchPhotos(
                SearchPhotosTestData.DATA_REQUEST.query,
                SearchPhotosTestData.DATA_REQUEST.page
            )
        }
        confirmVerified(unsplashService)
    }

}