package com.app.william.youtubewithsrt

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.william.youtubewithsrt.databinding.RowSearchBinding
import com.app.william.youtubewithsrt.javabean.Item
import com.app.william.youtubewithsrt.javabean.Snippet

class SearchAdapter(val searchModel: SearchViewModel) :
    PagedListAdapter<Item, SearchAdapter.ViewHolder>(object : DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem.id?.videoId == newItem.id?.videoId
        }

        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
            return true
        }
    }) {

    inner class ViewHolder(private val binding: RowSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {

            binding.root.setOnClickListener {
                getItem(adapterPosition)?.let {
                    searchModel.setItem(it)
                }
            }
        }

        fun set(data: Snippet) {
            binding.snippet = data
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: RowSearchBinding = RowSearchBinding.inflate(layoutInflater, parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.snippet?.let {
            holder.set(it)
        }
    }
}