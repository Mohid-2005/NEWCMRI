package com.example.mycmri.ui

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mycmri.viewmodels.DiagnosisViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiagnosesPage(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val viewModel: DiagnosisViewModel = viewModel()
    val available by viewModel.availableDiagnoses.collectAsState()
    val selected by viewModel.selectedDiagnoses.collectAsState()

    // Track dropdown expanded state per index
    val expandedMap = remember { mutableStateMapOf<Int, Boolean>() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("📋 Symptoms") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("homepage") }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(text = "Select Symptoms/Diagnoses", style = MaterialTheme.typography.titleMedium)

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                itemsIndexed(selected) { index, docId ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Dropdown for each diagnosis entry
                        ExposedDropdownMenuBox(
                            expanded = expandedMap.getOrPut(index) { false },
                            onExpandedChange = { expandedMap[index] = it },
                            modifier = Modifier.weight(1f)
                        ) {
                            TextField(
                                value = available.find { it.docId == docId }?.name ?: "Select diagnosis",
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Diagnosis ${index + 1}") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandedMap[index]!!) },
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth()
                            )
                            ExposedDropdownMenu(
                                expanded = expandedMap[index]!!,
                                onDismissRequest = { expandedMap[index] = false }
                            ) {
                                available.forEach { option ->
                                    DropdownMenuItem(
                                        text = { Text(option.name) },
                                        onClick = {
                                            viewModel.replaceDiagnosisAt(index, option.docId)
                                            expandedMap[index] = false
                                        }
                                    )
                                }
                            }
                        }

                        IconButton(onClick = { viewModel.removeDiagnosisAt(index) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Remove")
                        }
                    }
                }
            }

            Button(
                onClick = { viewModel.addEmptyDiagnosis() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "➕ Add New Symptoms/Diagnosis")
            }
        }
    }
}
