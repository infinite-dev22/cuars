package com.example.cuars.repositories


import com.example.cuars.models.IncidentInformation
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

const val NOTES_COLLECTION_REF = "IncidentInformation"

class StorageRepository {

    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    init {
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
    }

    private lateinit var database: DatabaseReference
    private fun initializeDbRef() {
        database = Firebase.database.reference
    }

    fun user() = Firebase.auth.currentUser
    fun hasUser(): Boolean = Firebase.auth.currentUser != null

    private val notesRef: CollectionReference = Firebase
        .firestore.collection(NOTES_COLLECTION_REF)

    fun addIncident(
        userId: String,
        name: String,
        email: String,
        address: String,
        phone: String,
        incidentLocation: String,
        incidentDescription: String,
        incidentWitnessAvailable: Boolean,
        incidentVictimInjured: Boolean,
        incidentInjuryDescription: String,
        onComplete: (Boolean) -> Unit,
    ) {
        initializeDbRef()
        val documentId = notesRef.document().id
        val note = IncidentInformation(
            userId,
            name,
            email,
            address,
            phone,
            incidentLocation,
            incidentDescription,
            incidentWitnessAvailable,
            incidentVictimInjured,
            incidentInjuryDescription
        )
        notesRef
            .document(documentId)
            .set(note)
            .addOnCompleteListener { result ->
                onComplete.invoke(result.isSuccessful)
            }

        try {
            database.child(NOTES_COLLECTION_REF).child(documentId).setValue(note)
        } catch (_: Exception) {
        }
    }

}