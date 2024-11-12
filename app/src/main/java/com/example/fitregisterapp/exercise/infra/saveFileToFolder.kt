package com.example.fitregisterapp.exercise.infra

import android.content.Context
import android.net.Uri
import android.provider.DocumentsContract

data class FileInfo(
    val name: String,
    val content: String,
    val folderUri: Uri
)

fun saveFileToSelectedFolder(context: Context, fileInfo: FileInfo) {
    val resolver = context.contentResolver
    val documentUri = DocumentsContract
        .buildDocumentUriUsingTree(
            fileInfo.folderUri,
            DocumentsContract.getTreeDocumentId(fileInfo.folderUri)
        )
    val fileUri = DocumentsContract.createDocument(
        resolver,
        documentUri,
        "text/markdown",
        fileInfo.name
    )

    fileUri?.let { uri ->
        resolver.openOutputStream(uri)?.use { outputStream ->
            outputStream.write(fileInfo.content.toByteArray())
        }
    }
}