package com.example.fitregisterapp.exercise.domain

import com.example.fitregisterapp.ui.UnilateralExercise
import java.time.LocalDate

interface UnilateralExerciseRepository {
    suspend fun getByDate(date: LocalDate): List<UnilateralExercise>
    suspend fun save(exercise: UnilateralExercise)
    suspend fun find(name: String, variation: String, date: LocalDate?): UnilateralExercise?
    suspend fun clearAllByDate(date: LocalDate)
}