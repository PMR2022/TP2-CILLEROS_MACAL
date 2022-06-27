package com.example.tp2.data.source.remote.api.responses

import com.google.gson.annotations.SerializedName

class ItemResponse (
    var id: Int,
    @SerializedName("label")
    var description: String,
    @SerializedName("checked")
    var fait: Int = 0
)
