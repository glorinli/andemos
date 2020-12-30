package xyz.glorin.coveodemo.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import xyz.glorin.coveodemo.model.SearchResponse

interface CoveoApi {
    @GET("rest/search/v2")
    fun getSearchResults(
        @Query("firstResult") firstResult: Int,
        @Query("numberOfResults") numberOfResults: Int,
        @Query("q") query: String
    ): Call<SearchResponse>
}