package studio.aquatan.plannap.data.source

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import studio.aquatan.plannap.data.api.CommentService
import studio.aquatan.plannap.data.model.Comment

class CommentDataSourceFactory(
    private val service: CommentService,
    private val planId: Long
) : DataSource.Factory<String, Comment>() {

    val sourceLiveData = MutableLiveData<CommentDataSource>()

    override fun create(): DataSource<String, Comment> {
        val source = CommentDataSource(service, planId)
        sourceLiveData.postValue(source)
        return source
    }
}