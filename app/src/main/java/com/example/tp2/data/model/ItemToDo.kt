package com.example.tp2.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "item_to_do")
data class ItemToDo(

    @PrimaryKey
    var id: Int = 0,
    @SerializedName("label")
    var description: String = "",
    @SerializedName("checked")
    var fait: Int = 0,
    var idList: Int = 0,
    var isUpdated: Int = 0
) {



@JvmName("getFait1")
fun getFait(): Int{
    return this.fait
}
override fun toString(): String {
    return "{\"id\": \"${id}\", \"description\": \"${description}\", \"checked\": ${fait}}"
}
}