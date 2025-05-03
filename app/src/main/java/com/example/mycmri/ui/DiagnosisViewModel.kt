package com.example.mycmri.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mycmri.data.DiagnosisOption
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel to manage loading and updating of diagnoses for a patient.
 */
class DiagnosisViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid
    private val patientDoc = userId?.let { db.collection("patient").document(it) }

    /**
     * Available diagnosis options loaded from Firestore 'diagnosis' collection.
     */
    private val _availableDiagnoses = MutableStateFlow<List<DiagnosisOption>>(emptyList())
    val availableDiagnoses: StateFlow<List<DiagnosisOption>> = _availableDiagnoses

    /**
     * List of selected diagnosis document IDs (empty string means placeholder for new entries).
     */
    private val _selectedDiagnoses = MutableStateFlow<List<String>>(emptyList())
    val selectedDiagnoses: StateFlow<List<String>> = _selectedDiagnoses

    init {
        loadAvailableDiagnoses()
        loadPatientDiagnoses()
    }

    /**
     * Retrieves all diagnoses from the diagnosis collection in Firebase
     */
    private fun loadAvailableDiagnoses() {
        db.collection("diagnosis").get()
            .addOnSuccessListener { snapshot ->
                val options = snapshot.documents.mapNotNull { doc ->
                    val name = doc.getString("name") ?: return@mapNotNull null
                    DiagnosisOption(name, doc.id)
                }
                _availableDiagnoses.value = options
            }
    }

    private fun loadPatientDiagnoses() {
        patientDoc?.get()?.addOnSuccessListener { snapshot ->
            val refs = snapshot.get("diagnosis") as? List<DocumentReference> ?: emptyList()
            val ids = refs.map { it.id }
            _selectedDiagnoses.value = ids
        }
    }

    /**
     * Adds a placeholder entry for selecting a new diagnosis.
     */
    fun addEmptyDiagnosis() {
        _selectedDiagnoses.value = _selectedDiagnoses.value + ""
    }

    /**
     * Replace diagnosis at [index] with [newDocId], syncing with Firestore.
     */
    fun replaceDiagnosisAt(index: Int, newDocId: String) {
        val oldList = _selectedDiagnoses.value.toMutableList()
        val oldId = oldList[index]
        val newList = oldList.apply { this[index] = newDocId }

        // Syncs Firestore
        patientDoc?.let { docRef ->
            viewModelScope.launch {
                if (oldId.isNotBlank()) {
                    docRef.update("diagnosis", FieldValue.arrayRemove(db.collection("diagnosis").document(oldId)))
                }
                if (newDocId.isNotBlank()) {
                    docRef.update("diagnosis", FieldValue.arrayUnion(db.collection("diagnosis").document(newDocId)))
                }
            }
        }

        _selectedDiagnoses.value = newList
    }

    /**
     * Remove diagnosis entry at [index], syncing with Firestore if necessary.
     * @param index the entry at this index to be removed within Firestore
     */
    fun removeDiagnosisAt(index: Int) {
        val oldList = _selectedDiagnoses.value.toMutableList()
        val oldId = oldList.removeAt(index)

        patientDoc?.let { docRef ->
            if (oldId.isNotBlank()) {
                docRef.update("diagnosis", FieldValue.arrayRemove(db.collection("diagnosis").document(oldId)))
            }
        }

        _selectedDiagnoses.value = oldList
    }
}