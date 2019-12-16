package com.leastarxon.marvelheroes.api

import com.leastarxon.marvelheroes.model.MarvelCharactersListResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface IApiHelper {
    @GET("/v1/public/characters")
    fun loadHeroes(@Query("ts") timeStamp: String,
                   @Query("apikey") openkey: String,
                   @Query("hash") hash: String,
                   @Query("orderBy") orderBy: String,
                   @Query("limit") limit: Int,
                   @Query("offset") offset: Int
                   ): Single<MarvelCharactersListResponse?>?

}