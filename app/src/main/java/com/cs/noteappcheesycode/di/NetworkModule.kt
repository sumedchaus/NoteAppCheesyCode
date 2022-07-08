package com.cs.noteappcheesycode.di

import com.cs.noteappcheesycode.api.AuthInterceptor
import com.cs.noteappcheesycode.api.NoteApi
import com.cs.noteappcheesycode.api.UserApi
import com.cs.noteappcheesycode.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun providesRetrofitBuilder(): Retrofit.Builder {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constants.BASE_URL)
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(authInterceptor).build()

    }

    @Singleton
    @Provides
    fun providesUserApi(retrofitBuilder: Retrofit.Builder): UserApi {
        return retrofitBuilder.build().create(UserApi::class.java)
    }

    @Singleton
    @Provides
    fun providesNoteApi(retrofitBuilder: Retrofit.Builder, okHttpClient: OkHttpClient): NoteApi {
        return retrofitBuilder
            .client(okHttpClient)
            .build().create(NoteApi::class.java)

    }

}