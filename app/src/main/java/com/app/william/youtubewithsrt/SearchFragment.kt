package com.app.william.youtubewithsrt

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.app.william.youtubewithsrt.databinding.LayoutSearchBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class SearchFragment : BottomSheetDialogFragment() {
    private var mBehavior: BottomSheetBehavior<View>? = null

    private lateinit var binding: LayoutSearchBinding
    private lateinit var searchModel: SearchViewModel
    private lateinit var mainViewModel: MainViewModel
    private lateinit var searchAdapter: SearchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
        mainViewModel = ViewModelProvider(activity!!).get(MainViewModel::class.java)
        searchModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        searchAdapter = SearchAdapter(searchModel)

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        val view: View = View.inflate(context, R.layout.layout_search, null)
        binding = LayoutSearchBinding.bind(view)
        binding.lifecycleOwner = this
        binding.model = searchModel
        dialog.setContentView(view)
        mBehavior = BottomSheetBehavior.from(view.parent as View)
        return dialog
    }

    override fun onStart() {
        super.onStart()

        mBehavior!!.state = BottomSheetBehavior.STATE_COLLAPSED
        binding.editText.requestFocus()

        binding.editText.setOnEditorActionListener { text, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_SEARCH,
                EditorInfo.IME_ACTION_DONE,
                EditorInfo.IME_ACTION_NEXT,
                EditorInfo.IME_ACTION_GO,
                EditorInfo.IME_ACTION_SEND -> {
                    searchModel.search(text.text.toString())
                    text.clearFocus()
                }
            }
            false
        }
        binding.recyclerView.adapter = searchAdapter
        searchModel.list.observe(this, Observer {
            searchAdapter.submitList(it)
            if (it != null){
                searchModel.setSearch(false)

            }
            mBehavior!!.state = BottomSheetBehavior.STATE_COLLAPSED

        })

        searchModel.selectItem.observe(this, Observer { item ->

            if (item != null) {

                val intent = Intent()
                intent.putExtra("videoId", item.id?.videoId)

                item.snippet?.title?.let { s ->
                    val arr = s.split(" - ")

                    intent.putExtra("artist", arr[0])
                    if (arr.size == 2) {

                        intent.putExtra("song", arr[1])
                    }

                }
                mainViewModel.changeVideo(intent)

                mBehavior!!.state = BottomSheetBehavior.STATE_HIDDEN
            }

        })

    }


}