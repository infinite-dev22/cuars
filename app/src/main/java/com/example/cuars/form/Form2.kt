package com.example.cuars.form

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.cuars.EditNumberField
import com.example.cuars.EditTextAreaInput
import com.example.cuars.EditTextInput
import com.example.cuars.R
import com.example.cuars.TopAppBarSlave
import com.example.cuars.models.SharedViewModel
import com.example.cuars.viewModels.DetailUiState
import com.example.cuars.viewModels.DetailViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IncidentForm2(
    context: Context,
    detailViewModel: DetailViewModel?,
    sharedViewModel: SharedViewModel,
    navController: NavController
) {
    detailViewModel?.setEditFields(sharedViewModel.incidentInformation)
    val detailUiState = detailViewModel?.detailUiState ?: DetailUiState()

    val isFormsNotBlank = detailUiState.name.isNotBlank()
            && detailUiState.email.isNotBlank()
            && detailUiState.address.isNotBlank()
            && detailUiState.phone.isNotBlank()
            && detailUiState.incidentLocation.isNotBlank()
            && detailUiState.incidentDescription.isNotBlank()
            && detailUiState.incidentInjuryDescription.isNotBlank()

    MaterialTheme {
        Scaffold(
            topBar = {
                TopAppBar(title = {
                    TopAppBarSlave(
                        topBarTitle = R.string.form_topbar_title,
                        navController
                    )
                })
            },
        ) {
            Column(
                modifier = Modifier
                    .height(IntrinsicSize.Max)
                    .fillMaxSize()
                    .padding(paddingValues = it)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                EditTextInput(
                    R.string.victim_name,
                    value = detailUiState.name,
                    onValueChange = { detailViewModel?.onNameChange(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(8.dp),
                    singleLine = true
                )
                EditTextInput(
                    R.string.victim_email,
                    value = detailUiState.email,
                    onValueChange = { detailViewModel?.onEmailChange(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(8.dp),
                    singleLine = true
                )
                EditTextInput(
                    R.string.victim_address,
                    value = detailUiState.address,
                    onValueChange = { detailViewModel?.onAddressChange(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(8.dp),
                    singleLine = true
                )
                EditNumberField(
                    R.string.victim_phone,
                    value = detailUiState.phone,
                    onValueChange = { detailViewModel?.onPhoneChange(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(8.dp),
                    singleLine = true
                )
                EditTextInput(
                    R.string.location,
                    value = detailUiState.incidentLocation,
                    onValueChange = { detailViewModel?.onIncidentLocationChange(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(8.dp),
                    singleLine = true
                )
                EditTextAreaInput(
                    R.string.description,
                    value = detailUiState.incidentDescription,
                    onValueChange = { detailViewModel?.onIncidentDescriptionChange(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .height(300.dp)
                        .padding(8.dp),
                    singleLine = false,
                    maxLines = 7
                )
                EditTextAreaInput(
                    R.string.injury,
                    value = detailUiState.incidentInjuryDescription,
                    onValueChange = { detailViewModel?.onIncidentInjuryDescriptionChange(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .height(300.dp)
                        .padding(8.dp),
                    singleLine = false,
                    maxLines = 7
                )
                Button(
                    onClick = {
                        if (isFormsNotBlank) {
                            detailViewModel?.addIncident()
                            Toast.makeText(context, "Report Sent Successfully", Toast.LENGTH_LONG)
                                .show()
                            detailViewModel?.resetState()
                            navController.popBackStack()
                        } else {
                            Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_LONG)
                                .show()
                        }
                    },
                    Modifier.padding(0.dp, 20.dp, 0.dp, 50.dp),
                    shape = RoundedCornerShape(5.dp)
                ) {
                    Text(text = "Report")
                    Divider(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(20.dp),
                        color = Color.Transparent
                    )
                    Image(
                        painterResource(id = R.drawable.send),
                        contentDescription = "Cart button icon",
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}