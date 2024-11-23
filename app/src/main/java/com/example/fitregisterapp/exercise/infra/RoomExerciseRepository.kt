package com.example.fitregisterapp.exercise.infra

import com.example.fitregisterapp.exercise.domain.BilateralExerciseRepository
import com.example.fitregisterapp.ui.AppDatabase
import com.example.fitregisterapp.ui.BilateralExercise
import java.time.LocalDate

class RoomExerciseRepository(val database: AppDatabase): BilateralExerciseRepository {
    override suspend fun getByDate(date: LocalDate): List<BilateralExercise> =
        database
            .bilateralExerciseDao()
            .getExercisesByDate(date)

    override suspend fun save(exercise: BilateralExercise) =
        database
            .bilateralExerciseDao()
            .upsertBilateralExercise(exercise)

    override suspend fun find(name: String, variation: String, date: LocalDate?) =
        database
            .bilateralExerciseDao()
            .findExercise(name, variation, date ?: LocalDate.now())

    override suspend fun clearAllByDate(date: LocalDate) =
        database
            .bilateralExerciseDao()
            .deleteExerciseByDate(date)
}