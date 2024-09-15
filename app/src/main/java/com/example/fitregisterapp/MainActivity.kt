package com.example.fitregisterapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.fitregisterapp.ui.components.AutoCompleteInput
import com.example.fitregisterapp.ui.components.SimpleInput
import com.example.fitregisterapp.ui.components.UnilateralInput
import com.example.fitregisterapp.ui.share.DirectoryPicker
import com.example.fitregisterapp.ui.theme.FitRegisterAppTheme

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
    var fileNames by remember { mutableStateOf(emptyList<String>()) }
    var exerciseName by remember { mutableStateOf("") }
    var showDirectoryPicker by remember { mutableStateOf(false) }
    var variation by remember { mutableStateOf("") }
    var isUnilateral by remember { mutableStateOf(false) }
    var unilateralReps by remember { mutableStateOf(Pair(0, 0)) }
    var normalReps by remember { mutableStateOf(0) }
    val inputModifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp)) {
        Button(
            onClick = { showDirectoryPicker = true },
            modifier = Modifier
                .padding(top = 16.dp, bottom = 24.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Text("Cargar carpeta de ejercicios")
        }
        if(showDirectoryPicker) {
            DirectoryPicker { files ->
                fileNames = files
                    ?.mapNotNull { file -> file.name?.replace(".md", "") }
                    ?.ifEmpty { listOf("No se ha cargado la carpeta de ejercicios") }
                    ?: listOf("No se ha cargado la carpeta de ejercicios")
                showDirectoryPicker = false
            }
        }
        AutoCompleteInput(
            datalist = fileNames,
            onChange = { exerciseName = it },
            modifier = inputModifier)
        Spacer(modifier = Modifier.size(15.dp))
        OutlinedTextField(
            value = variation ,
            onValueChange = { variation = it},
            label = { Text("Variación")},
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
        if(exerciseName.isNotEmpty() && variation.isNotEmpty()) {
            if(isUnilateral) UnilateralInput { unilateralReps = it }
            else SimpleInput { normalReps = it }
        }
    }
}