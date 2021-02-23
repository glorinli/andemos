package xyz.glorin.coveodemo.search.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.PositionalDataSource
import retrofit2.Call
import retrofit2.Response
import xyz.glorin.coveodemo.api.ApiManager
import xyz.glorin.coveodemo.model.NetworkState
import xyz.glorin.coveodemo.model.SearchResponse
import xyz.glorin.coveodemo.model.SearchResult

class SearchDataSource(private val keyword: String) : PositionalDataSource<SearchResult>() {
    private val coveoApi = ApiManager.coveoApi

    /**
     * There is no sync on the state because paging will always call loadInitial first then wait
     * for it to return some success value before calling loadAfter and we don't support loadBefore
     * in this example.
     * <p>
     * See BoundaryCallback example for a more complete example on syncing multiple network states.
     */
    val networkState = MutableLiveData<NetworkState>()

    val initialLoad = MutableLiveData<NetworkState>()

    override fun loadInitial(
        params: LoadInitialParams,
        callback: LoadInitialCallback<SearchResult>
    ) {
        networkState.postValue(NetworkState.LOADING)
        initialLoad.postValue(NetworkState.LOADING)

        val firstResult = params.requestedStartPosition
        val numberOfResults = params.requestedLoadSize
        val searchResults = if (keyword.isEmpty())
            coveoApi.getFaq(firstResult, numberOfResults)
        else
            coveoApi.getSearchResults(firstResult, numberOfResults, keyword)

        searchResults.enqueue(
            object : retrofit2.Callback<SearchResponse> {
                override fun onResponse(
                    call: Call<SearchResponse>,
                    response: Response<SearchResponse>
                ) {
                    if (response.isSuccessful) {
                        callback.onResult(response.body()?.results ?: emptyList(), firstResult)
                        networkState.postValue(NetworkState.LOADED)
                        initialLoad.postValue(NetworkState.LOADED)
                    } else {
                        val error = NetworkState.error("Fail to load: ${response.code()}")
                        networkState.postValue(error)
                        initialLoad.postValue(error)
                    }
                }

                override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                    val error = NetworkState.error("Fail to load: ${t.message}")
                    networkState.postValue(error)
                    initialLoad.postValue(error)
                }

            }
        )
    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<SearchResult>) {
        networkState.postValue(NetworkState.LOADING)
        val firstResult = params.startPosition
        val numberOfResults = params.loadSize
        val searchResults = if (keyword.isEmpty())
            coveoApi.getFaq(firstResult, numberOfResults)
        else
            coveoApi.getSearchResults(firstResult, numberOfResults, keyword)
        searchResults.enqueue(
            object : retrofit2.Callback<SearchResponse> {
                override fun onResponse(
                    call: Call<SearchResponse>,
                    response: Response<SearchResponse>
                ) {
                    if (response.isSuccessful) {
                        callback.onResult(response.body()?.results ?: emptyList())
                        networkState.postValue(NetworkState.LOADED)
                    } else {
                        networkState.postValue(NetworkState.error("Fail to load: ${response.code()}"))
                    }
                }

                override fun onFailure(call: Call<SearchResponse>, t: Throwable) {

                }

            }
        )
    }

    fun retryAllFailed() {
        // TODO
    }
}