package com.example.fitregisterapp.exercise.domain

import com.example.fitregisterapp.ui.BilateralExercise
import java.time.LocalDate

interface BilateralExerciseRepository {
    suspend fun getByDate(date: LocalDate): List<BilateralExercise>
    suspend fun save(exercise: BilateralExercise)
    suspend fun find(name: String, variation: String, date: LocalDate?): BilateralExercise?
    suspend fun clearAllByDate(date: LocalDate)
}