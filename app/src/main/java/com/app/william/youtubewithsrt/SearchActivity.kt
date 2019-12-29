package com.app.william.youtubewithsrt

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.app.william.youtubewithsrt.databinding.ActivitySearchBinding


class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        val binding: ActivitySearchBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_search)

        val searchModel: SearchViewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        val searchAdapter = SearchAdapter(searchModel)

        binding.editText.requestFocus()
        binding.editText.setOnEditorActionListener { text, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchModel.search(text.text.toString())
                text.clearFocus()
            }
            false
        }
        binding.recyclerView.adapter = searchAdapter
        searchModel.list.observe(this, Observer {
            if (it != null) searchAdapter.submitList(it)
        })

        searchModel.selectItem.observe(this, Observer { item ->

            if (item != null) {

                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("videoId", item.id?.videoId)

                item.snippet?.title?.let { s ->
                    val arr = s.split(" - ")

                    intent.putExtra("artist", arr[0])
                    if (arr.size == 2) {

                        intent.putExtra("song", arr[1])
                    }

                }
                setResult(0, intent)
                finish()
            }

        })
    }
}