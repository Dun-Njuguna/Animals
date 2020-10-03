package com.example.animals

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.animals.di.AppModule
import com.example.animals.di.DaggerViewModelComponent
import com.example.animals.model.Animal
import com.example.animals.model.AnimalApiService
import com.example.animals.model.ApiKey
import com.example.animals.util.SharedPreferencesHelper
import com.example.animals.viewmodel.ListViewModel
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.util.concurrent.Executor


class ListViewModelTest {
    @get: Rule
    var rule = InstantTaskExecutorRule()

    @Mock
    lateinit var animalApiService: AnimalApiService

    @Mock
    lateinit var sharedPreferencesHelper: SharedPreferencesHelper

    private val application: Application = Mockito.mock(Application::class.java)
    private val listViewModel = ListViewModel(application, true)
    private val key = "Test key"

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        DaggerViewModelComponent
            .builder()
            .appModule(AppModule(application))
            .apiModule(ApiModuleTests(animalApiService))
            .prefsModule(PrefsModuleTests(sharedPreferencesHelper))
            .build()
            .inject(listViewModel)
    }

    @Before
    fun setupRxSchedulers() {
        val immediate = object : Scheduler() {
            override fun createWorker(): Worker {
                return ExecutorScheduler.ExecutorWorker(Executor { it.run() }, true)
            }
        }
        RxJavaPlugins.setInitNewThreadSchedulerHandler { scheduler -> immediate }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { scheduler -> immediate }
    }

    @Test
    fun getAnimalsSuccess(){
        Mockito.`when`(sharedPreferencesHelper.getApiKey()).thenReturn(key)
        val animal = Animal("cow", null,null,null, null, null, null)
        val animalList = listOf(animal)

        val testSingle = Single.just(animalList)

        Mockito.`when`(animalApiService.getAnimals(key)).thenReturn(testSingle)
        listViewModel.refresh()

        Assert.assertEquals(1, listViewModel.animals.value?.size)
        Assert.assertEquals(false, listViewModel.loadError.value)
        Assert.assertEquals(false, listViewModel.loading.value)
        Assert.assertEquals(animalList[0], listViewModel.animals.value?.get(0))
    }

    @Test
    fun getAnimalsFailure(){
        Mockito.`when`(sharedPreferencesHelper.getApiKey()).thenReturn(key)
        val testSingle = Single.error<List<Animal>>(Throwable())
        val keySingle = Single.just(ApiKey("ok",key))

        Mockito.`when`(animalApiService.getAnimals(key)).thenReturn(testSingle)
        Mockito.`when`(animalApiService.getApiKey()).thenReturn(keySingle)

        listViewModel.refresh()

        Assert.assertEquals(null, listViewModel.animals.value)
        Assert.assertEquals(false, listViewModel.loading.value)
        Assert.assertEquals(true, listViewModel.loadError.value)
    }

    @Test
    fun getKeySuccess(){
        Mockito.`when`(sharedPreferencesHelper.getApiKey()).thenReturn(null)
        val keySingle = Single.just(ApiKey("ok",key))

        val animal = Animal("cow", null,null,null, null, null, null)
        val animalList = listOf(animal)
        val testSingle = Single.just(animalList)

        Mockito.`when`(animalApiService.getApiKey()).thenReturn(keySingle)
        Mockito.`when`(animalApiService.getAnimals(key)).thenReturn(testSingle)

        listViewModel.refresh()

        Assert.assertEquals(1, listViewModel.animals.value?.size)
        Assert.assertEquals(false, listViewModel.loadError.value)
        Assert.assertEquals(false, listViewModel.loading.value)
    }

    @Test
    fun getApiKeyFailure() {
        Mockito.`when`(sharedPreferencesHelper.getApiKey()).thenReturn(null)
        val keySingle = Single.just(ApiKey(null, null))

        Mockito.`when`(animalApiService.getApiKey()).thenReturn(keySingle)

        listViewModel.refresh()

        Assert.assertEquals(true,listViewModel.loadError.value)
        Assert.assertEquals(false, listViewModel.loading.value)
    }

    @Test
    fun getApiKeyErrorThrown() {
        Mockito.`when`(sharedPreferencesHelper.getApiKey()).thenReturn(null)
        val keySingle = Single.error<ApiKey>(Throwable())

        Mockito.`when`(animalApiService.getApiKey()).thenReturn(keySingle)

        listViewModel.refresh()

        Assert.assertEquals(true,listViewModel.loadError.value)
        Assert.assertEquals(false, listViewModel.loading.value)
    }

}