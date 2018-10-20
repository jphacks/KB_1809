package studio.aquatan.plannap.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import studio.aquatan.plannap.ui.favorite.FavoriteFragment
import studio.aquatan.plannap.ui.home.HomeFragment
import studio.aquatan.plannap.ui.plan.list.PlanListFragment
import studio.aquatan.plannap.ui.plan.search.PlanSearchFragment

@Module
abstract class FragmentModule {
    @ContributesAndroidInjector
    abstract fun contributeHomeFragment(): HomeFragment

    @ContributesAndroidInjector
    abstract fun contributePlanSearchFragment(): PlanSearchFragment

    @ContributesAndroidInjector
    abstract fun contributeFavoriteFragment(): FavoriteFragment

    @ContributesAndroidInjector
    abstract fun contributePlanListFragment(): PlanListFragment

}