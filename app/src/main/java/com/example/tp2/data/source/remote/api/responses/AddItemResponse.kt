package com.example.tp2.data.source.remote.api.responses

import com.example.tp2.data.model.ItemToDo
import com.google.gson.annotations.SerializedName

data class AddItemResponse(
    @SerializedName("version")
    val version: String,
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("status")
    val status: Int,
    @SerializedName("item")
    val item: ItemToDo = ItemToDo()
)