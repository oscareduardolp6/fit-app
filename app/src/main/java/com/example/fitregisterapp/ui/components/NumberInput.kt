package com.example.fitregisterapp.ui.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun NumberInput(
    onChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    initialValue: Int = 0,
    label: @Composable() (() -> Unit)? = null
) {
    var numberText by remember {
        val value = if(initialValue == 0) "" else initialValue.toString()
        mutableStateOf(value)
    }

    OutlinedTextField(
        value = numberText,
        onValueChange = onValueChange@{ newValue ->
            if(newValue.isNotEmpty() || newValue.matches(Regex("^\\d*\\.?\\d*\$")))
                return@onValueChange
            numberText = newValue
            onChange(newValue.toIntOrNull() ?: 0)
        },
        label = label,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number
        ),
        modifier = modifier
    )
}