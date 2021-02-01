package io.benedictp.remotedata

import io.benedictp.domain.model.Launch
import io.benedictp.remotedata.model.LaunchDtoMapper
import io.benedictp.repository.remote.LaunchRemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LaunchRemoteDataSourceImpl @Inject constructor(
	private val spaceXApiService: SpaceXApiService,
	private val LaunchDtoMapper: LaunchDtoMapper
) : LaunchRemoteDataSource {

	override suspend fun getUpcomingLaunches(): Result<ArrayList<Launch>> = withContext(Dispatchers.IO) {
		return@withContext try {
			val upcomingLaunches = ArrayList(spaceXApiService.getUpcomingLaunches()
				.map { LaunchDtoMapper.mapFromDto(it) })
			Result.success(upcomingLaunches)
		} catch (e: Exception) {
			Result.failure(e)
		}
	}
}