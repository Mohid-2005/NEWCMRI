package com.example.mycmri.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mycmri.data.Medication
import com.example.mycmri.viewmodels.MedicationData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicationsPage(modifier: Modifier = Modifier, navController: NavController) {
    val context = LocalContext.current
    var medicationList by remember { mutableStateOf(listOf<Medication>()) }

    LaunchedEffect(Unit) {
        MedicationData.getMedications(context) { fetchedList ->
            medicationList = fetchedList
        }
    }

    var medName by remember { mutableStateOf("") }
    var timesPerDay by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("") }

    fun save() {
        if (medName.isNotBlank() && timesPerDay.isNotBlank() && duration.isNotBlank()) {
            val newMed = Medication("" ,medName, timesPerDay, duration)
            MedicationData.saveMedication(context, newMed)
            medName = ""
            timesPerDay = ""
            duration = ""
        }
    }

    fun delete(index: Int) {
        val med = medicationList[index]
        MedicationData.deleteMedication(context, med.id)

        // Creates a new list that is a copy of the original. Allows modication of the list, i.e.
        // removal of the Medication at the given index. Triggers Compose to recompose UI
        medicationList = medicationList.toMutableList().also {
            it.removeAt(index)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("💊 Medications") },
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
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {
            TextField(
                value = medName,
                onValueChange = { medName = it },
                label = { Text("Medication Name") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = timesPerDay,
                onValueChange = { timesPerDay = it },
                label = { Text("Times per Day") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = duration,
                onValueChange = { duration = it },
                label = { Text("Duration (days)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { save() },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("💾 Save Medication")
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("Saved Medications", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn {
                itemsIndexed(medicationList) { index, med ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("• ${med.name} - ${med.frequency}x/day for ${med.duration} days")
                        }
                        Button(
                            onClick = { delete(index) },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                        ) {
                            Text("Delete")
                        }
                    }
                    Divider()
                }
            }
        }
    }
}