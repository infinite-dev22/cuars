package com.example.cuars.viewModels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.cuars.models.FirstAid
import com.example.cuars.sealed.AidDataState
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings


class FirstAidViewModel : ViewModel() {
    val response: MutableState<AidDataState> = mutableStateOf(AidDataState.Empty)
    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    init {
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
        fetchDataFromFirebase()
    }

    private fun fetchDataFromFirebase() {
        val tempList = mutableListOf<FirstAid>()
        response.value = AidDataState.Loading
        FirebaseDatabase.getInstance()
            .reference.child("FirstAid")
            .orderByChild("name")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (dataSnap in snapshot.children) {
                        val firstAid = dataSnap.getValue(FirstAid::class.java)
                        if (firstAid != null)
                            tempList.add(firstAid)
                    }
                    response.value = AidDataState.Success(tempList)
                }

                override fun onCancelled(error: DatabaseError) {
                    response.value = AidDataState.Failure(error.message)
                }

            })
    }
}