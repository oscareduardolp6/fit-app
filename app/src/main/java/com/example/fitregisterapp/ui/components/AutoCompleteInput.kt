package com.example.fitregisterapp.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AutoCompleteInput(datalist: List<String>, onChange: (String) -> Unit,  modifier: Modifier = Modifier, limit: Int = 3) {
    var expanded by remember { mutableStateOf(false) }
    var query by remember { mutableStateOf("") }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("Nombre Ejercicio")},
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors()
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            datalist
                .filter { option -> option.contains(query) }
                .take(limit)
                .forEach { selectedOption ->
                DropdownMenuItem(
                    text = { Text(selectedOption) },
                    onClick = {
                        query = selectedOption
                        expanded = false
                        onChange(query)
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding)
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun AutoCompleteInputPreview(){
    val testList = listOf("Dominadas Prono", "Lagartijas", "Sentadillas")
    AutoCompleteInput(datalist = testList, onChange = { })
}