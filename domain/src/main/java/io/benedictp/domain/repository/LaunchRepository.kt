package io.benedictp.domain.repository

import io.benedictp.domain.model.Launch
import io.benedictp.domain.util.Result

interface LaunchRepository {

	suspend fun getUpcomingLaunches(): Result<ArrayList<Launch>>

}