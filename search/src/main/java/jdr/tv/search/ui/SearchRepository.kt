package jdr.tv.search.ui

import androidx.room.withTransaction
import jdr.tv.base.Dispatchers.IO
import jdr.tv.data.Request
import jdr.tv.data.Resource
import jdr.tv.data.Response
import jdr.tv.data.asSuccess
import jdr.tv.data.execute
import jdr.tv.data.mapper.toShow
import jdr.tv.data.onFailure
import jdr.tv.data.onSuccess
import jdr.tv.local.Database
import jdr.tv.local.entities.SearchItem
import jdr.tv.local.entities.Show
import jdr.tv.local.insertOrUpdate
import jdr.tv.remote.TmdbApi
import jdr.tv.remote.entities.RemoteShowList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchRepository @Inject constructor(private val database: Database, private val tmdbApi: TmdbApi) {

    companion object {
        private const val PAGE_GAP = 100L
    }

    fun search(query: String): Flow<Resource<List<Show>>> {
        return flow<Resource<List<Show>>> {
            emit(Resource.loading(database.searchItemDao().selectSearchShowList()))
            fetchSearch(query)
                .onSuccess {
                    insert(it)
                    emitAll(database.searchItemDao().selectSearchShowListFlow().asSuccess())
                }
                .onFailure { emit(Resource.failure(it)) }
        }.flowOn(IO)
    }

    private suspend fun fetchSearch(query: String): Response<List<RemoteShowList>> {
        return IntRange(1, 2).map { Request { tmdbApi.search(it, query) } }.execute()
    }

    private suspend fun insert(remoteShowList: List<RemoteShowList>) = withContext(IO) {
        val showList = ArrayList<Show>()
        val searchItemList = ArrayList<SearchItem>()

        remoteShowList.forEach {
            val page = it.page
            it.results.forEachIndexed { index, remoteShow ->
                showList.add(remoteShow.toShow())
                searchItemList.add(SearchItem(page * PAGE_GAP + index, remoteShow.id, page))
            }
        }

        database.withTransaction {
            database.searchItemDao().deleteAll()
            database.showDao().insertOrUpdate(showList)
            database.searchItemDao().insertOrUpdate(searchItemList)
        }
    }

    suspend fun deleteAll() = withContext(IO) { database.searchItemDao().deleteAll() }
}
