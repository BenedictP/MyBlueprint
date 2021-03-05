package io.benedictp.myblueprint.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.benedictp.remotedata.LaunchRemoteDataSourceImpl
import io.benedictp.remotedata.SpaceXApiService
import io.benedictp.remotedata.model.LaunchDtoMapper
import io.benedictp.repository.remote.LaunchRemoteDataSource
import javax.inject.Named
import kotlin.coroutines.CoroutineContext

@Module
@InstallIn(SingletonComponent::class)
object RemoteDataSourceModule {

	@Provides
	fun provideLaunchRemoteDataSource(
		spaceXApiService: SpaceXApiService,
		@Named("ioDispatcher") coroutineContext: CoroutineContext
	): LaunchRemoteDataSource {
		return LaunchRemoteDataSourceImpl(spaceXApiService, LaunchDtoMapper, coroutineContext)
	}

}