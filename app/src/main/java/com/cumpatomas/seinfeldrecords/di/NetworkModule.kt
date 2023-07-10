package com.cumpatomas.seinfeldrecords.di

import com.cumpatomas.seinfeldrecords.data.network.CharGesturesApi
import com.cumpatomas.seinfeldrecords.data.network.CharListApi
import com.cumpatomas.seinfeldrecords.data.network.QuestionsAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): QuestionsAPI {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .baseUrl("https://seinfeldapp-29d5f.web.app/")
            .build()
            .create(QuestionsAPI::class.java)
    }

    @Provides
    fun provideCharRetrofit(okHttpClient: OkHttpClient): CharListApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .baseUrl("https://seinfeldapp-29d5f.web.app/")
            .build()
            .create(CharListApi::class.java)
    }

    @Provides
    fun provideGesturesRetrofit(okHttpClient: OkHttpClient): CharGesturesApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .baseUrl("https://seinfeldapp-29d5f.web.app/")
            .build()
            .create(CharGesturesApi::class.java)
    }

    @Provides
    fun providesHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
    }

}