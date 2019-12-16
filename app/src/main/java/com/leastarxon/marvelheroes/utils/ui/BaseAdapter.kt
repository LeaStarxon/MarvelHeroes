package com.leastarxon.marvelheroes.utils.ui

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.*
import com.leastarxon.marvelheroes.R
import com.leastarxon.marvelheroes.model.Hero
import com.leastarxon.marvelheroes.ui.details.DetailsFragment
import com.leastarxon.marvelheroes.ui.details.DetailsFragment.Companion.HERO_IMAGE_URL
import com.leastarxon.marvelheroes.ui.details.DetailsFragment.Companion.HERO_INFO
import com.leastarxon.marvelheroes.ui.details.DetailsFragment.Companion.HERO_NAME
import com.leastarxon.marvelheroes.ui.details.DetailsFragment.Companion.TRANSITION_NAME
import com.leastarxon.marvelheroes.utils.IUpdatingPosition

class BaseAdapter<T>(
    protected var holderLayout: Int,
    protected var variableId: Int,
    protected var activity: AppCompatActivity
) : RecyclerView.Adapter<BaseHolder?>() {
    var recyclerView: RecyclerView? = null
    var list: ArrayList<T> = ArrayList()
        get() {
            return field
        }
        set(list: ArrayList<T>) {
            field.clear()
            field.addAll(list)
            notifyDataSetChanged()
        }

    fun updateListWithClear(update: ArrayList<T>) {
        list.clear()
        list.addAll(update)
        notifyItemRangeChanged(0, list.size)
    }

    fun addToList(update: ArrayList<T>) {
        var previousLast = list.size - 1
        val countUpdate = update.size
        val newLastIndex = previousLast + countUpdate
        list.addAll(update)
        if (newLastIndex >= list.size) {
            previousLast = list.size - countUpdate - 1
        }
        notifyItemRangeInserted(previousLast, countUpdate)
    }

    fun refreshContext(compatActivity: AppCompatActivity) {
        activity = compatActivity
    }

    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): BaseHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(holderLayout, parent, false)
        return BaseHolder(v, viewType, BaseClickListener())
    }

    override fun onBindViewHolder(@NonNull holder: BaseHolder, position: Int) {
        holder.clickListener!!.updatePosition(position)
        holder.binding!!.setVariable(variableId, list[position])
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.imageView?.transitionName = "photoHero"+ position
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    fun getCurrentChildPosition(): Int {
        val gridLayoutManager = recyclerView?.layoutManager as? GridLayoutManager
        return gridLayoutManager?.findFirstVisibleItemPosition() ?: 0
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class BaseClickListener() : IUpdatingPosition {
        protected var position = 0
        override fun updatePosition(position: Int) {
            this.position = position
        }

        override fun onClick(view: View?) {
            val hero = list.get(position) as Hero
            val bundle = Bundle();
            bundle.putString(HERO_NAME, hero.name);
            bundle.putString(HERO_INFO, hero.description);
            bundle.putString(HERO_IMAGE_URL, hero.url);
            var nameTrans = "photoHero" + position
            bundle.putString(TRANSITION_NAME, nameTrans)
            var detailsFragment = DetailsFragment()
            detailsFragment.arguments = bundle
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                detailsFragment.sharedElementEnterTransition = AnimationUtil.getTransition()
                detailsFragment.enterTransition = TransitionInflater.from(activity).inflateTransition(android.R.transition.slide_top)
                detailsFragment.exitTransition = TransitionInflater.from(activity).inflateTransition(android.R.transition.slide_bottom)
                detailsFragment.sharedElementReturnTransition = AnimationUtil.getTransition()
            }

//            ViewCompat.setTransitionName(activity.findViewById(R.id.photoHero), nameTrans)
//            ViewCompat.setTransitionName(activity.findViewById(R.id.photoHeroDetails), nameTrans)
            var view = activity.findViewById(R.id.photoHero) as ImageView
            activity.supportFragmentManager.beginTransaction()
                .addSharedElement(view, nameTrans)
                .replace(R.id.container, detailsFragment, "detailsFragment")
                .addToBackStack(null)
                .commit()
        }
    }

}