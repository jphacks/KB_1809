package studio.aquatan.plannap.data.source

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import studio.aquatan.plannap.data.api.PlanService
import studio.aquatan.plannap.data.model.Plan

class PlanDataSourceFactory(
    private val service: PlanService,
    private val keyword: String? = null
) : DataSource.Factory<String, Plan>() {

    val sourceLiveData = MutableLiveData<PlanDataSource>()

    override fun create(): DataSource<String, Plan> {
        val source = PlanDataSource(service, keyword)
        sourceLiveData.postValue(source)
        return source
    }
}