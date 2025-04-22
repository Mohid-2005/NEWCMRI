package com.example.mycmri.Pages

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocumentsPage(navController: NavController, modifier: Modifier = Modifier) {

    // Context and file selection result launcher
    val context = LocalContext.current
    var selectedFileUri by remember { mutableStateOf<Uri?>(null) }
    val filePicker: ActivityResultLauncher<String> = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            selectedFileUri = uri
        }
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("📂 Documents") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            // Button to pick a file
            Button(
                onClick = { filePicker.launch("*/*") }, // Allow all types of files
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Upload Document")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Display selected file path if any
            selectedFileUri?.let {
                val fileName = getFileNameFromUri(context, it)
                Text(text = "Selected file: $fileName")
            }

            // Button to navigate back to Home
            Button(
                onClick = { navController.navigate("homepage") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Back to Home")
            }
        }
    }
}

// Helper function to extract the file name from the URI
fun getFileNameFromUri(context: Context, uri: Uri): String {
    val cursor = context.contentResolver.query(uri, null, null, null, null)
    cursor?.let {
        val columnIndex = it.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
        it.moveToFirst()
        val fileName = it.getString(columnIndex)
        it.close()
        return fileName
    }
    return uri.path?.let { File(it).name } ?: "Unknown File"
}

@Preview
@Composable
fun PreviewDocumentsPage() {
    // Passing a mock NavController to preview
    DocumentsPage(navController = NavController(LocalContext.current))
}


