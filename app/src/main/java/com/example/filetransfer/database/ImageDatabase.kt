package com.example.filetransfer.database

import android.content.Context
import androidx.room.*
import com.example.filetransfer.tools.Constant

@Entity(tableName = Constant.IMAGE_TABLE)
data class ImageTable(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val image_url: String,
    val title: String,
    val file_location: String
)

@Database(entities = [ImageTable::class], version = 1, exportSchema = false)
abstract class ImageDatabase : RoomDatabase() {
    abstract fun daoImageInterface(): DaoImageInterface

    companion object {
        @Volatile
        private var INSTANCE: ImageDatabase? = null

        fun getDatabase(context: Context): ImageDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ImageDatabase::class.java,
                    Constant.IMAGE_TABLE
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}