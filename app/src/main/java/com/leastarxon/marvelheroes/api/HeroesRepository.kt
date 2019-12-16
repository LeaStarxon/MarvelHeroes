package com.leastarxon.marvelheroes.api

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.leastarxon.marvelheroes.model.MarvelCharactersListResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class HeroesRepository (var application: Application) {
    private var heroesResponse: MarvelCharactersListResponse = MarvelCharactersListResponse("","","",null)
    private var mutableLiveData = MutableLiveData<MarvelCharactersListResponse>()

    @SuppressLint("CheckResult")
    fun getMutableLiveData(needLoad: Boolean, offset: Int): MutableLiveData<MarvelCharactersListResponse> {
        if(needLoad) {
            ApiHelper().loadCharactersList(offset)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ handleList(it as MarvelCharactersListResponse) }, { errorList(it) })
        }
        return mutableLiveData;
    }
    private fun errorList(throwable: Throwable){
        throwable.printStackTrace()
        mutableLiveData.postValue(MarvelCharactersListResponse("","","300", null))
    }
    private fun handleList(charactersResponse: MarvelCharactersListResponse){
        if(charactersResponse.data?.results != null) {
            mutableLiveData.postValue(charactersResponse)
        }
    }
}