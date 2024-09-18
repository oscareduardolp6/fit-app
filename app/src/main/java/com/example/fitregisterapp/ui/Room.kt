package com.example.fitregisterapp.ui

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Entity
data class FileName(
    @PrimaryKey(autoGenerate = false)
    val name: String
)

@Dao
interface FileNameDao {
    @Upsert
    suspend fun upsertFileName(fileName: FileName)

    @Query("SELECT * FROM filename")
    suspend fun getAllFileNames(): List<FileName>
}

@Database(entities = [FileName::class], version = 1)
abstract class FileNameDatabase: RoomDatabase() {
    abstract fun fileNameDao(): FileNameDao

    companion object {
        @Volatile
        private var INSTANCE: FileNameDatabase? = null

        fun getDatabase(context: Context): FileNameDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FileNameDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}


