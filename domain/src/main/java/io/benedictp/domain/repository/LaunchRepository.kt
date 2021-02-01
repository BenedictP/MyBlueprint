package io.benedictp.domain.repository

import io.benedictp.domain.model.Launch

interface LaunchRepository {

	suspend fun getUpcomingLaunches(): Result<ArrayList<Launch>>

}