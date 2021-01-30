package io.benedictp.remotedata

import io.benedictp.remotedata.model.LaunchDto
import retrofit2.http.GET

interface SpaceXApiService {

	@GET("launches/upcoming")
	suspend fun getUpcomingLaunches(): List<LaunchDto>

}