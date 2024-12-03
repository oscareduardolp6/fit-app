package com.example.fitregisterapp.exercise.infra

import com.example.fitregisterapp.exercise.domain.UnilateralExerciseRepository
import com.example.fitregisterapp.ui.AppDatabase
import com.example.fitregisterapp.ui.UnilateralExercise
import java.time.LocalDate

class RoomUnilateralExerciseRepository(val database: AppDatabase): UnilateralExerciseRepository {
    override suspend fun getByDate(date: LocalDate): List<UnilateralExercise> =
        database
            .unilateralExerciseDao()
            .getExerciseByDate(date)

    override suspend fun save(exercise: UnilateralExercise) =
        database
            .unilateralExerciseDao()
            .upsert(exercise)

    override suspend fun find(
        name: String,
        variation: String,
        date: LocalDate?
    ): UnilateralExercise? =
        database
            .unilateralExerciseDao()
            .findExercise(name, variation, date ?: LocalDate.now())

    override suspend fun clearAllByDate(date: LocalDate) =
        database
            .bilateralExerciseDao()
            .deleteExerciseByDate(date)

}