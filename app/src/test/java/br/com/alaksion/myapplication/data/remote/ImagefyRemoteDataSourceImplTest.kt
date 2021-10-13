package br.com.alaksion.myapplication.data.remote

import br.com.alaksion.myapplication.ImagefyBaseTest
import br.com.alaksion.myapplication.data.datasource.ImagefyRemoteDataSource
import br.com.alaksion.myapplication.data.remote.services.UnsplashAuthService
import br.com.alaksion.myapplication.data.remote.services.UnsplashService
import br.com.alaksion.myapplication.testdata.AuthorPhotosTestData
import br.com.alaksion.myapplication.testdata.AuthorProfileTestData
import br.com.alaksion.myapplication.testdata.GetImagesTestData
import br.com.alaksion.myapplication.testdata.PhotoDetailsTestData
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import retrofit2.Response
import kotlin.test.assertNotNull

class ImagefyRemoteDataSourceImplTest : ImagefyBaseTest() {


    private val authService: UnsplashAuthService = mockk()
    private val unsplashService: UnsplashService = mockk()
    private lateinit var dataSource: ImagefyRemoteDataSource

    override fun setUp() {
        super.setUp()
        dataSource = ImagefyRemoteDataSourceImpl(unsplashService, authService)
    }

    @Test
    fun `Should get images from unsplash service`() = runBlocking {
        coEvery { unsplashService.getPhotos(any()) } returns Response.success(GetImagesTestData.GET_IMAGES_RESPONSE)

        val result = dataSource.getPhotos((1))

        assertNotNull(result)
        coVerify(exactly = 1) { unsplashService.getPhotos(1) }
        confirmVerified(unsplashService)
    }

    @Test
    fun `Should get author profile from unsplash service`() = runBlocking {
        coEvery { unsplashService.getAuthorProfile(any()) } returns Response.success(
            AuthorProfileTestData.AUTHOR_PROFILE_RESPONSE
        )

        val result = dataSource.getAuthorProfile("username")

        assertNotNull(result)
        coVerify(exactly = 1) { unsplashService.getAuthorProfile("username") }
        confirmVerified(unsplashService)
    }

    @Test
    fun `Should get author photos from unsplash service`() = runBlocking {
        coEvery { unsplashService.getAuthorPhotos(any(), any(), any()) } returns Response.success(
            AuthorPhotosTestData.AUTHOR_PHOTOS_RESPONSE
        )

        val result = dataSource.getAuthorPhotos("username", 13)

        assertNotNull(result)
        coVerify(exactly = 1) { unsplashService.getAuthorPhotos("username", 12, 13) }
        confirmVerified(unsplashService)
    }

    @Test
    fun `Should get photo details from unsplash service`() = runBlocking {
        coEvery { unsplashService.getPhotoDetails(any()) } returns Response.success(
            PhotoDetailsTestData.AUTHOR_PHOTOS_RESPONSE
        )

        val result = dataSource.getPhotoDetails("photoId")

        assertNotNull(result)
        coVerify(exactly = 1) { unsplashService.getPhotoDetails("photoId") }
        confirmVerified(unsplashService)
    }

    @Test
    fun `Should like photo in unsplash service`() = runBlocking {
        coEvery { unsplashService.likePhoto(any()) } returns Response.success(Unit)

        val result = dataSource.likePhoto("photoId")

        assertNotNull(result)
        coVerify(exactly = 1) { unsplashService.likePhoto("photoId") }
        confirmVerified(unsplashService)
    }

    @Test
    fun `Should unlike photo in unsplash service`() = runBlocking {
        coEvery { unsplashService.unlikePhoto(any()) } returns Response.success(Unit)

        val result = dataSource.unlikePhoto("photoId")

        assertNotNull(result)
        coVerify(exactly = 1) { unsplashService.unlikePhoto("photoId") }
        confirmVerified(unsplashService)
    }

}