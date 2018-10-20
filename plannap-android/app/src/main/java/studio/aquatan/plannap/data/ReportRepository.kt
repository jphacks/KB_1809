package studio.aquatan.plannap.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.launch
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import studio.aquatan.plannap.data.api.ReportService
import studio.aquatan.plannap.data.model.Report

class ReportRepository {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://plannap.aquatan.studio")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    private val service = retrofit.create(ReportService::class.java)

    fun getReportList(planId: Int): LiveData<List<Report>> {
        val result = MutableLiveData<List<Report>>()

        GlobalScope.launch {
            try {
                val response = service.getReports(planId).execute()
                result.postValue(response.body())
            } catch (e: Exception) {
                Log.e(javaClass.simpleName, "Failed to fetch getReports", e)
            }
        }

        return result
    }

    fun registerReport(targetReport: Report) {
        GlobalScope.launch {
            try {
                service.postReport(targetReport).execute()
            } catch (e: Exception) {
                Log.e(javaClass.simpleName, "Failed to fetch postReport", e)
            }
        }
    }
}