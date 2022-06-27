package com.example.tp2.data.source.remote.api

import com.example.tp2.data.source.remote.api.responses.*
import retrofit2.http.*


interface Service {

    @POST("authenticate")
    suspend fun authenticate(@Query("user") user: String, @Query("password") pass: String): AuthResponse

    @GET("lists")
    suspend fun getLists(@Query("hash") hash: String): GetListsResponse

    @GET("lists/{id}/items")
    suspend fun getItems(@Path("id") id: Int, @Query("hash") hash: String): GetItemsResponse

    @POST("lists/{id}/items")
    suspend fun addItem(@Path("id") id: Int, @Query("hash") hash: String, @Query("label") description: String): AddItemResponse

    @PUT("lists/{idList}/items/{idItem}")
    suspend fun updateItem(@Path("idList") idList: Int, @Path("idItem") idItem: Int, @Query("hash") hash: String, @Query("check") check: Int): AddItemResponse
}