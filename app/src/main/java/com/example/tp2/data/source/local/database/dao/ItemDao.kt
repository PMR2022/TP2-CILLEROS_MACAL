package com.example.tp2.data.source.local.database.dao


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tp2.data.model.ItemToDo

@Dao
interface ItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveOrUpdateItems(items: MutableList<ItemToDo>)

    @Query("SELECT * FROM item_to_do WHERE idList=:idList")
    suspend fun getItems(idList: Int): MutableList<ItemToDo>

    @Query("SELECT * FROM item_to_do WHERE id=:idItem")
    suspend fun getItem(idItem: Int): ItemToDo

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveOrUpdateItem(item: ItemToDo)


    @Query("SELECT * FROM item_to_do WHERE isUpdated=0")
    suspend fun getNotUpdatedItem(): MutableList<ItemToDo>
}