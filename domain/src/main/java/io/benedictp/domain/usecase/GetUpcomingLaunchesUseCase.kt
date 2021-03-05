package io.benedictp.domain.usecase

import io.benedictp.domain.model.Launch
import io.benedictp.domain.repository.LaunchRepository
import io.benedictp.domain.util.ControlledRunner
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named
import kotlin.coroutines.CoroutineContext

private val controlledRunner = ControlledRunner<Result<ArrayList<Launch>>>()

class GetUpcomingLaunchesUseCase @Inject constructor(
	private val launchRepository: LaunchRepository,
	@Named("defaultDispatcher") private val defaultDispatcher: CoroutineContext
) {

	suspend operator fun invoke(): Result<ArrayList<Launch>> = withContext(defaultDispatcher) {
		return@withContext controlledRunner.joinPreviousOrRun {
			return@joinPreviousOrRun launchRepository.getUpcomingLaunches()
		}
	}

}