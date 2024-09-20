package com.example.fitregisterapp.ui

class BilateralExerciseSaver(private val dao: BilateralExerciseDao) {
    suspend fun save(name: String, variation: String, reps: Int) {
        val exercise = dao.findExercise(name, variation)
            ?: BilateralExercise(name = name, variation = variation, reps = emptyList())
        val updatedExercise = exercise.copy(reps = exercise.reps + reps)
        dao.upsertBilateralExercise(updatedExercise)
    }
}