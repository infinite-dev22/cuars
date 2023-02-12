package com.example.cuars.incidents

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.cuars.FloatingButton
import com.example.cuars.R
import com.example.cuars.Screen
import com.example.cuars.TopAppBarMain
import com.example.cuars.models.IncidentInformation
import com.example.cuars.models.SharedViewModel
import com.example.cuars.sealed.DataState
import com.example.cuars.viewModels.DetailViewModel
import com.example.cuars.viewModels.IncidentsViewModel

private var selectedIncident by mutableStateOf(IncidentInformation())

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IncidentsScreen(
    navController: NavController,
    incidentsViewModel: IncidentsViewModel,
    sharedViewModel: SharedViewModel,
    detailViewModel: DetailViewModel
) {
    Scaffold(
        topBar = { TopAppBar(title = { TopAppBarMain(topBarTitle = R.string.main_topbar_title) }) },

        floatingActionButton = {
            FloatingButton(navController)
        },
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues = it),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (val result = incidentsViewModel.response.value) {
                is DataState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is DataState.Success -> {
                    ShowLazyList(
                        result.data,
                        navController,
                        sharedViewModel,
                        detailViewModel
                    )
                }

                is DataState.Failure -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = result.message,
                            fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                        )
                    }
                }

                else -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Error Fetching data",
                            fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ShowLazyList(
    incidents: MutableList<IncidentInformation>,
    navController: NavController,
    sharedViewModel: SharedViewModel,
    detailViewModel: DetailViewModel
) {
    if (incidents.size > 0) {
        LazyColumn {
            items(incidents) { incident ->
                CardItem(
                    incident,
                    navController,
                    sharedViewModel,
                    detailViewModel
                )
            }
        }
    } else {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Nothing to show here",
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                textAlign = TextAlign.Center,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun CardItem(
    incident: IncidentInformation,
    navController: NavController,
    sharedViewModel: SharedViewModel,
    detailViewModel: DetailViewModel
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(10.dp)
            .clickable {
                sharedViewModel.addIncidentInformation(incident)
                navController.navigate(route = Screen.FormScreenEdit.route)
            }
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {
            Column() {
                Text(
                    text = "Name: ${incident.name!!}",
                    fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                    textAlign = TextAlign.Center,
                    color = Color.White
                )
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .width(1.dp),
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Telephone: ${incident.phone!!}",
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                    textAlign = TextAlign.Center,
                    color = Color.White
                )
                Text(
                    text = "Email: ${incident.email!!}",
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    textAlign = TextAlign.Center,
                    color = Color.White
                )
                Text(
                    text = "Address: ${incident.address!!}",
                    fontSize = MaterialTheme.typography.bodySmall.fontSize,
                    textAlign = TextAlign.Center,
                    color = Color.White
                )
                Text(
                    text = "Accident at: ${incident.incidentLocation!!}",
                    fontSize = MaterialTheme.typography.labelSmall.fontSize,
                    textAlign = TextAlign.Center,
                    color = Color.White
                )
            }
        }

    }
}