package studio.aquatan.plannap.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import studio.aquatan.plannap.data.api.PlanService
import studio.aquatan.plannap.data.model.Plan

class PlanRepository {

    companion object {
        private val DUMMY_LIST = listOf(
            Plan(0, "PlanA"),
            Plan(1, "PlanB"),
            Plan(2, "PlanC"),
            Plan(3, "PlanD"),
            Plan(4, "PlanE"),
            Plan(5, "PlanF"),
            Plan(6, "PlanG"),
            Plan(7, "PlanH"),
            Plan(8, "PlanI"),
            Plan(9, "PlanJ")
        )
    }

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://sample.drf.aquatan.studio")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    private val service = retrofit.create(PlanService::class.java)

    fun getPlanList(): LiveData<List<Plan>> {
        val result = MutableLiveData<List<Plan>>()

        GlobalScope.launch {
            // TODO fetch plan list via API
            delay(2000)
            result.postValue(DUMMY_LIST)

//            try {
//                val response = service.plans().execute()
//                result.postValue(response.body())
//            } catch (e: Exception) {
//                Log.e(javaClass.simpleName, "Failed to fetch plans", e)
//            }
        }

        return result
    }

    fun getPlanById(id: Long): LiveData<Plan> {
        val result = MutableLiveData<Plan>()

        GlobalScope.launch {
            // TODO fetch plan via API
            delay(1000)
            result.postValue(DUMMY_LIST.find { it.id == id })
        }

        return result
    }
}