package com.leastarxon.marvelheroes.utils

import android.view.View.OnClickListener

interface IUpdatingPosition : OnClickListener {
    fun updatePosition(position: Int)
}
