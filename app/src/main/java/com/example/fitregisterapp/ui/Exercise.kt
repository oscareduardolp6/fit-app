package com.example.fitregisterapp.ui

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.TypeConverter
import androidx.room.Upsert
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Entity
data class BilateralExercise(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val variation: String,
    val date: LocalDate = LocalDate.now(),
    val reps: List<Int>
)

@Entity
data class UnilateralExercise(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val variation: String,
    val date: LocalDate,
    val leftReps: List<Int>,
    val rightReps: List<Int>
)

@Dao
interface BilateralExerciseDao {
    @Upsert
    suspend fun upsertBilateralExercise(exercise: BilateralExercise)

    @Query("SELECT * FROM bilateralexercise WHERE date = :date")
    suspend fun getExercisesByDate(date: LocalDate): List<BilateralExercise>

    @Query("DELETE FROM bilateralexercise WHERE date = :date")
    suspend fun deleteExerciseByDate(date: LocalDate)

    @Query("SELECT * FROM bilateralexercise")
    suspend fun getAll(): List<BilateralExercise>

    @Query("SELECT * FROM bilateralexercise WHERE date = :date AND variation = :variation AND name = :name")
    suspend fun findExercise(name: String, variation: String, date: LocalDate = LocalDate.now()): BilateralExercise?

    @Query("DELETE FROM bilateralexercise")
    suspend fun deleteAll()

}

@Dao
interface UnilateralExerciseDao {
    @Upsert
    suspend fun upsert(exercise: UnilateralExercise)

    @Query("SELECT * FROM unilateralexercise WHERE date = :date")
    suspend fun getExerciseByDate(date: LocalDate): List<UnilateralExercise>

    @Query("DELETE FROM unilateralexercise WHERE date = :date")
    suspend fun deleteExercisesByDate(date: LocalDate)

    @Query("SELECT * FROM unilateralexercise")
    suspend fun getAll(): List<UnilateralExercise>

    @Query("SELECT * FROM unilateralexercise WHERE date = :date AND variation = :variation AND name = :name")
    suspend fun findExercise(name: String, variation: String, date: LocalDate = LocalDate.now()): UnilateralExercise?

    @Query("DELETE FROM unilateralexercise")
    suspend fun deleteAll()
}

class Converters {

    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE

    @TypeConverter
    fun fromLocalDate(date: LocalDate): String {
        return date.format(formatter)
    }

    @TypeConverter
    fun toLocalDate(dateString: String): LocalDate {
        return LocalDate.parse(dateString, formatter)
    }

    @TypeConverter
    fun fromListOfIntegers(list: List<Int>?): String? {
        return list?.joinToString(",") // Convierte la lista en una cadena separada por comas
    }

    @TypeConverter
    fun toListOfIntegers(data: String?): List<Int>? {
        return data?.split(",")?.map { it.toInt() } // Convierte la cadena de vuelta a una lista de enteros
    }
}
