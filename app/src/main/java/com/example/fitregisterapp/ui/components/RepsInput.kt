package com.example.fitregisterapp.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun UnilateralInput(onChange: (Pair<Int, Int>) -> Unit) {
    var rightReps by remember { mutableStateOf(0) }
    var leftReps by remember { mutableStateOf(0) }

    Row(){
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
        ) {
            NumberInput(
                onChange = {
                    rightReps = it
                    onChange(rightReps to leftReps)
                },
                label = { Text("Derecha")})
        }
        Column(
           modifier = Modifier
               .weight(1f)
               .padding(start = 8.dp)
        ) {
            NumberInput(
                onChange = {
                    leftReps = it
                    onChange(rightReps to leftReps)
                },
                label = { Text("Izquierda")})
        }
    }
}

@Composable
fun SimpleInput(onChange: (Int) -> Unit) {
        NumberInput(
            onChange = onChange,
            label = { Text("Repeticiones") },
            modifier = Modifier
                .padding(horizontal = 50.dp)
        )
}