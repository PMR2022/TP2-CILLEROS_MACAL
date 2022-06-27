package com.example.tp2.data.source.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.tp2.data.model.ItemToDo
import com.example.tp2.data.model.ListeToDo
import com.example.tp2.data.model.ProfilListeToDo
import com.example.tp2.data.source.local.database.dao.ItemDao
import com.example.tp2.data.source.local.database.dao.ListDao
import com.example.tp2.data.source.local.database.dao.ProfilDao

@Database(
    entities = [
        ItemToDo::class,
        ListeToDo::class,
        ProfilListeToDo::class
    ],
    version = 1
)
abstract class ToDoDatabase: RoomDatabase() {
    abstract fun profilDao(): ProfilDao
    abstract fun listDao(): ListDao
    abstract fun itemDao(): ItemDao

}