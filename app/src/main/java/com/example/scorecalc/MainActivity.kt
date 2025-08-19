package com.example.scorecalc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    ScoreCalculator()
                }
            }
        }
    }
}

@Composable
fun ScoreCalculator() {
    var score by rememberSaveable { mutableStateOf(0L) }
    var textField by rememberSaveable { mutableStateOf("") }
    var inputError by rememberSaveable { mutableStateOf(false) }

    val increments = listOf(1L, 2L, 5L, 10L, 20L, 50L, 100L)

    fun setFromText() {
        val trimmed = textField.trim()
        val value = trimmed.toLongOrNull()
        if (value != null) {
            score = value
            inputError = false
        } else {
            inputError = true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Add", style = MaterialTheme.typography.titleMedium)
        FlowRowButtons(values = increments, prefix = "+") { delta -> score += delta }

        // Big score in the middle
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = score.toString(),
                fontSize = 64.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }

        Text(text = "Subtract", style = MaterialTheme.typography.titleMedium)
        FlowRowButtons(values = increments, prefix = "-") { delta -> score -= delta }

        // Bottom actions: set any score + reset to zero
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = textField,
                onValueChange = {
                    textField = it
                    if (inputError) inputError = false
                },
                label = { Text("Enter any score (e.g. -42, 1000)") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = inputError,
                supportingText = {
                    if (inputError) Text("Please enter a whole number.")
                },
                modifier = Modifier.fillMaxWidth()
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = { setFromText() }, modifier = Modifier.weight(1f)) {
                    Text("Set Score")
                }
                OutlinedButton(
                    onClick = { score = 0; textField = "" },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Refresh (Zero)")
                }
            }
        }
    }
}

@Composable
private fun FlowRowButtons(
    values: List<Long>,
    prefix: String,
    onClick: (Long) -> Unit
) {
    androidx.compose.foundation.layout.FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        maxItemsInEachRow = 4,
        modifier = Modifier.fillMaxWidth()
    ) {
        values.forEach { v ->
            Button(onClick = { onClick(v) }, modifier = Modifier.weight(1f, fill = false)) {
                Text(text = "$prefix$v")
            }
        }
    }
}
