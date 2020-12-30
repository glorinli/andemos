package xyz.glorin.coveodemo.search

import androidx.lifecycle.ViewModel
import xyz.glorin.coveodemo.model.Listing
import xyz.glorin.coveodemo.search.repository.SearchRepository
import xyz.glorin.coveodemo.model.SearchResult

class SearchViewModel : ViewModel() {
    private val repository = SearchRepository()

    val searchResults: Listing<SearchResult>
        get() = repository.searchResults()

    fun search(keyword: String) {

    }
}