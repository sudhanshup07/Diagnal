package com.example.diagnal.ui.listing

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.diagnal.R
import com.example.diagnal.databinding.ItemListingBinding
import com.example.diagnal.domain.model.Content
import com.squareup.picasso.Picasso

class ListingAdapter(private val onClickListener: OnClickListener):
    ListAdapter<Content, ListingAdapter.CategoryItemViewHolder>(DiffCallback) {

    class CategoryItemViewHolder(
        private var binding: ItemListingBinding

    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Content, onClickListener: OnClickListener) {

            binding.root.setOnClickListener {
                onClickListener.onClick(item, it)
            }
            binding.textView.text = item.name

            Picasso.get()
                .load("file:///android_asset/${item.posterImage}")
                .placeholder(R.drawable.placeholder_for_missing_posters)
                .into(binding.banner)
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Content>() {
        override fun areItemsTheSame(oldItem: Content, newItem: Content): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Content, newItem: Content): Boolean {
            return oldItem.name == newItem.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryItemViewHolder {
        return CategoryItemViewHolder(
            ItemListingBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun onBindViewHolder(holder: CategoryItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, onClickListener)
    }

    class OnClickListener(val clickListener: (item: Content, view: View) -> Unit) {
        fun onClick(item: Content, view: View) = clickListener(item, view)
    }
}