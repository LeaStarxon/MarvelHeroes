package com.leastarxon.marvelheroes.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Hero(
    @SerializedName("id") var id: String?,
    @SerializedName("name") var name: String?,
    @SerializedName("description") var description: String?,
    @SerializedName("thumbnail") var thumbnail: Thumbnail?,
    var url: String? = ((if(thumbnail?.path != null) thumbnail.path else "") ?: "") + "." + ((if(thumbnail?.extension != null) thumbnail.extension else "") ?: "")
) : Serializable


