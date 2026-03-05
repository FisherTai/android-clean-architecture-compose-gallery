package com.ftwingman.android_clean_architecture_compose_gallery.di

import com.ftwingman.android_clean_architecture_compose_gallery.BuildConfig
import com.ftwingman.android_clean_architecture_compose_gallery.data.remote.PexelsApiService
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PexelsNetworkModule {

    @Provides
    @Singleton
    @Named("pexels")
    fun providePexelsOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", BuildConfig.PEXELS_ACCESS_KEY)
                    .build()
                chain.proceed(request)
            }
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            })
            .build()
    }

    @Provides
    @Singleton
    @Named("pexels")
    fun providePexelsRetrofit(
        @Named("pexels") okHttpClient: OkHttpClient,
        moshi: Moshi
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(PexelsApiService.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Provides
    @Singleton
    fun providePexelsApiService(@Named("pexels") retrofit: Retrofit): PexelsApiService {
        return retrofit.create(PexelsApiService::class.java)
    }
}
