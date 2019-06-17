package jdr.tv.search.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.RequestManager
import jdr.tv.base.Dispatchers.IOExecutor
import jdr.tv.local.entities.Show
import jdr.tv.search.databinding.ItemShowBackdropBinding
import jdr.tv.ui.adapter.BindingPagedListAdapter

class SearchAdapter(private val glide: RequestManager) :
    BindingPagedListAdapter<Show, ItemShowBackdropBinding>(AsyncDifferConfig.Builder(diffUtil).setBackgroundThreadExecutor(IOExecutor).build()) {

    override fun bindingForViewType(viewType: Int, inflater: LayoutInflater, parent: ViewGroup): ItemShowBackdropBinding {
        return ItemShowBackdropBinding.inflate(inflater, parent, false)
    }

    override fun bind(binding: ItemShowBackdropBinding, item: Show?) {
        binding.show = item
        binding.glide = glide
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Show>() {
            override fun areItemsTheSame(oldItem: Show, newItem: Show): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Show, newItem: Show): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}

/*
@BindingAdapter("backdropPath")
fun setBackdropPath(view: ImageView, backdropPath: String?) {
    view.loadImage(backdropPath, jdr.tv.app.R.drawable.ic_backdrop, "w300", "w1280")
}*/