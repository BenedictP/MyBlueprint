package io.benedictp.myblueprint.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Named
import kotlin.coroutines.CoroutineContext

@Module
@InstallIn(SingletonComponent::class)
object Dispatcher {

	@Provides
	@Named("defaultDispatcher")
	fun provideDefaultDispatcher(): CoroutineContext = Dispatchers.Default

	@Provides
	@Named("ioDispatcher")
	fun provideIoDispatcher(): CoroutineContext = Dispatchers.IO

}