package xyz.glorin.coveodemo.search

import android.content.Context
import android.content.Intent
import android.net.Uri
import xyz.glorin.coveodemo.model.SearchResult

object SearchHelper {
    fun openSearchResult(searchResult: SearchResult, context: Context) {
        searchResult.clickUri?.let {
            context.startActivity(Intent(Intent.ACTION_VIEW).setData(Uri.parse(it)))
        }
    }
}