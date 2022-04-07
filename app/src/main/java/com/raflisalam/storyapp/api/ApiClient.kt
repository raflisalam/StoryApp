package com.raflisalam.storyapp.api

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private const val BASE_URL = "https://story-api.dicoding.dev/v1/"

    val gson = GsonBuilder().setLenient().create()
    val instance: ApiServices by lazy {
        val retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        }
        retrofit.create(ApiServices::class.java)
    }
}