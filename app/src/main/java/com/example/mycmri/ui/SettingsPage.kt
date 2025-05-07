package com.example.mycmri.ui

import android.util.Log
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.platform.LocalContext
import com.example.mycmri.AuthViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsPage(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val context = LocalContext.current
    val topTabs = listOf("🏠 Home", "📅 Appointments", "⚙️ Settings")
    var selectedTab by remember { mutableStateOf(2) }

    // TextField state and initial values
    var username by remember { mutableStateOf(TextFieldValue("")) }
    var email by remember { mutableStateOf(TextFieldValue("")) }
    var initialUsername by remember { mutableStateOf("") }
    var initialEmail by remember { mutableStateOf("") }
    var notificationsEnabled by remember { mutableStateOf(true) }

    val user = FirebaseAuth.getInstance().currentUser
    val uid = user?.uid
    val db = FirebaseFirestore.getInstance()

    // Load current values on first composition
    LaunchedEffect(Unit) {
        // Load email from Auth
        user?.email?.let { e ->
            email = TextFieldValue(e)
            initialEmail = e
        }
        // Load username from Firestore
        if (uid != null) {
            db.collection("patient").document(uid).get()
                .addOnSuccessListener { snap ->
                    val uname = snap.getString("username") ?: ""
                    username = TextFieldValue(uname)
                    initialUsername = uname
                }
                .addOnFailureListener { e ->
                    Log.e("SettingsPage", "Failed to load username", e)
                }
        }
    }

    // Handle tab navigation
    LaunchedEffect(selectedTab) {
        when (selectedTab) {
            0 -> navController.navigate("homepage")
            1 -> navController.navigate("appointments")
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
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text(text = "Settings", fontSize = 32.sp)
            Spacer(modifier = Modifier.height(16.dp))

            // Username
            Text(text = "Username", fontSize = 18.sp)
            TextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Enter your new username") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Email
            Text(text = "Email", fontSize = 18.sp)
            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Enter your new email") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Notifications toggle
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

            // Save Settings
            Button(
                onClick = {
                    if (uid == null) {
                        Toast.makeText(context, "Not signed in", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    val newEmail = email.text.trim()
                    val newUsername = username.text.trim()
                    var updated = false

                    // Update email if changed
                    if (newEmail.isNotBlank() && newEmail != initialEmail) {
                        user?.verifyBeforeUpdateEmail(newEmail)
                            ?.addOnSuccessListener {
                                Toast.makeText(context, "Verification sent to $newEmail", Toast.LENGTH_LONG).show()
                            }
                            ?.addOnFailureListener { e ->
                                Log.e("SettingsPage", "Email update failed", e)
                                Toast.makeText(context, "Email update failed: ${e.message}", Toast.LENGTH_LONG).show()
                            }
                        updated = true
                    }

                    // Update username if changed
                    if (newUsername.isNotBlank() && newUsername != initialUsername) {
                        db.collection("patient").document(uid)
                            .update("username", newUsername)
                            .addOnSuccessListener {
                                Toast.makeText(context, "Username updated", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener { e ->
                                Log.e("SettingsPage", "Username update failed", e)
                                Toast.makeText(context, "Username update failed", Toast.LENGTH_SHORT).show()
                            }
                        updated = true
                    }

                    if (!updated) {
                        Toast.makeText(context, "Nothing to update", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Settings")
            }
        }
    }
}