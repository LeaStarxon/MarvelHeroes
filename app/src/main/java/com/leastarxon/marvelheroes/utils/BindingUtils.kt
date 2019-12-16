package com.leastarxon.marvelheroes.utils

import android.widget.ImageView
import androidx.constraintlayout.widget.Placeholder
import androidx.databinding.BindingAdapter
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.leastarxon.marvelheroes.R
import com.leastarxon.marvelheroes.utils.ui.BaseAdapter
import com.leastarxon.marvelheroes.utils.ui.BaseHolder
import com.squareup.picasso.Picasso
import java.io.File

object BindingUtils {

    @BindingAdapter("app:adapterRecyclerView")
    @JvmStatic
    fun setAdapter(
        recyclerView: RecyclerView,
        adapter: RecyclerView.Adapter<BaseHolder?>
    ) {
        recyclerView.adapter = adapter
    }

    @BindingAdapter("app:layoutManagerRecyclerView")
    @JvmStatic
    fun setLayoutManager(
        recyclerView: RecyclerView,
        layout: RecyclerView.LayoutManager
    ) {
        recyclerView.layoutManager = layout
    }

    @BindingAdapter("app:refreshingListener")
    @JvmStatic
    fun setSwipeRefreshListener(
        refreshLayout: SwipeRefreshLayout,
        refreshListener: SwipeRefreshLayout.OnRefreshListener
    ) {
        refreshLayout.setOnRefreshListener(refreshListener)
    }

    @BindingAdapter("app:goToIndex")
    @JvmStatic
    fun goToIndex(
        recyclerView: RecyclerView,
        index: Int
    ) {
        val baseAdapter = recyclerView.adapter as? BaseAdapter<*>
        if (baseAdapter != null && baseAdapter.list.size > index) {
            recyclerView.scrollToPosition(index)
        }
    }

    @BindingAdapter("app:scrollListener")
    @JvmStatic
    fun scrollListener(
        recyclerView: RecyclerView,
        scrollListener: RecyclerView.OnScrollListener
    ) {
        recyclerView.addOnScrollListener(scrollListener)
    }

    @BindingAdapter("app:imageLoad")
    @JvmStatic
    fun loadImage(imageView: ImageView, id: String?) {
        if (id == null || id.isEmpty()) {
            Picasso.with(imageView.context)
                .load(R.drawable.ic_superhero)
                .placeholder(R.drawable.ic_superhero)
                .into(imageView)
        } else {
            Picasso.with(imageView.context)
                .load(id)
                .placeholder(R.drawable.ic_superhero)
                .fit()
                .into(imageView)
        }
    }
}