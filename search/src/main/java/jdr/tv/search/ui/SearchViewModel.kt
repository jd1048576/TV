package jdr.tv.search.ui

import androidx.lifecycle.viewModelScope
import jdr.tv.viewmodel.StateViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(private val repository: SearchRepository) : StateViewModel<SearchViewState>(SearchViewState("", true)) {

    private var debounce: Job? = null

    var invalid = false
    var focus: Boolean
        get() = state.focus
        set(value) {
            state = state.copy(focus = value)
        }

    val search = repository.search(viewModelScope) { state.query }

    fun onQueryTextSubmit(query: String) {
        focus = false
        onQueryTextChange(query)
    }

    fun onQueryTextChange(query: String) {
        if (state.query != query) {
            state = state.copy(query = query)
            invalid = true
            debounce?.cancel()
            debounce = viewModelScope.launch {
                delay(275L)
                search.invalidate()
                if (query.isEmpty()) {
                    invalidate()
                }
            }
        }
    }

    fun invalidate() = viewModelScope.launch { repository.deleteAll() }
}