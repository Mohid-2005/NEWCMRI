package com.example.mycmri.ui

import android.content.Context
import android.content.SharedPreferences
import androidx.activity.ComponentActivity
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VaccinesPage(navController: NavController, modifier: Modifier = Modifier, viewModel: VaccineViewModel) {
    val context = LocalContext.current
    val vaccineStates by viewModel.checkedState.collectAsState()

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
                modifier = Modifier.weight(1f)
            ) {
                items(viewModel.vaccines) { vaccine ->
                    val isChecked = vaccineStates[vaccine.docId] ?: false
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Checkbox(
                            checked = isChecked,
                            onCheckedChange = { viewModel.toggleVaccine(vaccine, it)}
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = vaccine.name)
                    }
                }
            }

            Button(
                onClick = { navController.navigate("homepage") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Back to Home")
            }
        }
    }
}

