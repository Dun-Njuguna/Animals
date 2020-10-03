package com.example.animals

import com.example.animals.di.ApiModule
import com.example.animals.model.AnimalApiService

class ApiModuleTests(private val mockService: AnimalApiService): ApiModule() {
    override fun provideAnimalApiService(): AnimalApiService {
        return mockService
    }
}