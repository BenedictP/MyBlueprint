package io.benedictp.remotedata

import io.benedictp.domain.model.Launch
import io.benedictp.domain.util.Failure
import io.benedictp.domain.util.Result
import io.benedictp.domain.util.Success
import io.benedictp.remotedata.model.LaunchDtoMapper
import io.benedictp.repository.remote.LaunchRemoteDataSource
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class LaunchRemoteDataSourceImpl @Inject constructor(
	private val spaceXApiService: SpaceXApiService,
	private val LaunchDtoMapper: LaunchDtoMapper,
	private val ioDispatcher: CoroutineContext
) : LaunchRemoteDataSource {

	override suspend fun getUpcomingLaunches(): Result<ArrayList<Launch>> = withContext(ioDispatcher) {
		return@withContext try {
			val upcomingLaunches = ArrayList(spaceXApiService.getUpcomingLaunches()
				.map { LaunchDtoMapper.mapFromDto(it) })
			Success(upcomingLaunches)
		} catch (e: Exception) {
			Failure(e)
		}
	}
}