package xyz.glorin.coveodemo.model

data class SearchSuggest(
    val expression: String?,
    val score: Float,
    val highlighted: String?
)
