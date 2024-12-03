package com.example.fitregisterapp.exercise.app

import com.example.fitregisterapp.shared.domain.toMXFormat
import com.example.fitregisterapp.ui.BilateralExercise
import com.example.fitregisterapp.ui.UnilateralExercise

private const val TAGS_SECTION = """---
tags: 
    - bitacora_ejercicio
---
"""

private fun header(exercise: BilateralExercise) =
    "# 💪 [[${exercise.name}]] del [[${exercise.date.toMXFormat()}]]"

private fun headerUnilateral(exercise: UnilateralExercise) =
    "# 💪 [[${exercise.name}]] del [[${exercise.date.toMXFormat()}]]"

private fun variation(exercise: BilateralExercise) =
    "[Variacion:: ${exercise.variation}]"

private fun unilateralVariation(exercise: UnilateralExercise) =
    "[Variacion:: ${exercise.variation}]"

private fun contentShape(exercise: BilateralExercise, setsContent: String) = """$TAGS_SECTION
${header(exercise)}
${variation(exercise)} 
$setsContent
"""

private fun contentShape(exercise: UnilateralExercise, setsContent: String) = """$TAGS_SECTION
${headerUnilateral(exercise)}
${unilateralVariation(exercise)}
$setsContent
"""

fun unilateralExerciseToMd(exercise: UnilateralExercise): String {
    val sets = exercise.rightReps.zip(exercise.leftReps).withIndex()
    var content = ""

    for ((index, values) in sets) {
        val (right, left) = values
        content += "\n[Serie_${index + 1}_derecha:: $right]"
        content += "\n[Serie_${index + 1}_izquierda:: $left]"
    }
    return contentShape(exercise, content)
}

fun bilateralExerciseToMd(exercise: BilateralExercise): String {
    val setsContent = exercise.reps
        .withIndex().joinToString("\n") { (index, value) ->
            "[Serie_${index + 1}:: $value]"
        }
    return contentShape(exercise, setsContent)
}