package com.example.cuars.viewModels


import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.cuars.models.IncidentInformation
import com.example.cuars.repositories.StorageRepository
import com.google.firebase.auth.FirebaseUser

class DetailViewModel(
    private val repository: StorageRepository = StorageRepository(),
) : ViewModel() {
    var detailUiState by mutableStateOf(DetailUiState())
        private set

    private val hasUser: Boolean
        get() = repository.hasUser()

    private val user: FirebaseUser?
        get() = repository.user()

    fun onNameChange(name: String) {
        detailUiState = detailUiState.copy(name = name)
    }

    fun onEmailChange(email: String) {
        detailUiState = detailUiState.copy(email = email)
    }

    fun onAddressChange(address: String) {
        detailUiState = detailUiState.copy(address = address)
    }

    fun onPhoneChange(phone: String) {
        detailUiState = detailUiState.copy(phone = phone)
    }

    fun onIncidentLocationChange(incidentLocation: String) {
        detailUiState = detailUiState.copy(incidentLocation = incidentLocation)
    }

    fun onIncidentDescriptionChange(incidentDescription: String) {
        detailUiState = detailUiState.copy(incidentDescription = incidentDescription)
    }

    fun onIncidentWitnessAvailableChange(incidentWitnessAvailable: Boolean) {
        detailUiState = detailUiState.copy(incidentWitnessAvailable = incidentWitnessAvailable)
    }

    fun onIncidentVictimInjuredChange(incidentVictimInjured: Boolean) {
        detailUiState = detailUiState.copy(incidentVictimInjured = incidentVictimInjured)
    }

    fun onIncidentInjuryDescriptionChange(incidentInjuryDescription: String) {
        detailUiState = detailUiState.copy(incidentInjuryDescription = incidentInjuryDescription)
    }

    fun addIncident() {
        if (hasUser) {
            repository.addIncident(
                userId = user!!.uid,
                name = detailUiState.name,
                email = detailUiState.email,
                address = detailUiState.address,
                phone = detailUiState.phone,
                incidentLocation = detailUiState.incidentLocation,
                incidentDescription = detailUiState.incidentDescription,
                incidentWitnessAvailable = detailUiState.incidentWitnessAvailable,
                incidentVictimInjured = detailUiState.incidentVictimInjured,
                incidentInjuryDescription = detailUiState.incidentInjuryDescription
            ) {
                detailUiState = detailUiState.copy(noteAddedStatus = it)
            }
            resetState()
        } else {
        }


    }

    fun setEditFields(incident: IncidentInformation) {
        detailUiState = detailUiState.copy(
            name = incident.name,
            email = incident.email,
            address = incident.address,
            phone = incident.phone,
            incidentLocation = incident.incidentLocation,
            incidentDescription = incident.incidentDescription,
            incidentWitnessAvailable = incident.incidentWitnessAvailable,
            incidentVictimInjured = incident.incidentVictimInjured,
            incidentInjuryDescription = incident.incidentInjuryDescription,
        )

    }

    fun getIncident(incidentInformationId: String) {
        repository.getIncident(
            incidentInformationId = incidentInformationId,
            onError = {},
        ) {
            detailUiState = detailUiState.copy(selectedIncident = it)
            detailUiState.selectedIncident?.let { it1 -> setEditFields(it1) }
        }
    }

    fun updateIncident(
        incidentInformationId: String
    ) {
        repository.updateIncident(
            incidentInformationId = incidentInformationId,
            userId = user!!.uid,
            name = detailUiState.name,
            email = detailUiState.email,
            address = detailUiState.address,
            phone = detailUiState.phone,
            incidentLocation = detailUiState.incidentLocation,
            incidentDescription = detailUiState.incidentDescription,
            incidentWitnessAvailable = detailUiState.incidentWitnessAvailable,
            incidentVictimInjured = detailUiState.incidentVictimInjured,
            incidentInjuryDescription = detailUiState.incidentInjuryDescription,
        ) {
            detailUiState = detailUiState.copy(updateIncidentStatus = it)
        }
    }

    fun resetIncidentAddedStatus() {
        detailUiState = detailUiState.copy(
            noteAddedStatus = false,
            updateIncidentStatus = false,
        )
    }

    fun resetState() {
        detailUiState = detailUiState.copy(name = "")
        detailUiState = detailUiState.copy(email = "")
        detailUiState = detailUiState.copy(address = "")
        detailUiState = detailUiState.copy(phone = "")
        detailUiState = detailUiState.copy(incidentLocation = "")
        detailUiState = detailUiState.copy(incidentDescription = "")
        detailUiState = detailUiState.copy(incidentInjuryDescription = "")

        Log.d("TAG", "Running it")

    }


}

data class DetailUiState(
    val userId: String = "",
    val name: String = "",
    val email: String = "",
    val address: String = "",
    val phone: String = "",
    val incidentLocation: String = "",
    val incidentDescription: String = "",
    val incidentWitnessAvailable: Boolean = false,
    val incidentVictimInjured: Boolean = false,
    val incidentInjuryDescription: String = "",
    val noteAddedStatus: Boolean = false,
    val updateIncidentStatus: Boolean = false,
    val selectedIncident: IncidentInformation? = null,
)
