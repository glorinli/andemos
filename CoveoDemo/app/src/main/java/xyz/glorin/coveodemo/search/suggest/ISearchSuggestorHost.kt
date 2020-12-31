package xyz.glorin.coveodemo.search.suggest

import xyz.glorin.coveodemo.model.SearchSuggest

interface ISearchSuggestorHost {
    fun onClickSearchSuggest(suggest: SearchSuggest)
}