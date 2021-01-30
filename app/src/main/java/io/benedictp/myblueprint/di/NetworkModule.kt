package io.benedictp.myblueprint.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.benedictp.myblueprint.BuildConfig
import io.benedictp.remotedata.SpaceXApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

	@Provides
	fun provideSpaceXApiService(): SpaceXApiService {
		return Retrofit.Builder()
			.client(provideOkHttpClient())
			.baseUrl("https://api.spacexdata.com/v4/")
			.addConverterFactory(MoshiConverterFactory.create())
			.build()
			.create(SpaceXApiService::class.java)
	}

	private fun provideOkHttpClient(): OkHttpClient {
		val logLevel = getLogLevel()
		return OkHttpClient.Builder()
			.addInterceptor(logLevel)
			.connectTimeout(30, TimeUnit.SECONDS)
			.readTimeout(30, TimeUnit.SECONDS)
			.build()
	}

	private fun getLogLevel(): HttpLoggingInterceptor {
		val logging = HttpLoggingInterceptor()
		logging.level = if (BuildConfig.DEBUG) {
			HttpLoggingInterceptor.Level.BODY
		} else {
			HttpLoggingInterceptor.Level.NONE
		}
		return logging
	}

}