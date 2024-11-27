package com.example.fitregisterapp.ui.viewModels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.fitregisterapp.ui.FileName
import com.example.fitregisterapp.ui.FileNameDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FileNamesViewModel(private val fileNameDao: FileNameDao): ViewModel() {
    var fileNames = mutableStateOf<List<String>>(emptyList())
        private set

    init {
        loadFileNames()
    }

    fun saveFileNames(newFileNames: List<String>) = viewModelScope
        .launch(Dispatchers.IO) {
            fileNameDao.deleteAll()
            newFileNames
                .map(::FileName)
                .forEach { fileNameDao.upsertFileName(it) }
            withContext(Dispatchers.Main) {
                fileNames.value = newFileNames
            }
        }

    private fun loadFileNames() = viewModelScope
        .launch(Dispatchers.IO) {
            val loadedFileNames = fileNameDao
                .getAllFileNames()
                .map { it.name }
            withContext(Dispatchers.Main) {
                fileNames.value = loadedFileNames
            }
        }
}

class FileNamesViewModelFactory(private val fileNameDao: FileNameDao): ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(FileNamesViewModel::class.java))
            @Suppress("UNCHECKED_CAST")
            return FileNamesViewModel(fileNameDao) as T
        throw  IllegalArgumentException("Unknown Viewmodel class")
    }
}