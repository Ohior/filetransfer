package com.example.filetransfer.database

import androidx.lifecycle.LiveData
import androidx.room.*
import java.awt.font.TextAttribute

@Dao
interface DaoImageInterface {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addToDatabase(table: ImageTable)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateDatabase(textAttribute: TextAttribute)

    @Delete
    suspend fun dataAll()

    @Query("SELECT * FROM imageTable ORDER BY id ASC")
    fun getAllData(): LiveData<List<ImageTable>>
}