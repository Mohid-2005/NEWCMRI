package com.example.mycmri.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mycmri.AuthViewModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

data class Appointment(val name: String = "", val date: Timestamp = Timestamp.now())

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Appointments(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val topTabs = listOf("🏠 Home", "📅 Appointments", "⚙️ Settings")
    var selectedTab by remember { mutableStateOf(1) }

    val appointments = remember { mutableStateListOf<Appointment>() }
    val context = navController.context

    // Load appointments from Firestore
    LaunchedEffect(Unit) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId == null) {
            Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show()
            return@LaunchedEffect
        }

        FirebaseFirestore.getInstance()
            .collection("patient")
            .document(userId)
            .collection("appointment")
            .get()
            .addOnSuccessListener { docs ->
                appointments.clear()
                for (doc in docs) {
                    val name = doc.getString("name") ?: ""
                    val date = doc.getTimestamp("date") ?: Timestamp.now()
                    appointments.add(Appointment(name, date))
                }
            }
            .addOnFailureListener {
                Toast.makeText(context, "Failed to load appointments", Toast.LENGTH_SHORT).show()
            }
    }

    // Navigation on tab switch
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
            Text(text = "Appointments", fontSize = 32.sp)
            Spacer(modifier = Modifier.height(16.dp))

            if (appointments.isEmpty()) {
                Text("No appointments available.", fontSize = 16.sp)
            } else {
                appointments.forEach { appointment ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = appointment.name,
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                text = formatTimestamp(appointment.date),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Formats Firestore timestamp
 */
fun formatTimestamp(timestamp: Timestamp): String {
    val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
    return sdf.format(timestamp.toDate())
}
