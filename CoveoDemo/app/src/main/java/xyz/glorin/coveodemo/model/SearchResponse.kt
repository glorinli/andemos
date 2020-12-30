package xyz.glorin.coveodemo.model

data class SearchResponse(
    val totalCount: Int,
    val results: List<SearchResult>
)
