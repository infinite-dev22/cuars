package com.example.cuars.viewModels


import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.cuars.models.IncidentInformation
import com.example.cuars.sealed.DataState
import com.example.cuars.user
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings


class IncidentsViewModel : ViewModel() {
    val response: MutableState<DataState> = mutableStateOf(DataState.Empty)
    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    init {
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
        fetchDataFromFirebase()
    }

    private fun fetchDataFromFirebase() {
        val tempList = mutableListOf<IncidentInformation>()
        response.value = DataState.Loading
        FirebaseDatabase.getInstance().reference.child("IncidentInformation")
            .orderByChild("userId")
            .equalTo(user?.uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (dataSnap in snapshot.children) {
                        val incident = dataSnap.getValue(IncidentInformation::class.java)
                        if (incident != null)
                            tempList.add(incident)
                    }
                    response.value = DataState.Success(tempList)
                }

                override fun onCancelled(error: DatabaseError) {
                    response.value = DataState.Failure(error.message)
                }

            })
    }
}

