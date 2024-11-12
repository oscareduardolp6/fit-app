package com.example.fitregisterapp.shared.domain

import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun LocalDate.toMXFormat(): String = this.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))