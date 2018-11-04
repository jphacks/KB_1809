package studio.aquatan.plannap.data.source

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import studio.aquatan.plannap.data.api.PlanService
import studio.aquatan.plannap.data.model.Plan

class MyPlanDataSourceFactory(
    private val service: PlanService
) : DataSource.Factory<String, Plan>() {

    val sourceLiveData = MutableLiveData<MyPlanDataSource>()

    override fun create(): DataSource<String, Plan> {
        val source = MyPlanDataSource(service)
        sourceLiveData.postValue(source)
        return source
    }
}