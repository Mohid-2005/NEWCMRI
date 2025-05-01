package com.example.mycmri.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mycmri.AuthState
import com.example.mycmri.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel) {

    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current

    LaunchedEffect(authState.value) {
        if (authState.value is AuthState.Unauthenticated) {
            navController.navigate("login")
        }
    }

    // 👇 Retrieve username from SharedPreferences
    val username = remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        username.value = StorageHelper.getUsername(context) ?: "User"
    }
    StorageHelper.saveUsername(context, "User")


    val categories = listOf(
        "📋 Diagnoses",
        "⚠️ Allergies",
        "💊 Medications",
        "📊 Results",
        "📂 Documents",
        "💉 Vaccines"
    )

    val topTabs = listOf("🏠 Home", "📅 Appointments", "⚙️ Settings")
    var selectedTab by remember { mutableStateOf(0) }

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
                                when (index) {
                                    1 -> navController.navigate("appointments")
                                    2 -> navController.navigate("settings")
                                }
                            },
                            text = { Text(title) }
                        )
                    }
                }
            }
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("linked_websites") },
                    icon = { Text("🌐") },
                    label = { Text("Websites") }
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            // 👇 Updated welcome message
            Text(text = "Welcome, ${username.value} 👋", fontSize = 24.sp)
            Spacer(modifier = Modifier.height(16.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(categories) { item ->
                    Button(
                        onClick = {
                            when (item) {
                                "📋 Diagnoses" -> navController.navigate("diagnoses")
                                "⚠️ Allergies" -> navController.navigate("allergies")
                                "💊 Medications" -> navController.navigate("medications")
                                "📊 Results" -> navController.navigate("results")
                                "📂 Documents" -> navController.navigate("documents")
                                "💉 Vaccines" -> navController.navigate("vaccines")
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        elevation = ButtonDefaults.buttonElevation(4.dp)
                    ) {
                        Text(text = item, color = Color.White)
                    }
                }
            }
        }
    }
}
