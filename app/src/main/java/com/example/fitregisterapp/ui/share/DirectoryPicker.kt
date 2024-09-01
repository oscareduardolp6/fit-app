package com.example.fitregisterapp.ui.share

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.documentfile.provider.DocumentFile
import kotlinx.coroutines.launch

@Composable
fun DirectoryPicker(onFilesLoaded: (List<DocumentFile>?) -> Unit) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val directoryPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocumentTree()
    ) { uri: Uri? ->
        if (uri != null) {
            coroutineScope.launch {
                val files = getFilesFromDirectory(context, uri)
                onFilesLoaded(files)
            }
        } else {
            onFilesLoaded(emptyList<DocumentFile>())
        }
    }

    LaunchedEffect(Unit) {
        directoryPickerLauncher.launch(null)
    }
}

private fun getFilesFromDirectory(context: Context, uri: Uri): List<DocumentFile>? {
    val documentFile = DocumentFile.fromTreeUri(context, uri)
    return documentFile?.listFiles()?.toList()
}
