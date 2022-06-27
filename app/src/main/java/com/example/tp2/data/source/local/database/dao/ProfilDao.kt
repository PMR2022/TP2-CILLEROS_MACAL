package com.example.tp2.data.source.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tp2.data.model.ProfilListeToDo

@Dao
interface ProfilDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveOrUpdateProfil(profil: ProfilListeToDo)

    @Query("SELECT * FROM profil_list_to_do")
    suspend fun getProfils(): MutableList<ProfilListeToDo>

    @Query("SELECT * FROM profil_list_to_do WHERE login=:login AND pass =:pass")
    suspend fun authenticate(login: String, pass: String): ProfilListeToDo
}