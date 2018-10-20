package studio.aquatan.plannap.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import studio.aquatan.plannap.ui.plan.list.PlanListFragment
import studio.aquatan.plannap.ui.plan.search.PlanSearchFragment

@Module
abstract class FragmentModule {
    @ContributesAndroidInjector
    abstract fun contributePlanListFragment(): PlanListFragment

    @ContributesAndroidInjector
    abstract fun contributePlanSearchFragment(): PlanSearchFragment
}