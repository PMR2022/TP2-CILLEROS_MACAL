package com.example.tp2.data.source.remote

import com.example.tp2.data.source.remote.api.Service
import com.example.tp2.data.source.remote.api.responses.AddItemResponse
import com.example.tp2.data.source.remote.api.responses.AuthResponse
import com.example.tp2.data.source.remote.api.responses.GetItemsResponse
import com.example.tp2.data.source.remote.api.responses.GetListsResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RemoteDataSource {
    private var BASE_URL = "http://tomnab.fr/todo-api/"
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val service = retrofit.create(Service::class.java)

    suspend fun authenticate(user: String, pass: String): AuthResponse {
        return service.authenticate(user, pass)
    }


    suspend fun addItem(id: Int, hash: String, description: String): AddItemResponse {
        return service.addItem(id, hash, description)
    }
    suspend fun getItems(id: Int, hash: String): GetItemsResponse {
        return service.getItems(id, hash)
    }
    suspend fun updateItem(idList: Int, hash: String, idItem: Int, check: Int): AddItemResponse {
        return service.updateItem(idList, idItem, hash, check)
    }
    suspend fun getLists(hash: String): GetListsResponse {
        return service.getLists(hash)
    }
}