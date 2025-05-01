package com.example.mycmri.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mycmri.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsPage(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel) {

    val topTabs = listOf("🏠 Home", "📅 Appointments", "⚙️ Settings")
    var selectedTab by remember { mutableStateOf(2) } // Settings tab selected

    // Manage the input fields for the settings
    var username by remember { mutableStateOf(TextFieldValue("User123")) }
    var email by remember { mutableStateOf(TextFieldValue("user123@example.com")) }
    var notificationsEnabled by remember { mutableStateOf(true) }

    // Handle the ui when the user changes the tab
    LaunchedEffect(selectedTab) {
        when (selectedTab) {
            0 -> navController.navigate("homepage") // Navigate to HomePage
            1 -> navController.navigate("appointments") // Navigate to AppointmentsPage
        }
    }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = { Text("MyCMRI Settings") },
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
                            onClick = { selectedTab = index },
                            text = { Text(title) }
                        )
                    }
                }
            }
        },
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text(text = "Settings", fontSize = 32.sp)
            Spacer(modifier = Modifier.height(16.dp))

            // Change Username
            Text(text = "Username", fontSize = 18.sp)
            TextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Enter your new username") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Change Email
            Text(text = "Email", fontSize = 18.sp)
            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Enter your new email") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Enable/Disable Notifications
            Text(text = "Notifications", fontSize = 18.sp)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Enable notifications", fontSize = 16.sp)
                Spacer(modifier = Modifier.width(16.dp))
                Switch(
                    checked = notificationsEnabled,
                    onCheckedChange = { notificationsEnabled = it }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Save Settings Button
            Button(
                onClick = {
                    // Save settings logic goes here (e.g., save to ViewModel, API, or local storage)
                    // For now, we'll just show a confirmation message.
                    Toast.makeText(navController.context, "Settings saved!", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Settings")
            }
        }
    }
}
