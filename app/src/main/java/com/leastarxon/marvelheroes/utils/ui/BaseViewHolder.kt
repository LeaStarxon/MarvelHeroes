package com.leastarxon.marvelheroes.utils.ui

import android.view.View
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.leastarxon.marvelheroes.BR
import com.leastarxon.marvelheroes.R
import com.leastarxon.marvelheroes.utils.IUpdatingPosition

class BaseHolder(v: View, viewtype: Int, clickListener: IUpdatingPosition) : RecyclerView.ViewHolder(v) {
    var binding: ViewDataBinding?
    var viewtype: Int
    var clickListener: IUpdatingPosition? = null
    var imageView: ImageView? = null
    init {
        binding = DataBindingUtil.bind(v)
        this.viewtype = viewtype
        this.clickListener = clickListener
        imageView = v.findViewById(R.id.photoHero) as ImageView
        binding!!.setVariable(BR.clickVM, clickListener)
    }
}