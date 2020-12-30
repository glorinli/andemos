package xyz.glorin.coveodemo.search.repository

import androidx.lifecycle.switchMap
import androidx.paging.Config
import androidx.paging.toLiveData
import xyz.glorin.coveodemo.api.ApiManager
import xyz.glorin.coveodemo.model.Listing
import xyz.glorin.coveodemo.model.SearchResult

class SearchRepository {

    fun searchResults(keyword: String): Listing<SearchResult> {
        val sourceFactory = SearchDataSourceFactory(keyword)
        val livePagedList = sourceFactory.toLiveData(
            config = Config(
                pageSize = 20,
                enablePlaceholders = false
            ),
            fetchExecutor = ApiManager.networkExecutor
        )

        val refreshState = sourceFactory.sourceLiveData.switchMap {
            it.initialLoad
        }

        return Listing(
            pagedList = livePagedList,
            networkState = sourceFactory.sourceLiveData.switchMap {
                it.networkState
            },
            retry = {
                sourceFactory.sourceLiveData.value?.retryAllFailed()
            },
            refresh = {
                sourceFactory.sourceLiveData.value?.invalidate()
            },
            refreshState = refreshState
        )
    }
}