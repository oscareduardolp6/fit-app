package com.example.fitregisterapp.ui

import java.time.LocalDate

class BilateralExerciseSaver(private val dao: BilateralExerciseDao) {
    suspend fun save(name: String, variation: String, reps: Int) {
        val exercise = dao.findExercise(name, variation)
            ?: BilateralExercise(name = name, variation = variation, reps = emptyList())
        val updatedExercise = exercise.copy(reps = exercise.reps + reps)
        dao.upsertBilateralExercise(updatedExercise)
    }
}

class UnilateralExerciseSaver(private val dao: UnilateralExerciseDao) {
    suspend fun save(name: String, variation: String, rightReps: Int, leftReps: Int) {
        val exercise = dao.findExercise(name, variation)
            ?: UnilateralExercise(
                name = name,
                variation = variation,
                date = LocalDate.now(),
                leftReps = emptyList(),
                rightReps = emptyList()
                )
        val updatedExercise = exercise.copy(
            rightReps = exercise.rightReps + rightReps,
            leftReps = exercise.leftReps + leftReps
        )
        dao.upsert(updatedExercise)
    }
}