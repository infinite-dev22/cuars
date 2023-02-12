package com.example.cuars.repositories


import androidx.lifecycle.MutableLiveData
import com.example.cuars.models.IncidentInformation
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

const val NOTES_COLLECTION_REF = "IncidentInformation"

class StorageRepository {
    var specimens: MutableLiveData<List<IncidentInformation>> =
        MutableLiveData<List<IncidentInformation>>()

    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var storageReference = FirebaseStorage.getInstance().reference

    init {
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
    }

    private val TAG = "ReadAndWriteSnippets"
    private lateinit var database: DatabaseReference
    fun initializeDbRef() {
        database = Firebase.database.reference
    }

    fun user() = Firebase.auth.currentUser
    fun hasUser(): Boolean = Firebase.auth.currentUser != null

    fun getUserId(): String = Firebase.auth.currentUser?.uid.orEmpty()

    private val notesRef: CollectionReference = Firebase
        .firestore.collection(NOTES_COLLECTION_REF)

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getUserIncidentInformation(
        userId: String,
    ): Flow<Resources<List<IncidentInformation>>> = callbackFlow {
        var snapshotStateListener: ListenerRegistration? = null

        try {
            snapshotStateListener = notesRef
                .orderBy("timestamp")
                .whereEqualTo("userId", userId)
                .addSnapshotListener { snapshot, e ->
                    val response = if (snapshot != null) {
                        val notes = snapshot.toObjects(IncidentInformation::class.java)
                        Resources.Success(data = notes)
                    } else {
                        Resources.Error(throwable = e?.cause)
                    }
                    trySend(response)

                }


        } catch (e: Exception) {
            trySend(Resources.Error(e.cause))
            e.printStackTrace()
        }

        awaitClose {
            snapshotStateListener?.remove()
        }


    }

    fun getIncident(
        incidentInformationId: String,
        onError: (Throwable?) -> Unit,
        onSuccess: (IncidentInformation?) -> Unit
    ) {
        notesRef
            .document(incidentInformationId)
            .get()
            .addOnSuccessListener {
                onSuccess.invoke(it?.toObject(IncidentInformation::class.java))
            }
            .addOnFailureListener { result ->
                onError.invoke(result.cause)
            }
    }

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
        incidentInjuryDescription: String
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

        database.child(NOTES_COLLECTION_REF).child(documentId).setValue(note)
    }

    fun deleteIncidentInformation(noteId: String, onComplete: (Boolean) -> Unit) {
        notesRef.document(noteId)
            .delete()
            .addOnCompleteListener {
                onComplete.invoke(it.isSuccessful)
            }
    }

    fun updateIncident(
        incidentInformationId: String,
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
        onResult: (Boolean) -> Unit
    ) {
        val updateData = hashMapOf<String, Any>(
            "incidentInformationId" to incidentInformationId,
            "userId" to userId,
            "name" to name,
            "email" to email,
            "address" to address,
            "phone" to phone,
            "incidentLocation" to incidentLocation,
            "incidentDescription" to incidentDescription,
            "incidentWitnessAvailable" to incidentWitnessAvailable,
            "incidentVictimInjured" to incidentVictimInjured,
            "incidentInjuryDescription" to incidentInjuryDescription,
        )

        notesRef.document(incidentInformationId)
            .update(updateData)
            .addOnCompleteListener {
                onResult(it.isSuccessful)
            }
        database.child(NOTES_COLLECTION_REF).child(incidentInformationId).updateChildren(updateData)
    }

    fun signOut() = Firebase.auth.signOut()


}


sealed class Resources<T>(
    val data: T? = null,
    val throwable: Throwable? = null,
) {
    class Loading<T> : Resources<T>()
    class Success<T>(data: T?) : Resources<T>(data = data)
    class Error<T>(throwable: Throwable?) : Resources<T>(throwable = throwable)

}