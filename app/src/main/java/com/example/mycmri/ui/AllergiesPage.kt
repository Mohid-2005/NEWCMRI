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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllergiesPage(
    modifier: Modifier = Modifier,
    navController: NavController,
    @SuppressLint("ContextCastToActivity") viewModel: AllergyViewModel = viewModel(LocalContext.current as ComponentActivity)
) {
    val selectedAllergies by viewModel.selectedAllergies.collectAsState()
    var customAllergy by remember { mutableStateOf("") }
    var showCustomInput by remember { mutableStateOf(false) }

    val commonAllergies = listOf("Peanut", "Egg", "Fish", "Gluten")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("⚠️ Allergies") },
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
            Text("Common Allergies", style = MaterialTheme.typography.titleMedium)

            LazyColumn {
                itemsIndexed(commonAllergies) { _, allergy ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = selectedAllergies.contains(allergy),
                            onCheckedChange = { viewModel.toggleAllergy(allergy) }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(allergy)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { showCustomInput = !showCustomInput }) {
                Text(if (showCustomInput) "Cancel" else "Other Allergens")
            }

            if (showCustomInput) {
                Spacer(modifier = Modifier.height(12.dp))
                TextField(
                    value = customAllergy,
                    onValueChange = { customAllergy = it },
                    label = { Text("Enter custom allergen") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = {
                    viewModel.addCustomAllergy(customAllergy)
                    customAllergy = ""
                    showCustomInput = false
                }) {
                    Text("Add Allergy")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text("Saved Allergies", style = MaterialTheme.typography.titleMedium)

            LazyColumn {
                itemsIndexed(selectedAllergies) { index, allergy ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("• $allergy")
                        IconButton(onClick = { viewModel.removeAllergyAt(index) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete Allergy")
                        }
                    }
                }
            }
        }
    }
}

