package com.example.cuars.sealed

import com.example.cuars.models.FirstAid

sealed class AidDataState {
    class Success(val data: MutableList<FirstAid>) : AidDataState()
    class Failure(val message: String) : AidDataState()
    object Loading : AidDataState()
    object Empty : AidDataState()
}