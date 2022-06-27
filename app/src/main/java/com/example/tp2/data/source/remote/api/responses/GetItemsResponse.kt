package com.example.tp2.data.source.remote.api.responses

import com.google.gson.annotations.SerializedName

data class GetItemsResponse(
    @SerializedName("version")
    val version: String,
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("status")
    val status: Int,
    @SerializedName("items")
    val items: MutableList<ItemResponse>
)