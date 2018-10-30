package studio.aquatan.plannap.data.source

import androidx.paging.DataSource
import studio.aquatan.plannap.data.api.PlanService
import studio.aquatan.plannap.data.model.Plan

class PlanDataSourceFactory(
    private val service: PlanService
) : DataSource.Factory<Int, Plan>() {

    override fun create(): DataSource<Int, Plan> {
        return PlanDataSource(service)
    }
}