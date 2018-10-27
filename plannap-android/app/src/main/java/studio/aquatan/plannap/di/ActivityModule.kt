package studio.aquatan.plannap.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import studio.aquatan.plannap.ui.comment.list.CommentListActivity
import studio.aquatan.plannap.ui.login.LoginActivity
import studio.aquatan.plannap.ui.main.MainActivity
import studio.aquatan.plannap.ui.plan.detail.PlanDetailActivity
import studio.aquatan.plannap.ui.plan.post.PlanPostActivity
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
    abstract fun contributePlanPostActivity(): PlanPostActivity

    @ContributesAndroidInjector
    abstract fun contributeLoginActivity(): LoginActivity

    @ContributesAndroidInjector
    abstract fun contributeCommentListActivity(): CommentListActivity
}