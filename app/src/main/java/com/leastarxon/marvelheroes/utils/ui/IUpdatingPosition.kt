package com.leastarxon.marvelheroes.utils.ui

import android.view.View.OnClickListener

interface IUpdatingPosition : OnClickListener {
    fun updatePosition(position: Int)
}
