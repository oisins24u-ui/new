package com.example.iptvplayer.ui.content

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.iptvplayer.databinding.ItemStreamBinding

class StreamAdapter(
    private var items: List<StreamItem>,
    private val onClick: (StreamItem) -> Unit
) : RecyclerView.Adapter<StreamAdapter.ViewHolder>() {

    data class StreamItem(val id: Int, val name: String, val iconUrl: String?, val streamId: Int, val extension: String? = null)

    class ViewHolder(val binding: ItemStreamBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStreamBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.binding.nameTextView.text = item.name
        // holder.binding.iconImageView.setImageResource(android.R.drawable.ic_media_play)
        holder.itemView.setOnClickListener { onClick(item) }
    }

    override fun getItemCount() = items.size

    fun updateItems(newItems: List<StreamItem>) {
        items = newItems
        notifyDataSetChanged()
    }
}
