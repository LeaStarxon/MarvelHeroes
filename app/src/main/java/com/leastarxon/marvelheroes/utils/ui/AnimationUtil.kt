package com.leastarxon.marvelheroes.utils.ui

import androidx.transition.*

object AnimationUtil {
    fun getTransition():TransitionSet{
        val detailsTransition = TransitionSet()
        detailsTransition.addTransition(ChangeBounds())
        detailsTransition.addTransition(ChangeTransform())
        detailsTransition.addTransition(ChangeImageTransform())
        return detailsTransition
    }
}