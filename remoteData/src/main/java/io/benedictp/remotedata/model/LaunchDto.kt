package io.benedictp.remotedata.model

import com.squareup.moshi.JsonClass
import io.benedictp.domain.model.Launch

@JsonClass(generateAdapter = true)
data class LaunchDto(
	val name: String,
	val id: String
)

object LaunchDtoMapper {

	fun mapFromDto(launchDto: LaunchDto): Launch {
		return Launch(
			launchDto.name,
			launchDto.id
		)
	}

}