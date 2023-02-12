package com.example.cuars.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class SharedViewModel: ViewModel() {
    var incidentInformation by mutableStateOf(IncidentInformation())
        private set

    fun addIncidentInformation (incident: IncidentInformation) {
        incidentInformation = incident
    }
}