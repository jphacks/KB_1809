package studio.aquatan.plannap.di

import android.content.Context
import dagger.Module
import dagger.Provides
import studio.aquatan.plannap.Plannap
import studio.aquatan.plannap.data.AuthRepository
import studio.aquatan.plannap.data.PlanRepository
import studio.aquatan.plannap.ui.ViewModelFactory
import javax.inject.Singleton

@Module
class AppModule(
    private val app: Plannap
) {
    private val planRepository = PlanRepository(app.session)
    private val authRepository = AuthRepository(app.session)

    @Provides
    @Singleton
    fun provideContext(): Context = app

    @Provides
    @Singleton
    fun provideViewModelFactory() = ViewModelFactory(app, planRepository, authRepository)
}