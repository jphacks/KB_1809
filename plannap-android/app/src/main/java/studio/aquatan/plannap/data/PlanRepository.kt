package studio.aquatan.plannap.data

import android.util.Log
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

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://plannap.aquatan.studio")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    private val service = retrofit.create(PlanService::class.java)


    fun getPlanList(): LiveData<List<Plan>> {
        val result = MutableLiveData<List<Plan>>()

        GlobalScope.launch {
            delay(2000)
            try {
                val response = service.getPlans().execute()
                result.postValue(response.body())
            } catch (e: Exception) {
                Log.e(javaClass.simpleName, "Failed to fetch plans", e)
            }
        }

        return result
    }

    fun getPlanById(id: Long): LiveData<Plan> {
        val result = MutableLiveData<Plan>()

        GlobalScope.launch {
            delay(1000)
            try {
                val response = service.getPlan(id).execute()
                result.postValue(response.body())
            } catch (e: Exception) {
                Log.e(javaClass.simpleName, "Failed to fetch getPlan", e)
            }
        }

        return result
    }

    fun getPlanByKeyword(keyword: String): LiveData<List<Plan>> {
        val result = MutableLiveData<List<Plan>>()

        GlobalScope.launch {
            delay(1000)
            try {
                val response = service.getPlan(keyword).execute()
                result.postValue(response.body())
            } catch (e: Exception) {
                Log.e(javaClass.simpleName, "Failed to fetch getPlan", e)
            }
        }

        return result
    }

    fun registerPlan(targetPlan: Plan){

        GlobalScope.launch {
            delay(1000)
            try {
                service.postPlan(targetPlan).execute()
            } catch (e: Exception) {
                Log.e(javaClass.simpleName, "Failed to fetch postPlan", e)
            }
        }
    }
}