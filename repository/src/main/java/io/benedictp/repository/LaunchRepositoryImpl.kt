package io.benedictp.repository

import io.benedictp.domain.model.Launch
import io.benedictp.domain.repository.LaunchRepository
import io.benedictp.domain.util.Result
import io.benedictp.domain.util.Success
import io.benedictp.repository.remote.LaunchRemoteDataSource
import javax.inject.Inject

class LaunchRepositoryImpl @Inject constructor(
	private val launchRemoteDataSource: LaunchRemoteDataSource
) : LaunchRepository {

	override suspend fun getUpcomingLaunches(): Result<ArrayList<Launch>> {
		return launchRemoteDataSource.getUpcomingLaunches()
	}
}