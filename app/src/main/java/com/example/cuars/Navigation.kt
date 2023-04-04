package com.example.cuars

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.cuars.firstAid.FirstAidScreen
import com.example.cuars.form.IncidentForm
import com.example.cuars.form.IncidentForm2
import com.example.cuars.incidents.IncidentsScreen
import com.example.cuars.models.SharedViewModel
import com.example.cuars.viewModels.DetailViewModel
import com.example.cuars.viewModels.FirstAidViewModel
import com.example.cuars.viewModels.IncidentsViewModel

@Composable
fun NavigationMap(detailViewModel: DetailViewModel) {
    val navController = rememberNavController()
    val sharedViewModel: SharedViewModel = viewModel()
    NavHost(navController = navController, startDestination = Screen.IncidentsScreen.route) {
        composable(
            route = Screen.IncidentsScreen.route
        ) {
            IncidentsScreen(
                navController = navController,
                incidentsViewModel = IncidentsViewModel(),
                sharedViewModel = sharedViewModel,
                detailViewModel = detailViewModel
            )
        }

        composable(
            route = Screen.AidScreen.route
        ) {
            FirstAidScreen(
                navController = navController,
                incidentsViewModel = FirstAidViewModel(),
                detailViewModel = detailViewModel
            )
        }

        composable(
            route = Screen.FormScreenAdd.route,
            arguments = listOf(navArgument("incident") {
                type = NavType.StringType
                defaultValue = ""
            })
        ) {
            IncidentForm(
                context = LocalContext.current,
                detailViewModel = detailViewModel,
                navController = navController
            )
        }

        composable(
            route = Screen.FormScreenEdit.route
        ) {
            IncidentForm2(
                LocalContext.current,
                detailViewModel,
                sharedViewModel = sharedViewModel,
                navController
            )
        }
    }
}
