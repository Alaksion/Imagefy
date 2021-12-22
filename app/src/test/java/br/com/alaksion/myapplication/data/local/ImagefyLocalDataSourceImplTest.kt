package br.com.alaksion.myapplication.data.local

import android.content.Context
import android.content.SharedPreferences
import br.com.alaksion.myapplication.data.datasource.ImagefyLocalDataSource
import br.com.alaksion.myapplication.utils.ImagefyBaseTest
import io.mockk.mockk

class ImagefyLocalDataSourceImplTest : ImagefyBaseTest() {

    private lateinit var localDataSource: ImagefyLocalDataSource
    private val sharedPreferences: SharedPreferences = mockk(relaxed = true)
    private val context: Context = mockk(relaxed = true)


}