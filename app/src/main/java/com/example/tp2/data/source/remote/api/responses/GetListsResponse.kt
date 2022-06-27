package com.example.tp2.data.source.remote.api.responses

import com.example.tp2.data.model.ListeToDo
import com.google.gson.annotations.SerializedName

data class GetListsResponse(

    @SerializedName("version")
    val version: String,
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("status")
    val status: Int,
    @SerializedName("lists")
    val lists: MutableList<ListeToDo> = mutableListOf<ListeToDo>()
)
