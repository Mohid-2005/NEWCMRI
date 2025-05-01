package com.example.mycmri.ui
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LinkedWebsitesPage(modifier: Modifier = Modifier, navController: NavController) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Linked Websites") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text("Here is the link to HSE:")
            Spacer(modifier = Modifier.padding(8.dp))

            // Clickable text that opens the URL
            TextButton(onClick = {
                // Open the URL in the browser
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.hse.ie"))
                context.startActivity(intent)
            }) {
                Text(
                    text = "Visit HSE.ie",
                    style = MaterialTheme.typography.bodyLarge.copy(textDecoration = TextDecoration.Underline)
                )
            }
        }
    }
}
