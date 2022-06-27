package com.example.tp2.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "profil_list_to_do")
data class ProfilListeToDo (
    @PrimaryKey
    var login: String = "",
    var pass: String = "",
    var hash: String = ""
)

{
override fun toString(): String {
    return "{\"login\": \"${login}\"}"
}
}
