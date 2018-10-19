package studio.aquatan.plannap.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import studio.aquatan.plannap.ui.main.MainActivity
import studio.aquatan.plannap.ui.plan.add.PlanAddActivity
import studio.aquatan.plannap.ui.plan.detail.PlanDetailActivity
import studio.aquatan.plannap.ui.plan.searchresult.PlanSearchResultActivity

@Module
abstract class ActivityModule {
    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun contributePlanDetailActivity(): PlanDetailActivity

    @ContributesAndroidInjector
    abstract fun contributePlanSearchResultActivity(): PlanSearchResultActivity


    @ContributesAndroidInjector
    abstract fun contributePlanAddActivity(): PlanAddActivity
}