package com.example.tp2.data.source.remote.api.responses

import com.example.tp2.data.model.ProfilListeToDo
import com.google.gson.annotations.SerializedName

data class AuthResponse(

    @SerializedName("version")
    val version: String,
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("status")
    val status: Int,
    @SerializedName("hash")
    val hash: String
) {
    fun toProfilListeToDo(login: String, pass: String):  ProfilListeToDo{
        return ProfilListeToDo(
            login = login,
            pass = pass,
            hash = hash
        )
    }
}