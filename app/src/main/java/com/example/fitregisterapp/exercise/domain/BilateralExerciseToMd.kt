package com.example.fitregisterapp.exercise.domain

import com.example.fitregisterapp.shared.domain.toMXFormat
import com.example.fitregisterapp.ui.BilateralExercise

private const val TAGS_SECTION = """
---
tags: 
    - bitacora_ejercicio
---
"""

private fun header(exercise: BilateralExercise) =
    "# 💪 [[${exercise.name}]] del [[${exercise.date.toMXFormat()}]]"

private fun variation(exercise: BilateralExercise) =
    "[Variacion:: ${exercise.variation}]"

private fun contentShape(exercise: BilateralExercise, setsContent: String) = """
$TAGS_SECTION
${header(exercise)}
${variation(exercise)} 
$setsContent
"""

fun bilateralExerciseToMd(exercise: BilateralExercise): String {
    val setsContent = exercise.reps
        .withIndex().joinToString("\n") { (index, value) ->
            "[Serie_${index + 1}:: $value]"
        }
    return contentShape(exercise, setsContent)
}