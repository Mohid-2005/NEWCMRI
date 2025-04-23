package com.example.mycmri.Navigation

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mycmri.helpers.StorageHelper2

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicationsPage(modifier: Modifier = Modifier, navController: NavController) {
    val context = LocalContext.current
    var medicationList by remember { mutableStateOf(StorageHelper2.getMedications(context).toMutableList()) }

    var medName by remember { mutableStateOf("") }
    var timesPerDay by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("") }

    fun save() {
        if (medName.isNotBlank() && timesPerDay.isNotBlank() && duration.isNotBlank()) {
            medicationList.add(Triple(medName, timesPerDay, duration))
            StorageHelper2.saveMedications(context, medicationList)
            medName = ""
            timesPerDay = ""
            duration = ""
        }
    }

    fun delete(index: Int) {
        medicationList.removeAt(index)
        StorageHelper2.saveMedications(context, medicationList)
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("💊 Medications") })
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
                            Text("• ${med.first} - ${med.second}x/day for ${med.third} days")
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

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { navController.navigate("homepage") }) {
                Text("🏠 Go to Home Page")
            }
        }
    }
}