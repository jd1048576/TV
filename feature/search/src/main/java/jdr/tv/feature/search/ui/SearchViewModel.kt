package jdr.tv.feature.search.ui

import androidx.lifecycle.viewModelScope
import jdr.tv.common.extensions.conflateIn
import jdr.tv.common.ui.Resource
import jdr.tv.common.viewmodel.StateViewModel
import jdr.tv.data.local.entities.Show
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class SearchViewModel @Inject constructor(private val repository: SearchRepository) : StateViewModel<SearchViewState>(SearchViewState()) {

    companion object {
        private const val DEBOUNCE = 275L
    }

    private val invalidate = BroadcastChannel<Unit>(Channel.CONFLATED)
    private val _search: BroadcastChannel<Resource<List<Show>>> = invalidate.asFlow()
        .debounce(DEBOUNCE)
        .flatMapLatest { repository.search(state.query) }
        .conflateIn(viewModelScope)

    var focus: Boolean
        get() = state.focus
        set(value) {
            state = state.copy(focus = value)
        }

    val search: Flow<Resource<List<Show>>>
        get() = _search.asFlow()

    fun onQueryTextSubmit(query: String) {
        focus = false
        onQueryTextChange(query)
    }

    fun onQueryTextChange(query: String) {
        val trimmedQuery = query.trim()
        if (state.query != trimmedQuery) {
            state = state.copy(query = trimmedQuery)
            if (trimmedQuery.isEmpty()) {
                invalidate()
            } else {
                invalidate.offer(Unit)
            }
        }
    }

    private fun invalidate() = viewModelScope.launch { repository.deleteAll() }
}
