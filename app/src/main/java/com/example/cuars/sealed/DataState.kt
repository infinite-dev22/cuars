package com.example.cuars.sealed

import com.example.cuars.models.IncidentInformation


sealed class DataState {
    class Success(val data: MutableList<IncidentInformation>) : DataState()
    class Failure(val message: String) : DataState()
    object Loading : DataState()
    object Empty : DataState()
}