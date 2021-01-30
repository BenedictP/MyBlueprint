package io.benedictp.domain.usecase

import io.benedictp.domain.model.Launch
import io.benedictp.domain.repository.LaunchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetUpcomingLaunchesUseCase @Inject constructor(
	private val launchRepository: LaunchRepository
) {

	suspend operator fun invoke(): Result<List<Launch>> = withContext(Dispatchers.Default) {
		return@withContext launchRepository.getUpcomingLaunches()
	}

}