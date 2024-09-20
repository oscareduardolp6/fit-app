package com.example.fitregisterapp.ui

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.Upsert
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

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

    @Query("DELETE FROM filename")
    suspend fun deleteAll()

}

@Database(entities = [FileName::class, BilateralExercise::class, UnilateralExercise::class], version = 2)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun fileNameDao(): FileNameDao
    abstract fun bilateralExerciseDao(): BilateralExerciseDao
    abstract fun unilateralExerciseDao(): UnilateralExerciseDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Agrega la tabla para la nueva entidad UnilateralExercise
                database.execSQL("""
            CREATE TABLE IF NOT EXISTS bilateralexercise (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                name TEXT NOT NULL,
                variation TEXT NOT NULL,
                reps TEXT NOT NULL,
                date TEXT NOT NULL
            );
        """.trimIndent())

                database.execSQL("""
            CREATE TABLE IF NOT EXISTS unilateralexercise (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                name TEXT NOT NULL,
                variation TEXT NOT NULL,
                date TEXT NOT NULL,
                leftReps TEXT NOT NULL,
                rightReps TEXT NOT NULL
            );
        """.trimIndent())
            }
        }
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}


