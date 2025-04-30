package com.example.mycmri.Navigation

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mycmri.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Appointments(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel) {

    val topTabs = listOf("🏠 Home", "📅 Appointments", "⚙️ Settings")
    var selectedTab by remember { mutableStateOf(1) } // Appointments tab selected

    var appointmentText by remember { mutableStateOf(TextFieldValue("")) }
    val appointments = remember { mutableStateListOf<String>() }

    LaunchedEffect(selectedTab) {
        when (selectedTab) {
            0 -> navController.navigate("homepage")
            2 -> navController.navigate("settings")
        }
    }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = { Text("MyCMRI") },
                    navigationIcon = {
                        IconButton(onClick = { navController.navigate("homepage") }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back to Home")
                        }
                    },
                    actions = {
                        TextButton(onClick = { authViewModel.signout() }) {
                            Text("Sign out")
                        }
                    }
                )
                TabRow(selectedTabIndex = selectedTab) {
                    topTabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = {
                                selectedTab = index
                            },
                            text = { Text(title) }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text(text = "Appointments", fontSize = 32.sp)
            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = appointmentText,
                onValueChange = { appointmentText = it },
                label = { Text("Write your appointment details here...") },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Enter your appointment...") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (appointmentText.text.isNotEmpty()) {
                        appointments.add(appointmentText.text)
                        appointmentText = TextFieldValue("")
                        Toast.makeText(navController.context, "Appointment saved!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(navController.context, "Please enter appointment details!", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Appointment")
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (appointments.isEmpty()) {
                Text(text = "No appointments saved yet.", fontSize = 16.sp)
            } else {
                Text(text = "Your Saved Appointments:", fontSize = 20.sp)
                Spacer(modifier = Modifier.height(8.dp))

                appointments.forEachIndexed { index, appointment ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = appointment,
                            fontSize = 16.sp,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(
                            onClick = {
                                appointments.removeAt(index)
                                Toast.makeText(navController.context, "Appointment removed!", Toast.LENGTH_SHORT).show()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete Appointment"
                            )
                        }
                    }
                }
            }
        }
    }
}


