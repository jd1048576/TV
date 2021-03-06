package jdr.tv.feature.shows.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jdr.tv.common.extensions.conflateIn
import jdr.tv.data.local.entities.Show
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class ShowsViewModel @Inject constructor(repository: ShowsRepository) : ViewModel() {

    private val _addedShowList: BroadcastChannel<List<Show>> = repository.selectAddedShowList()
        .conflateIn(viewModelScope)

    val addedShowList: Flow<List<Show>>
        get() = _addedShowList.asFlow()
}
