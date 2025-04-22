package com.example.mycmri.Pages

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VaccinesPage(navController: NavController, modifier: Modifier = Modifier) {

    // Access context using LocalContext.current
    val context = LocalContext.current

    // List of vaccines available in Ireland
    val allVaccines = listOf(
        "COVID-19 Vaccine",
        "Influenza (Flu) Vaccine",
        "Hepatitis B",
        "Tetanus",
        "MMR (Measles, Mumps, Rubella)",
        "HPV (Human Papillomavirus)",
        "Polio Vaccine",
        "Varicella (Chickenpox)",
        "Meningococcal Vaccine",
        "Pneumococcal Vaccine"
    )

    // SharedPreferences setup
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("VaccinePreferences", Context.MODE_PRIVATE)

    // Read stored vaccine statuses from SharedPreferences
    val savedVaccineStatus = remember { mutableStateOf(loadVaccineStatus(sharedPreferences, allVaccines)) }

    // Update SharedPreferences when a vaccine status changes
    fun saveVaccineStatus(vaccine: String, isChecked: Boolean) {
        savedVaccineStatus.value = savedVaccineStatus.value.toMutableMap().apply {
            this[vaccine] = isChecked
        }
        sharedPreferences.edit().putBoolean(vaccine, isChecked).apply()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("💉 Vaccines") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            // List of vaccines with checkboxes to indicate if the user has taken the vaccine
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(allVaccines) { vaccine ->
                    // Toggle state for each vaccine
                    val isChecked = savedVaccineStatus.value[vaccine] ?: false
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Checkbox(
                            checked = isChecked,
                            onCheckedChange = { checked ->
                                // Save the vaccine status when the checkbox is clicked
                                saveVaccineStatus(vaccine, checked)
                            }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = vaccine)
                    }
                }
            }

            // Button to navigate back to the Home Page
            Button(
                onClick = { navController.navigate("homepage") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Back to Home")
            }
        }
    }
}

// Function to load vaccine status from SharedPreferences
fun loadVaccineStatus(sharedPreferences: SharedPreferences, allVaccines: List<String>): Map<String, Boolean> {
    val vaccineStatus = mutableMapOf<String, Boolean>()
    allVaccines.forEach { vaccine ->
        vaccineStatus[vaccine] = sharedPreferences.getBoolean(vaccine, false) // Default to false if not found
    }
    return vaccineStatus
}

@Preview
@Composable
fun PreviewVaccinesPage() {
    // Pass NavController for preview purposes (you can set a mock one if needed)
    VaccinesPage(navController = NavController(LocalContext.current))
}

