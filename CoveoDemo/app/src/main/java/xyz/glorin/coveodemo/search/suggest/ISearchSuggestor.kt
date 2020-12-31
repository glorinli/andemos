package xyz.glorin.coveodemo.search.suggest

interface ISearchSuggestor {
    fun loadSuggests(query: String)
    fun hideSuggests()
}