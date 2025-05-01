package com.example.mycmri

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mycmri.ui.AllergiesPage
import com.example.mycmri.ui.HomePage
import com.example.mycmri.ui.LoginPage
import com.example.mycmri.ui.SignUp
import com.example.mycmri.ui.Appointments
import com.example.mycmri.ui.LinkedWebsitesPage
import com.example.mycmri.ui.MedicationsPage
import com.example.mycmri.ui.ResultsPage
import com.example.mycmri.ui.SettingsPage
import com.example.mycmri.ui.DiagnosesPage
import com.example.mycmri.Pages.DocumentsPage
import com.example.mycmri.ui.VaccinesPage
import com.example.mycmri.ui.VaccineViewModel

@Composable
fun MyAppNavigation(modifier: Modifier = Modifier,authViewModel: AuthViewModel) {
    val navController = rememberNavController()
    val vaccineViewModel: VaccineViewModel = viewModel()

    NavHost(navController = navController, startDestination = "login", builder = {
        composable("login"){
            LoginPage(modifier,navController, authViewModel)
        }
        composable("signup"){
            SignUp(modifier,navController, authViewModel)
        }
        composable("homepage"){
            HomePage(modifier,navController, authViewModel)
        }
        composable("appointments"){
            Appointments(modifier,navController, authViewModel)
        }
        composable("settings") {
            SettingsPage(modifier, navController, authViewModel)
        }
        composable("linked_websites") {  // Linked Websites route
            LinkedWebsitesPage(modifier, navController)
        }
        composable("diagnoses") {
            DiagnosesPage(modifier, navController)
        }

        composable("allergies") {
            AllergiesPage(modifier, navController)
        }
        composable("medications") {
            MedicationsPage(modifier, navController)
        }
        composable("results") {
            ResultsPage(navController, modifier)
        }
        composable("documents") {
            DocumentsPage(navController, modifier)
        }
        composable("vaccines") {
            VaccinesPage(navController, modifier, vaccineViewModel)
        }


    })
}




