package io.benedictp.repository.remote

import io.benedictp.domain.model.Launch
import io.benedictp.domain.util.Result

interface LaunchRemoteDataSource {

	suspend fun getUpcomingLaunches(): Result<ArrayList<Launch>>

}