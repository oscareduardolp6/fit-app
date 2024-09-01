package com.example.fitregisterapp

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.documentfile.provider.DocumentFile
import com.example.fitregisterapp.ui.components.AutoCompleteInput
import com.example.fitregisterapp.ui.share.DirectoryPicker
import com.example.fitregisterapp.ui.theme.FitRegisterAppTheme

class MainActivity : ComponentActivity() {

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FitRegisterAppTheme {
                Scaffold { _ ->
                    App()
                }
            }
        }
    }

    private fun getExercisesNames(): List<String> {
        lateinit var result: List<String>
        registerForActivityResult(ActivityResultContracts.OpenDocumentTree()) {
            result = it?.let {
                DocumentFile
                    .fromTreeUri(this, it)
                    ?.listFiles()
                    ?.mapNotNull { file -> file.name }
                    ?: listOf("No se cargaron los ejercicios")
            } ?: listOf("No se cargaron los ejercicios")
        }
        return result
    }
}

@Composable
fun App() {
    var fileNames by remember { mutableStateOf(emptyList<String>()) }
    var showDirectoryPicker by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(horizontal = 100.dp, vertical = 100.dp)) {
        Button(onClick = { showDirectoryPicker = true }) {
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
        AutoCompleteInput(fileNames)
    }
}

@Composable
fun CustomButton1() {
    val context = LocalContext.current
    var directoryUri by remember { mutableStateOf<Uri?>(null) }
    var exerciseFileNames by remember { mutableStateOf<List<String>?>(null) }

    val directoryPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocumentTree()
    ) { uri: Uri? ->
        directoryUri = uri
    }

    Button(
        onClick = { directoryPickerLauncher.launch(null) },
        colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.padding(100.dp)
    ) {
        Text(text = "Open Directory Picker", color = Color.White)
    }

    directoryUri?.let {
        Text(text = "Selected Directory: $it")
        exerciseFileNames = DocumentFile
            .fromTreeUri(context, it)
            ?.listFiles()
            ?.map { file -> file.name ?: "Archive without name" }
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FitRegisterAppTheme {
        Greeting("Android")
    }
}