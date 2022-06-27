package com.example.tp2.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "list_to_do")
data class ListeToDo (

    @PrimaryKey
    var id: Int = 0,
    @SerializedName("label")
    var titreListToDo: String = "",
    var login: String = ""

) {
override fun toString(): String {
    return "{\"id\": \"${id}\", \"titreListToDo\": \"${titreListToDo}\"}"
}
}