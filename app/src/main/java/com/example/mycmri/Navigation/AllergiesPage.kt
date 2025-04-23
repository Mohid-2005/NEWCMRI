package com.example.mycmri.Navigation

import android.content.Context
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
import com.example.mycmri.helpers.StorageHelper3

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllergiesPage(modifier: Modifier = Modifier, navController: NavController) {
    val context = LocalContext.current
    var selectedAllergies by remember { mutableStateOf(StorageHelper3.getAllergies(context).toMutableList()) }
    var customAllergy by remember { mutableStateOf("") }

    val commonAllergies = listOf(
        "Peanuts", "Tree nuts", "Milk", "Eggs", "Fish", "Shellfish", "Wheat", "Soy",
        "Gluten", "Sesame", "Dust mites", "Pollen", "Mold", "Animal dander",
        "Insect stings", "Latex", "Medications", "Perfume", "Citrus", "Nickel"
    )

    fun save() {
        StorageHelper3.saveAllergies(context, selectedAllergies)
    }

    fun toggleAllergy(allergy: String) {
        if (selectedAllergies.contains(allergy)) {
            selectedAllergies.remove(allergy)
        } else {
            selectedAllergies.add(allergy)
        }
        save()
    }

    fun addCustomAllergy() {
        if (customAllergy.isNotBlank()) {
            selectedAllergies.add(customAllergy.trim())
            customAllergy = ""
            save()
        }
    }

    fun deleteAllergy(index: Int) {
        selectedAllergies.removeAt(index)
        save()
    }

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
                            onCheckedChange = { toggleAllergy(allergy) }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(allergy)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text("Other Allergies")
            TextField(
                value = customAllergy,
                onValueChange = { customAllergy = it },
                label = { Text("Add custom allergy") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { addCustomAllergy() }) {
                Text("Add Allergy")
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
                        Button(
                            onClick = { deleteAllergy(index) },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                        ) {
                            Text("Delete")
                        }
                    }
                }
            }
        }
    }
}
