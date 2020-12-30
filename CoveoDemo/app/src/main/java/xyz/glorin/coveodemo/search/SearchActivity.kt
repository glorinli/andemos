package xyz.glorin.coveodemo.search

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import xyz.glorin.coveodemo.R

class SearchActivity : AppCompatActivity() {
    private lateinit var searchViewModel: SearchViewModel
    private lateinit var etKeyword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_search)

        searchViewModel = ViewModelProvider(this).get(SearchViewModel::class.java)

        initSearchBar()
        initSearchResults()
    }

    private fun initSearchBar() {
        findViewById<View>(R.id.btnFireSearch).setOnClickListener {
            fireSearch()
        }

        etKeyword = findViewById<EditText>(R.id.etKeyword).apply {
            setOnEditorActionListener { v, actionId, event ->
                var consumed = false
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    fireSearch()
                    consumed = true
                }

                consumed
            }
        }
    }

    private fun fireSearch() {
        etKeyword.text.toString().trim().let {
            if (!TextUtils.isEmpty(it)) {
                searchViewModel.search(it)
            }
        }
    }

    private fun initSearchResults() {
        val recyclerView = findViewById<RecyclerView>(R.id.rvSearchResults)
        val adapter = SearchResultsAdapter()
        searchViewModel.searchResults.pagedList.observe(this) {
            adapter.submitList(it)
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }
}