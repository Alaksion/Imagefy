package br.com.alaksion.myapplication

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.After
import org.junit.Before
import org.junit.Rule

abstract class ImagefyBaseTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    open fun setUp() {
    }

    @After
    open fun tearDown() {
    }

}