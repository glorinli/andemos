package xyz.glorin.coveodemo.search.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import xyz.glorin.coveodemo.model.SearchResult

class SearchDataSourceFactory(private val keyword: String) : DataSource.Factory<Int, SearchResult>() {
    val sourceLiveData = MutableLiveData<SearchDataSource>()
    override fun create(): DataSource<Int, SearchResult> {
        return SearchDataSource(keyword).also {
            sourceLiveData.postValue(it)
        }
    }
}