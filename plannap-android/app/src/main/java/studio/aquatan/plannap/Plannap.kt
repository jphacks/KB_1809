package studio.aquatan.plannap

import androidx.core.content.edit
import com.facebook.drawee.backends.pipeline.Fresco
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import dagger.android.support.DaggerApplication
import org.jetbrains.anko.defaultSharedPreferences
import studio.aquatan.plannap.di.ActivityModule
import studio.aquatan.plannap.di.AppModule
import studio.aquatan.plannap.di.FragmentModule
import studio.aquatan.plannap.worker.PostPlanWorker
import javax.inject.Singleton

class Plannap : DaggerApplication() {

    val component: Plannap.Component by lazy(LazyThreadSafetyMode.NONE) {
        DaggerPlannap_Component.builder()
            .appModule(AppModule(this))
            .build()
    }

    val session: Session by lazy {
        object : Session {
            override var token: String = defaultSharedPreferences.getString("token", null) ?: ""
                set(value) {
                    defaultSharedPreferences.edit { putString("token", value) }
                    field = value
                }

            override var username: String = defaultSharedPreferences.getString("username", null) ?: ""
                set(value) {
                    defaultSharedPreferences.edit { putString("username", value) }
                    field = value
                }

            override var password: String = defaultSharedPreferences.getString("password", null) ?: ""
                set(value) {
                    defaultSharedPreferences.edit { putString("password", value) }
                    field = value
                }
        }
    }

    override fun onCreate() {
        super.onCreate()
        Fresco.initialize(this)
    }

    override fun applicationInjector(): AndroidInjector<Plannap> = component

    @Singleton
    @dagger.Component(
        modules = [
            (AndroidSupportInjectionModule::class),
            (AppModule::class),
            (ActivityModule::class),
            (FragmentModule::class)]
    )
    interface Component : AndroidInjector<Plannap> {

        fun inject(planPostWorker: PostPlanWorker)
    }
}