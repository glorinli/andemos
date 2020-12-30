package xyz.glorin.coveodemo.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import xyz.glorin.coveodemo.model.Listing
import xyz.glorin.coveodemo.search.repository.SearchRepository
import xyz.glorin.coveodemo.model.SearchResult

class SearchViewModel : ViewModel() {
    private val repository = SearchRepository()
    private val keywords = MutableLiveData<String>()

    private val repoResult = keywords.map {
        repository.searchResults(it)
    }

    val searchResults = repoResult.switchMap { it.pagedList }
    val networkState = repoResult.switchMap { it.networkState }
    val refreshState = repoResult.switchMap { it.refreshState }

    fun search(keyword: String): Boolean {
        if (keywords.value != keyword) {
            keywords.postValue(keyword)
            return true
        }

        return false
    }

    fun refresh() {
        repoResult.value?.refresh?.invoke()
    }
}