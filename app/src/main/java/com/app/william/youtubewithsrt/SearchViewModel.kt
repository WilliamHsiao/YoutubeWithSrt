package com.app.william.youtubewithsrt

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.app.william.youtubewithsrt.javabean.Item

class SearchViewModel : ViewModel() {

    private val searchModel = SearchModel()
    val selectItem = MutableLiveData<Item>()
    var searchDataSourceFactory = SearchDataSourceFactory(searchModel)

    private val queue = MutableLiveData<String>()

    private val livePagedListBuilder = LivePagedListBuilder(searchDataSourceFactory, 3)


    val list: LiveData<PagedList<Item>> = Transformations.switchMap(queue) { q ->

        searchModel.queue = q
        livePagedListBuilder.build()
    }

    fun search(q: String) {
        if (q.isNotBlank()) {
            queue.postValue(q)
        }
    }

    fun setItem(item: Item) {
        selectItem.postValue(item)
    }
}