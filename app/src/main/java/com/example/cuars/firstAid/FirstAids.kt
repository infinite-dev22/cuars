package com.example.cuars.firstAid

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.cuars.FloatingButton
import com.example.cuars.Screen
import com.example.cuars.TopAppBarMain
import com.example.cuars.models.FirstAid
import com.example.cuars.models.SharedViewModel
import com.example.cuars.sealed.AidDataState
import com.example.cuars.viewModels.DetailViewModel
import com.example.cuars.viewModels.FirstAidViewModel
import com.example.cuars.R
import com.example.cuars.TopAppBarSlave

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FirstAidScreen(
    navController: NavController,
    incidentsViewModel: FirstAidViewModel,
    detailViewModel: DetailViewModel
) {
    Scaffold(
        topBar = { TopAppBar(title = { TopAppBarSlave(topBarTitle = R.string.aid_topbar_title, navController) }) },

        floatingActionButton = {
            FloatingButton(
                navController
            )
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
                is AidDataState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is AidDataState.Success -> {
                    ShowLazyList(
                        result.data,
                        navController,
                        detailViewModel
                    )
                }

                is AidDataState.Failure -> {
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
    firstAid: MutableList<FirstAid>,
    navController: NavController,
    detailViewModel: DetailViewModel
) {
    if (firstAid.size > 0) {
        LazyColumn {
            items(firstAid) { firstAid ->
                CardItem(
                    firstAid,
                    navController,
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
    firstAid: FirstAid,
    navController: NavController,
    detailViewModel: DetailViewModel
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(10.dp)
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {
            Column {
                Text(
                    text = "${firstAid.name!!}",
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
                    text = "${firstAid.description!!}",
                    fontSize = MaterialTheme.typography.bodySmall.fontSize,
                    textAlign = TextAlign.Center,
                    color = Color.White
                )
            }
        }

    }
}