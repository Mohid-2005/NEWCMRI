package com.example.mycmri

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mycmri.Navigation.AllergiesPage
import com.example.mycmri.Navigation.HomePage
import com.example.mycmri.Navigation.LoginPage
import com.example.mycmri.Navigation.SignUp
import com.example.mycmri.Navigation.Appointments
import com.example.mycmri.Navigation.LinkedWebsitesPage
import com.example.mycmri.Navigation.MedicationsPage
import com.example.mycmri.Navigation.ResultsPage
import com.example.mycmri.Navigation.SettingsPage
import com.example.mycmri.Navigation.DiagnosesPage
import com.example.mycmri.Pages.DocumentsPage
import com.example.mycmri.Pages.VaccinesPage
import com.example.mycmri.Navigation.DiagnosesPage


private infix fun Unit.Page(modifier: Modifier) {

}

@Composable
fun MyAppNavigation(modifier: Modifier = Modifier,authViewModel: AuthViewModel) {
    val navController = rememberNavController()

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
            VaccinesPage(navController, modifier)
        }


    })
}




