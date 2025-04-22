package com.example.mycmri.Navigation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultsPage(navController: NavController, modifier: Modifier = Modifier) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("📊 Results") },
                actions = {
                    TextButton(onClick = { navController.navigate("homepage") }) {
                        Text("Go Home")
                    }
                }
            )
        }
    ) { paddingValues ->

        val categories = listOf(
            "🩸 Blood Test",
            "🧬 Genetic Test",
            "🦠 COVID Test",
            "🧪 Urine Test"
        )

        val inputStates = remember { mutableStateMapOf<String, String>() }
        val expandedStates = remember { mutableStateMapOf<String, Boolean>() }

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            categories.forEach { category ->
                var input by remember { mutableStateOf("") }
                var expanded by remember { mutableStateOf(false) }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { expanded = !expanded }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = category, fontSize = 18.sp)

                        if (expanded) {
                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = input,
                                onValueChange = { input = it },
                                label = { Text("Enter $category results") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Button(onClick = {
                                inputStates[category] = input
                            }) {
                                Text("Save")
                            }

                            if (inputStates.containsKey(category)) {
                                Text(
                                    text = "Saved: ${inputStates[category]}",
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


