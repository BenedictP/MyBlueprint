package io.benedictp.repository.remote

import io.benedictp.domain.model.Launch

interface LaunchRemoteDataSource {

	suspend fun getUpcomingLaunches(): Result<List<Launch>>

}