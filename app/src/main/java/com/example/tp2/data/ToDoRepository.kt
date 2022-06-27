package com.example.tp2.data

import android.app.Application
import android.util.Log
import com.example.tp2.data.model.ItemToDo
import com.example.tp2.data.model.ListeToDo
import com.example.tp2.data.model.ProfilListeToDo
import com.example.tp2.data.source.local.LocalDataSource
import com.example.tp2.data.source.remote.RemoteDataSource
import com.example.tp2.data.source.remote.api.responses.ItemResponse
import java.lang.Exception

class ToDoRepository (
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
)

{
    suspend fun authenticate(login: String, pass: String): ProfilListeToDo?{
        val profil: ProfilListeToDo? = try {
            remoteDataSource.authenticate(login, pass).toProfilListeToDo(login, pass).also {
                localDataSource.saveOrUpdateProfil(it)
            }
        } catch (e:Exception){
            Log.d("EDPMR", "Erreur: ${e.message}")
            localDataSource.authenticate(login, pass)
        }
        return profil
    }




    suspend fun getLists(login: String, hash: String): MutableList<ListeToDo>{
        if (hash == null) return localDataSource.getLists(login)
        return try {
            remoteDataSource.getLists(hash).also {
                Log.d("EDPMR", it.toString())
                var lists = mutableListOf<ListeToDo>()
                for (list_response in it.lists) {
                    var list_todo = localDataSource.getList(list_response.id)
                    if (list_todo !== null) {
                        list_todo.titreListToDo = list_response.titreListToDo
                    } else {
                        list_todo = ListeToDo(
                            id = list_response.id,
                            titreListToDo = list_response.titreListToDo,
                            login = login
                        )
                    }
                    lists.add(list_todo)
                }
                Log.d("EDPMR", lists.toString())
                localDataSource.saveOrUpdateLists(lists)
            }
            localDataSource.getLists(login)
            } catch (e: Exception) {
            Log.d("EDPMR", "Erreur: ${e.message}")
            localDataSource.getLists(login)
        }
    }

    suspend fun getItems(idList: Int, hash: String): MutableList<ItemToDo> {
        Log.d("EDPMR", "appel getItems")
        if (hash == null) return localDataSource.getItems(idList)

        val toDoList = localDataSource.getList(idList)
        val itemList = localDataSource.getNotUpdatedItems()
        for (item in itemList){
            try {
                remoteDataSource.updateItem(idList, hash, item.id, item.fait)
            } catch (e:Exception){
                Log.d("EDPMR", "Erreur: ${e.message}")
            }
        }
        return try {
            remoteDataSource.getItems(idList, hash).also {
                var items = mutableListOf<ItemToDo>()
                for (item_response: ItemResponse in it.items) {
                    var item = localDataSource.getItem(item_response.id)
                    if(item !== null){

                        item.description = item_response.description
                        item.fait = item_response.fait
                    } else {
                        item = ItemToDo(
                            id = item_response.id,
                            description = item_response.description,
                            fait = item_response.fait,
                            idList = toDoList.id,
                            isUpdated = 1
                        )
                    }
                    items.add(item)
                    Log.d("EDPMR", it.items.toString())
                }
                localDataSource.saveOrUpdateItems(items)
            }
            localDataSource.getItems(toDoList.id)
        } catch (e: Exception) {
            localDataSource.getItems(toDoList.id)
        }
    }

    suspend fun updateItem(idList: Int, hash: String, idItem: Int, fait: Int) {
        try {
            val item = localDataSource.getItem(idItem)
            if (item?.isUpdated == 0){
                item.isUpdated = 1
                remoteDataSource.updateItem(idList, hash, idItem, item?.fait!!)
                localDataSource.saveOrUpdateItem(item)
            } else{
                remoteDataSource.updateItem(idList, hash, idItem, fait).also {
                    Log.d("EDPMR", it.toString())

                    item?.fait = fait

                    localDataSource.saveOrUpdateItem(item!!)
                }
            }
        } catch (e: Exception){

            Log.d("EDPMR", "Erreur: ${e.message}")
            val item = localDataSource.getItem(idItem)
            item?.isUpdated = 0
            item?.fait = fait
            localDataSource.saveOrUpdateItem(item!!)
        }
    }


    companion object {
        fun newInstance(application: Application): ToDoRepository{
            return ToDoRepository(
                localDataSource = LocalDataSource(application),
                remoteDataSource = RemoteDataSource()
            )
        }
    }
}