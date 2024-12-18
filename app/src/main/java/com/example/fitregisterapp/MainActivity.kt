package com.example.fitregisterapp

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fitregisterapp.exercise.domain.BilateralExerciseRepository
import com.example.fitregisterapp.exercise.domain.UnilateralExerciseRepository
import com.example.fitregisterapp.exercise.infra.RoomExerciseRepository
import com.example.fitregisterapp.exercise.infra.RoomUnilateralExerciseRepository
import com.example.fitregisterapp.shared.domain.toMXFormat
import com.example.fitregisterapp.ui.AppDatabase
import com.example.fitregisterapp.ui.BilateralExercise
import com.example.fitregisterapp.ui.BilateralExerciseSaver
import com.example.fitregisterapp.ui.UnilateralExercise
import com.example.fitregisterapp.ui.UnilateralExerciseSaver
import com.example.fitregisterapp.ui.components.AutoCompleteInput
import com.example.fitregisterapp.ui.components.SaveFileInFolderButton
import com.example.fitregisterapp.ui.components.SimpleInput
import com.example.fitregisterapp.ui.components.UnilateralInput
import com.example.fitregisterapp.ui.share.DirectoryPicker
import com.example.fitregisterapp.ui.theme.FitRegisterAppTheme
import com.example.fitregisterapp.ui.viewModels.FileNamesViewModel
import com.example.fitregisterapp.ui.viewModels.FileNamesViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FitRegisterAppTheme {
                Scaffold { paddingValues ->
                    App(paddingValues = paddingValues)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(paddingValues: PaddingValues) {
    var exerciseName by remember { mutableStateOf("") }
    var showDirectoryPicker by remember { mutableStateOf(false) }
    var variation by remember { mutableStateOf("") }
    var isUnilateral by remember { mutableStateOf(false) }
    var unilateralReps by remember { mutableStateOf(Pair(0, 0)) }
    var normalReps by remember { mutableStateOf(0) }
    val canSave by remember(exerciseName, variation) {
        derivedStateOf { exerciseName.isNotEmpty() && variation.isNotEmpty() }
    }
    val inputModifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp)
    val database = AppDatabase.getDatabase(LocalContext.current)
    val bilateralExerciseSaver = BilateralExerciseSaver(database.bilateralExerciseDao())
    val unilateralExerciseSaver = UnilateralExerciseSaver(database.unilateralExerciseDao())
    val context = LocalContext.current
    val bilateralExerciseRepository = RoomExerciseRepository(database)
    val unilateralExerciseRepository = RoomUnilateralExerciseRepository(database)
    val fileNamesViewModel: FileNamesViewModel = viewModel(
        factory = FileNamesViewModelFactory(database.fileNameDao())
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp)
    ) {
        Button(
            onClick = { showDirectoryPicker = true },
            modifier = Modifier
                .padding(top = 16.dp, bottom = 24.dp)
                .fillMaxWidth(1f)
                .align(Alignment.CenterHorizontally)
        ) {
            Text("Cargar carpeta de ejercicios")
        }
        Button(
            onClick = {
                CoroutineScope(Dispatchers.IO).launch {
                    val testExercise: BilateralExercise? = database
                        .bilateralExerciseDao()
                        .getAll()
                        .getOrNull(0)

                    val message = testExercise?.let { "${it.name} ${it.variation} ${it.reps}" }
                        ?: "No hay ejercicios registrados"

                    withContext(Dispatchers.Main) {
                        Toast
                            .makeText(context, message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            },
            modifier = Modifier
                .padding(top = 16.dp, bottom = 24.dp)
                .fillMaxWidth(1f)
                .align(Alignment.CenterHorizontally)
        ) {
            Text("Mostrar último dato guardado")
        }
        Button(
            onClick = {
                CoroutineScope(Dispatchers.IO).launch {
                    val testExercise: UnilateralExercise? = database
                        .unilateralExerciseDao()
                        .getAll()
                        .getOrNull(0)

                    val message =
                        testExercise?.let { "${it.name} ${it.variation} derecha:${it.rightReps} izquierda:${it.leftReps}" }
                            ?: "No hay ejercicios registrados"

                    withContext(Dispatchers.Main) {
                        Toast
                            .makeText(context, message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            },
            modifier = Modifier
                .padding(top = 16.dp, bottom = 24.dp)
                .fillMaxWidth(1f)
                .align(Alignment.CenterHorizontally)
        ) {
            Text("Mostrar último dato guardado Unilateral")
        }
        Button(
            onClick = {
                CoroutineScope(Dispatchers.IO).launch {
                    database
                        .bilateralExerciseDao()
                        .deleteAll()
                    database
                        .unilateralExerciseDao()
                        .deleteAll()
                    withContext(Dispatchers.Main) {
                        Toast
                            .makeText(context, "Datos de hoy eliminados", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            },
            modifier = Modifier
                .padding(top = 16.dp, bottom = 24.dp)
                .fillMaxWidth(1f)
                .align(Alignment.CenterHorizontally)
        ) {
            Text("Eliminar todo")
        }

        if (showDirectoryPicker) {
            DirectoryPicker { files ->
                val newFileNames = files
                    ?.mapNotNull { file -> file.name?.replace(".md", "") }
                    ?.ifEmpty { listOf("No se ha cargado la carpeta de ejercicios") }
                    ?: listOf("No se ha cargado la carpeta de ejercicios")
                fileNamesViewModel.saveFileNames(newFileNames)
                showDirectoryPicker = false
            }
        }
        AutoCompleteInput(
            datalist = fileNamesViewModel.fileNames.value,
            onChange = { exerciseName = it },
            modifier = inputModifier
        )
        Spacer(modifier = Modifier.size(15.dp))
        OutlinedTextField(
            value = variation,
            onValueChange = { variation = it },
            label = { Text("Variación") },
            modifier = inputModifier,
            colors = ExposedDropdownMenuDefaults.textFieldColors()
        )
        Spacer(modifier = Modifier.size(15.dp))
        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Checkbox(
                checked = isUnilateral,
                onCheckedChange = { isUnilateral = it })
            Text("Unilateral", modifier = Modifier.align(Alignment.CenterVertically))
        }
        Spacer(modifier = Modifier.size(15.dp))
        if (canSave) {
            if (isUnilateral) UnilateralInput { unilateralReps = it }
            else SimpleInput { normalReps = it }
        }



        if (!isUnilateral) {
            Button(
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 24.dp)
                    .fillMaxWidth(1f)
                    .align(Alignment.CenterHorizontally),
                onClick = {
                    CoroutineScope(Dispatchers.IO).launch {
                        bilateralExerciseSaver.save(exerciseName, variation, normalReps)
                        withContext(Dispatchers.Main) {
                            exerciseName = ""
                            variation = ""
                            normalReps = 0
                            Toast
                                .makeText(
                                    context,
                                    "Ejercicio guardado correctamente",
                                    Toast.LENGTH_SHORT
                                )
                                .show()
                        }
                    }
                }
            ) {
                Text("Guardar Ejercicio")
            }
        } else {
            Button(
                onClick = {
                    CoroutineScope(Dispatchers.IO).launch {
                        unilateralExerciseSaver
                            .save(
                                name = exerciseName,
                                variation = variation,
                                rightReps = unilateralReps.first,
                                leftReps = unilateralReps.second
                            )
                        withContext(Dispatchers.Main) {
                            exerciseName = ""
                            variation = ""
                            unilateralReps = 0 to 0
                            Toast.makeText(
                                context,
                                "Ejercicio Unilateral guardado correctamente",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                }, modifier = Modifier
                    .padding(top = 16.dp, bottom = 24.dp)
                    .fillMaxWidth(1f)
                    .align(Alignment.CenterHorizontally)
            ) {
                Text("Guardar Ejercicio")
            }

        }
        SaveBilateralFileToUserSelectedFolder(bilateralExerciseRepository, unilateralExerciseRepository)
    }
}

@Composable
fun SaveBilateralFileToUserSelectedFolder(
    bilateralExerciseRepository: BilateralExerciseRepository,
    unilateralExerciseRepository: UnilateralExerciseRepository
) {
    var selectedFolderUri by remember { mutableStateOf<Uri?>(null) }
    val directoryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocumentTree()
    ) { uri: Uri? ->
        selectedFolderUri = uri
    }


    Button(onClick = { directoryLauncher.launch(null) }) {
        Text("Seleccionar carpeta")
    }

    if (selectedFolderUri != null) {
        SaveFileInFolderButton(bilateralExerciseRepository, unilateralExerciseRepository, selectedFolderUri!!)
    }
}

fun bilateralExerciseToFileName(exercise: BilateralExercise): String = "${exercise.name} ${exercise.variation} ${exercise.date.toMXFormat()}.md"
fun unilateralExerciseToFileName(exercise: UnilateralExercise): String = "${exercise.name} ${exercise.variation} ${exercise.date.toMXFormat()}.md"

