package com.example.mycmri.Navigation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mycmri.StorageHelper


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiagnosesPage(modifier: Modifier = Modifier, navController: NavController) {
    val context = LocalContext.current
    val storageHelper = remember { StorageHelper(context) }

    var diagnoses by remember { mutableStateOf(storageHelper.loadDiagnoses().ifEmpty { listOf("") }) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("📋 Diagnoses") })
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Enter Diagnoses", fontSize = 24.sp)

            diagnoses.forEachIndexed { index, diagnosis ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextField(
                        value = diagnosis,
                        onValueChange = { newValue ->
                            diagnoses = diagnoses.toMutableList().also {
                                it[index] = newValue
                            }
                        },
                        label = { Text("Diagnosis ${index + 1}") },
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = {
                        diagnoses = diagnoses.toMutableList().also {
                            it.removeAt(index)
                        }
                    }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete Diagnosis")
                    }
                }
            }

            Button(
                onClick = { diagnoses = diagnoses + "" },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("➕ Add New Diagnosis")
            }

            Button(
                onClick = {
                    storageHelper.saveDiagnoses(diagnoses.filter { it.isNotBlank() })
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("💾 Save Diagnoses")
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(onClick = { navController.navigate("homepage") }) {
                Text("🏠 Go to Home Page")
            }
        }
    }
}
