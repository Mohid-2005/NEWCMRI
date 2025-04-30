package com.example.mycmri.Pages

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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

    val context = LocalContext.current

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

    val sharedPreferences: SharedPreferences = context.getSharedPreferences("VaccinePreferences", Context.MODE_PRIVATE)
    val savedVaccineStatus = remember { mutableStateOf(loadVaccineStatus(sharedPreferences, allVaccines)) }

    fun saveVaccineStatus(vaccine: String, isChecked: Boolean) {
        savedVaccineStatus.value = savedVaccineStatus.value.toMutableMap().apply {
            this[vaccine] = isChecked
        }
        sharedPreferences.edit().putBoolean(vaccine, isChecked).apply()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("💉 Vaccines") },
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
                .fillMaxSize()
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(allVaccines) { vaccine ->
                    val isChecked = savedVaccineStatus.value[vaccine] ?: false
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Checkbox(
                            checked = isChecked,
                            onCheckedChange = { checked ->
                                saveVaccineStatus(vaccine, checked)
                            }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = vaccine)
                    }
                }
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

