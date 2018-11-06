package studio.aquatan.plannap.di

import android.content.Context
import dagger.Module
import dagger.Provides
import studio.aquatan.plannap.Plannap
import studio.aquatan.plannap.data.AuthRepository
import studio.aquatan.plannap.data.CommentRepository
import studio.aquatan.plannap.data.FavoriteRepository
import studio.aquatan.plannap.data.PlanRepository
import studio.aquatan.plannap.data.ReportRepository
import studio.aquatan.plannap.data.UserRepository
import studio.aquatan.plannap.ui.ViewModelFactory
import javax.inject.Singleton

@Module
class AppModule(
    private val app: Plannap
) {

    private val planRepository = PlanRepository(app, app.session)
    private val favoriteRepository = FavoriteRepository(app.session)
    private val commentRepository = CommentRepository(app.session)
    private val authRepository = AuthRepository(app.session)
    private val reportRepository = ReportRepository(app, app.session)
    private val userRepository = UserRepository(app.session)

    @Provides
    @Singleton
    fun provideContext(): Context = app

    @Provides
    @Singleton
    fun providePlanRepository() = planRepository

    @Provides
    @Singleton
    fun provideViewModelFactory() = ViewModelFactory(
        app, planRepository, favoriteRepository, commentRepository,
        authRepository, reportRepository, userRepository
    )
}