package studio.aquatan.plannap

import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import dagger.android.support.DaggerApplication
import studio.aquatan.plannap.di.ActivityModule
import studio.aquatan.plannap.di.AppModule
import studio.aquatan.plannap.di.FragmentModule
import javax.inject.Singleton

class Plannap : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerPlannap_Component.builder()
            .appModule(AppModule(this))
            .build()
    }

    @Singleton
    @dagger.Component(
        modules = [
            (AndroidSupportInjectionModule::class),
            (AppModule::class),
            (ActivityModule::class),
            (FragmentModule::class)]
    )
    interface Component : AndroidInjector<Plannap>
}