package com.example.fitregisterapp.exercise.app

import android.content.Context
import android.net.Uri
import com.example.fitregisterapp.bilateralExerciseToFileName
import com.example.fitregisterapp.exercise.domain.BilateralExerciseRepository
import com.example.fitregisterapp.exercise.domain.MdFile
import com.example.fitregisterapp.exercise.domain.Notifier
import com.example.fitregisterapp.exercise.infra.FileInfo
import com.example.fitregisterapp.exercise.infra.saveFileToSelectedFolder
import com.example.fitregisterapp.ui.BilateralExercise
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate

fun MdFileFromExercise(exercise: BilateralExercise): MdFile = MdFile(
    name = bilateralExerciseToFileName(exercise),
    content = bilateralExerciseToMd(exercise)
)

class MdFileToSelectedFolderSaver(
    private val repository: BilateralExerciseRepository,
    private val notify: Notifier
) {
    suspend operator fun invoke(
        context: Context,
        selectedFolderUri: Uri,
        date: LocalDate
    ){
        val exercises = repository.getByDate(date)
        if(exercises.isEmpty()) {
            withContext(Dispatchers.Main) {
                notify("No hay ejercicios guardados hoy")
            }
            return
        }
        exercises
            .map(::MdFileFromExercise)
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