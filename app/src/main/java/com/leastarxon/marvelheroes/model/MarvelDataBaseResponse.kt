package com.leastarxon.marvelheroes.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MarvelDataBaseResponse (@SerializedName("count") var count : Int?,
                                   @SerializedName("limit") var limit : Int?,
                                   @SerializedName("offset") var offset : Int,
                                   @SerializedName("results") var results : ArrayList<Hero>?) :Serializable