package xyz.glorin.coveodemo.model

data class SearchResult(
    val title: String?,
    val uri: String?,
    val printableUri: String?,
    val clickUri: String?,
    val excerpt: String?,
    val uniqueId: String?
)
