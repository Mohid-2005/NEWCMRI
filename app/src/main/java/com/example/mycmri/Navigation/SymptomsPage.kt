package com.example.mycmri.Navigation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
fun SymptomsPage(modifier: Modifier = Modifier, navController: NavController) {
    val context = LocalContext.current
    val storageHelper = remember { StorageHelper(context) }

    var symptoms by remember { mutableStateOf(storageHelper.loadDiagnoses().ifEmpty { listOf("") }) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("📋 Symptoms") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("homepage") }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back to Home")
                    }
                }
            )
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
            Text("Enter Symptoms", fontSize = 24.sp)

            symptoms.forEachIndexed { index, symptom ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextField(
                        value = symptom,
                        onValueChange = { newValue ->
                            symptoms = symptoms.toMutableList().also {
                                it[index] = newValue
                            }
                        },
                        label = { Text("Symptom ${index + 1}") },
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = {
                        symptoms = symptoms.toMutableList().also {
                            it.removeAt(index)
                        }
                    }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete Symptom")
                    }
                }
            }

            Button(
                onClick = { symptoms = symptoms + "" },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("➕ Add New Symptom")
            }

            Button(
                onClick = {
                    storageHelper.saveDiagnoses(symptoms.filter { it.isNotBlank() })
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("💾 Save Symptoms")
            }
        }
    }
}
