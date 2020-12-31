package xyz.glorin.coveodemo.search.suggest

import xyz.glorin.coveodemo.model.SearchSuggest

interface SuggestClickListener {
    fun onSuggestClicked(item: SearchSuggest)
}