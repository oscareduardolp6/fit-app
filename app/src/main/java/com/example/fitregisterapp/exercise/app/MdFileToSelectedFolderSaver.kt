package com.example.fitregisterapp.exercise.app

import android.content.Context
import android.net.Uri
import com.example.fitregisterapp.bilateralExerciseToFileName
import com.example.fitregisterapp.exercise.domain.BilateralExerciseRepository
import com.example.fitregisterapp.exercise.domain.MdFile
import com.example.fitregisterapp.exercise.domain.Notifier
import com.example.fitregisterapp.exercise.domain.UnilateralExerciseRepository
import com.example.fitregisterapp.exercise.infra.FileInfo
import com.example.fitregisterapp.exercise.infra.saveFileToSelectedFolder
import com.example.fitregisterapp.ui.BilateralExercise
import com.example.fitregisterapp.ui.UnilateralExercise
import com.example.fitregisterapp.unilateralExerciseToFileName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate

fun MdFileFromExercise(exercise: BilateralExercise): MdFile = MdFile(
    name = bilateralExerciseToFileName(exercise),
    content = bilateralExerciseToMd(exercise)
)

fun MdFileFromUnilateralExercise(exercise: UnilateralExercise): MdFile = MdFile(
    name = unilateralExerciseToFileName(exercise),
    content  = unilateralExerciseToMd(exercise)
)

class MdFileToSelectedFolderSaver(
    private val bilateralExerciseRepo: BilateralExerciseRepository,
    private val unilateralExerciseRepo: UnilateralExerciseRepository,
    private val notify: Notifier
) {
    suspend operator fun invoke(
        context: Context,
        selectedFolderUri: Uri,
        _date: LocalDate
    ){
        val date = _date.minusDays(1)
        val exercises = bilateralExerciseRepo.getByDate(date)
        val unilateralExercises = unilateralExerciseRepo.getByDate(date)

        if(exercises.isEmpty() && unilateralExercises.isEmpty()) {
            withContext(Dispatchers.Main) {
                notify("No hay ejercicios guardados hoy")
            }
            return
        }

        val bilateralExerciseFiles = exercises.map(::MdFileFromExercise)
        val unilateralExerciseFiles = unilateralExercises.map(::MdFileFromUnilateralExercise)

        (bilateralExerciseFiles + unilateralExerciseFiles)
            .forEach { mdFile ->
                saveFileToSelectedFolder(
                    context,
                    fileInfo = FileInfo(
                        name = mdFile.name,
                        content = mdFile.content,
                        folderUri = selectedFolderUri)
                )
                withContext(Dispatchers.Main) {
                    notify("Creado el archivo ${mdFile.name}")
                }
            }
    }

}