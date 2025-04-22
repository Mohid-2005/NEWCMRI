package com.example.mycmri.Navigation

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import com.example.mycmri.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Appointments(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel) {

    val topTabs = listOf("🏠 Home", "📅 Appointments", "⚙️ Settings")
    var selectedTab by remember { mutableStateOf(1) } // Appointments tab selected

    // State to manage the TextField input
    var appointmentText by remember { mutableStateOf(TextFieldValue("")) }

    // State for the list of appointments (this will be cleared when the app is closed)
    val appointments = remember { mutableStateListOf<String>() }

    // Handle the navigation when the user changes the tab
    LaunchedEffect(selectedTab) {
        when (selectedTab) {
            0 -> navController.navigate("homepage") // Navigate to HomePage
            2 -> navController.navigate("settings") // Navigate to SettingsPage
        }
    }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = { Text("MyCMRI") },
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
        },
        // Removed bottomBar
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text(text = "Appointments", fontSize = 32.sp)
            Spacer(modifier = Modifier.height(16.dp))

            // Appointment TextField
            TextField(
                value = appointmentText,
                onValueChange = { appointmentText = it },
                label = { Text("Write your appointment details here...") },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Enter your appointment...") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Button to save the appointment
            Button(
                onClick = {
                    // Add appointment to the list when the button is clicked
                    if (appointmentText.text.isNotEmpty()) {
                        appointments.add(appointmentText.text)
                        appointmentText = TextFieldValue("") // Reset the TextField after saving
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

            // Display current session saved appointments
            if (appointments.isEmpty()) {
                Text(text = "No appointments saved yet.", fontSize = 16.sp)
            } else {
                Text(text = "Your Saved Appointments:", fontSize = 20.sp)
                Spacer(modifier = Modifier.height(8.dp))

                // Display the saved appointments in a list
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
                                // Remove the selected appointment from the list
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

