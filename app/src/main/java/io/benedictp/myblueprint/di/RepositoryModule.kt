package io.benedictp.myblueprint.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.benedictp.domain.repository.LaunchRepository
import io.benedictp.repository.LaunchRepositoryImpl
import io.benedictp.repository.remote.LaunchRemoteDataSource

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

	@Provides
	fun provideLaunchRepository(launchRemoteDataSource: LaunchRemoteDataSource): LaunchRepository {
		return LaunchRepositoryImpl(launchRemoteDataSource)
	}

}