package com.example.tp2.data.source.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tp2.data.model.ListeToDo



@Dao
interface ListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveOrUpdateLists(items: MutableList<ListeToDo>)

    @Query("SELECT * FROM list_to_do")
    suspend fun getLists(): MutableList<ListeToDo>

    @Query("SELECT * FROM list_to_do WHERE login=:login")
    suspend fun getLists(login: String): MutableList<ListeToDo>

    @Query("SELECT * FROM list_to_do WHERE id=:idList")
    suspend fun getList(idList: Int): ListeToDo
}
