package studio.aquatan.plannap.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import studio.aquatan.plannap.ui.plan.list.PlanListFragment

@Module
abstract class FragmentModule {
    @ContributesAndroidInjector
    abstract fun contributePlanListFragment(): PlanListFragment
}