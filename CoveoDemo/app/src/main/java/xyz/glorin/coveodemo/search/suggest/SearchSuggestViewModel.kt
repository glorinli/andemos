package xyz.glorin.coveodemo.search.suggest

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Response
import xyz.glorin.coveodemo.api.ApiManager
import xyz.glorin.coveodemo.model.SearchSuggest
import xyz.glorin.coveodemo.model.SearchSuggestResponse

class SearchSuggestViewModel : ViewModel() {
    private var query: String? = null
    private val mainHandler = Handler(Looper.getMainLooper())
    private val requestRunnable = Runnable {
        requestSearchSuggests()
    }

    private val _suggests = MutableLiveData<List<SearchSuggest>>()

    val suggest
        get() = _suggests

    private fun requestSearchSuggests() {
        ApiManager.coveoApi.getSearchSuggests(query ?: "").enqueue(object: retrofit2.Callback<SearchSuggestResponse>{
            override fun onResponse(
                call: Call<SearchSuggestResponse>,
                response: Response<SearchSuggestResponse>
            ) {
                if (response.isSuccessful) {
                    _suggests.postValue(response.body()?.completions ?: emptyList())
                }
            }

            override fun onFailure(call: Call<SearchSuggestResponse>, t: Throwable) {

            }

        })
    }

    fun loadSearchSuggests(query: String) {
        if (this.query != query) {
            this.query = query
            mainHandler.removeCallbacks(requestRunnable)
            mainHandler.postDelayed(requestRunnable, 500L)
        }
    }

    override fun onCleared() {
        super.onCleared()
        mainHandler.removeCallbacks(requestRunnable)
    }
}