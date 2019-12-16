package com.leastarxon.marvelheroes.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MarvelCharactersListResponse (@SerializedName("attributionHTML") var attributionHTML : String?,
                                         @SerializedName("attributionText") var attributionText : String?,
                                         @SerializedName("code") var code : String?,
                                         @SerializedName("data") var data : MarvelDataBaseResponse?) :Serializable