package com.app.william.youtubewithsrt

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.app.william.youtubewithsrt.javabean.Item
import java.util.function.BiPredicate

class SearchViewModel : ViewModel() {

    private val searchModel = SearchModel()
    val selectItem = MutableLiveData<Item>()
    var searchDataSourceFactory = SearchDataSourceFactory(searchModel)
    val showLoading = MutableLiveData<Int>().apply {
        value = View.GONE
    }

    val queue = MutableLiveData<String>()

    private val livePagedListBuilder = LivePagedListBuilder(searchDataSourceFactory, PagedList.Config.Builder().apply {
        setPageSize(3)
        setPrefetchDistance(3)
        setInitialLoadSizeHint(3)
        setEnablePlaceholders(true)

    }.build())

    val list: LiveData<PagedList<Item>> = Transformations.switchMap(queue) { q ->

        searchModel.queue = q
        livePagedListBuilder.build()
    }

    fun search(q: String) {
        if (q.isNotBlank()) {
            setSearch(true)
            queue.postValue(q)
        }
    }

    fun setItem(item: Item) {
        selectItem.postValue(item)
    }

    fun setSearch(b: Boolean) {
        showLoading.postValue(if (b) View.VISIBLE else View.GONE)
    }
}