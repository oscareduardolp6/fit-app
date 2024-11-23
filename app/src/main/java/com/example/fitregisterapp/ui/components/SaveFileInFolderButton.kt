package com.example.fitregisterapp.ui.components

import android.net.Uri
import android.widget.Toast
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.fitregisterapp.exercise.app.MdFileToSelectedFolderSaver
import com.example.fitregisterapp.exercise.domain.BilateralExerciseRepository
import com.example.fitregisterapp.exercise.domain.Notifier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate

@Composable
fun SaveFileInFolderButton(bilateralExerciseRepository: BilateralExerciseRepository, selectedFolder: Uri) {
    val context = LocalContext.current
    val notify: Notifier = { text -> Toast.makeText(context, text, Toast.LENGTH_SHORT).show() }
    val saveMdFileToSelectedFolder = MdFileToSelectedFolderSaver(
        bilateralExerciseRepository,
        notify
    )
    Button(onClick = {
        CoroutineScope(Dispatchers.IO).launch {
            saveMdFileToSelectedFolder(context, selectedFolder, LocalDate.now())
        }
    }) {
        Text("Guardar archivo en la carpeta seleccionada")
    }
}