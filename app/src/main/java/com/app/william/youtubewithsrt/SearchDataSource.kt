package com.app.william.youtubewithsrt

import android.util.Log
import androidx.paging.PageKeyedDataSource
import com.app.william.youtubewithsrt.javabean.Item
import io.reactivex.disposables.Disposable

class SearchDataSource(private val searchModel: SearchModel) : PageKeyedDataSource<String, Item>() {

    private var disposableInit: Disposable? = null
    private var disposableAfter: Disposable? = null
    private var disposableBefore: Disposable? = null


    override fun loadInitial(
        params: LoadInitialParams<String>,
        callback: LoadInitialCallback<String, Item>
    ) {
        disposableInit = searchModel.search()?.subscribe({
            if (it.items != null)
                callback.onResult(it.items, it.prevPageToken, it.nextPageToken)

        }, {
            Log.e("loadInitial", it.message)
        })
    }

    override fun loadAfter(
        params: LoadParams<String>,
        callback: LoadCallback<String, Item>
    ) {
        disposableAfter = searchModel.page(params.key)?.subscribe({
            if (it.items != null)
                callback.onResult(it.items, it.nextPageToken)

        }, {
            Log.e("loadInitial", it.message)
        })
    }

    override fun loadBefore(
        params: LoadParams<String>,
        callback: LoadCallback<String, Item>
    ) {
        disposableBefore?.dispose()
        disposableBefore = searchModel.page(params.key)?.subscribe({
                if (it.items != null)
                    callback.onResult(it.items, it.prevPageToken)

            }, {
                Log.e("loadInitial", it.message)
            })
    }

    override fun invalidate() {
        super.invalidate()
        disposableInit?.dispose()
        disposableAfter?.dispose()
        disposableBefore?.dispose()
    }

}