package com.example.tp2.data.source.local

import android.app.Application
import androidx.room.Room
import com.example.tp2.data.model.ItemToDo
import com.example.tp2.data.model.ListeToDo
import com.example.tp2.data.model.ProfilListeToDo
import com.example.tp2.data.source.local.database.ToDoDatabase

class LocalDataSource(
    application: Application
) {
    private val roomDatabase =
        Room.databaseBuilder(application, ToDoDatabase::class.java, "room-database").build()


    private val daoItem = roomDatabase.itemDao()
    private val daoList = roomDatabase.listDao()
    private val daoProfil = roomDatabase.profilDao()



    suspend fun saveOrUpdateItems(items: MutableList<ItemToDo>) = daoItem.saveOrUpdateItems(items)
    suspend fun saveOrUpdateItem(item: ItemToDo) = daoItem.saveOrUpdateItem(item)
    suspend fun getItem(idItem: Int): ItemToDo? = daoItem.getItem(idItem)
    suspend fun getItems(idList: Int) = daoItem.getItems(idList)
    suspend fun getNotUpdatedItems(): MutableList<ItemToDo> = daoItem.getNotUpdatedItem()


        suspend fun saveOrUpdateLists(lists: MutableList<ListeToDo>) = daoList.saveOrUpdateLists(lists)
    suspend fun getList(idList: Int): ListeToDo = daoList.getList(idList)
    suspend fun getLists(): MutableList<ListeToDo> = daoList.getLists()
    suspend fun getLists(login: String): MutableList<ListeToDo> = daoList.getLists(login)


    suspend fun saveOrUpdateProfil(profil: ProfilListeToDo) = daoProfil.saveOrUpdateProfil(profil)
    suspend fun authenticate(login: String, pass: String) = daoProfil.authenticate(login, pass)
    suspend fun getProfils() = daoProfil.getProfils()

    }

