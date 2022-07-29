package com.fita.test.takehometest.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fita.test.takehometest.R
import com.fita.test.takehometest.databinding.AdapterMainBinding
import com.fita.test.takehometest.model.TrackData
import com.fita.test.takehometest.utils.AdapterListener

class MainAdapter constructor(private val items: MutableList<TrackData>) : RecyclerView.Adapter<MainAdapter.ViewHolder>() {
    lateinit var listener: AdapterListener<TrackData>
    private val isLoading: MutableList<Boolean> = ArrayList()
    private val isIndicator: MutableList<Boolean> = ArrayList()

    init {
        for (i in items.indices) {
            isLoading.add(i, false)
            isIndicator.add(i, false)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            AdapterMainBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = items[position]

        holder.binding.tvSongName.text = data.trackName
        holder.binding.tvArtistName.text = data.artistName
        holder.binding.tvAlbum.text = data.collectionName

        holder.itemView.setOnClickListener {
            changeStateShowOneItem(isIndicator, position)
            showLoading(position)
            listener.onClick(holder.itemView.context, data, position)
            notifyDataSetChanged()
        }

        if (isLoading[position]) {
            holder.binding.viewLoading.visibility = View.VISIBLE
        } else {
            holder.binding.viewLoading.visibility = View.GONE
        }

        if (isIndicator[position]) {
            holder.binding.ivIndicator.visibility = View.VISIBLE
        } else {
            holder.binding.ivIndicator.visibility = View.GONE
        }

        Glide.with(holder.itemView)
            .load(data.artworkUrl100)
            .into(holder.binding.ivItem)

        Glide.with(holder.itemView)
            .asGif()
            .load(R.drawable.play_indicator)
            .into(holder.binding.ivIndicator)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(view: AdapterMainBinding) : RecyclerView.ViewHolder(view.root) {
        val binding = view
    }

    private fun changeStateShowOneItem(listItems: MutableList<Boolean>, p: Int) {
        for (position in listItems.indices) {
            if (position == p) {
                listItems[p] = true
                continue
            }
            listItems[position] = false
        }
    }

    fun dismissLoading(position: Int) {
        isLoading[position] = false
        notifyItemChanged(position)
    }

    private fun showLoading(position: Int) {
        isLoading[position] = true
        changeStateShowOneItem(isLoading, position)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun hideIndicator(position: Int) {
        isIndicator[position] = false
        notifyDataSetChanged()
    }

    fun showIndicator(position: Int) {
        isIndicator[position] = true
        changeStateShowOneItem(isIndicator, position)
        notifyItemChanged(position)
    }
}