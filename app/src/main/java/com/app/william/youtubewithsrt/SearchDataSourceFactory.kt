package com.app.william.youtubewithsrt

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.app.william.youtubewithsrt.javabean.Item

class SearchDataSourceFactory(searchModel : SearchModel) : DataSource.Factory<String, Item>() {


    private val source = SearchDataSource(searchModel)
    private val sourceLiveData = MutableLiveData<SearchDataSource>()

    override fun create(): DataSource<String, Item> {
        sourceLiveData.postValue(source)
        return source
    }
}