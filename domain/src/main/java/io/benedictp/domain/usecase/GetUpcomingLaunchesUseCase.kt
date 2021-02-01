package io.benedictp.domain.usecase

import io.benedictp.domain.model.Launch
import io.benedictp.domain.repository.LaunchRepository
import io.benedictp.domain.util.ControlledRunner
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

private val controlledRunner = ControlledRunner<Result<ArrayList<Launch>>>()

class GetUpcomingLaunchesUseCase @Inject constructor(
	private val launchRepository: LaunchRepository
) {

	suspend operator fun invoke(): Result<ArrayList<Launch>> = withContext(Dispatchers.Default) {
		return@withContext controlledRunner.joinPreviousOrRun {
			return@joinPreviousOrRun launchRepository.getUpcomingLaunches()
		}
	}

}