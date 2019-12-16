package com.leastarxon.marvelheroes.ui.details

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DetailsViewModel() : ViewModel() {
    var name = MutableLiveData<String>()
        get() = field
    var info = MutableLiveData<String>()
        get() = field
    var url = MutableLiveData<String>()
        get() = field

    init {
        name.value = ""
        info.value = ""
        url.value = ""
    }

    internal fun initViews(
        heroName: String,
        heroImageUrl: String,
        heroInfo: String
    ) {
        name.value = heroName
        url.value = heroImageUrl
        info.value = heroInfo
    }

}
