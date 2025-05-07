package com.example.mycmri.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * A [ViewModel] class that manages and updates patient allergy information in Firestore.
 */
class AllergyViewModel : ViewModel(){
    private val db = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid
    private val patientDoc = userId?.let {db.collection("patient").document(it)}

    private val _selectedAllergies = MutableStateFlow<List<String>>(emptyList())
    val selectedAllergies: StateFlow<List<String>> = _selectedAllergies.asStateFlow()

    init {
        loadPatientAllergies()
    }

    /**
     * Fetches the 'allergy' field as a List from the 'patient' document in Firebase.
     */
    private fun loadPatientAllergies(){
        patientDoc?.get()?.addOnSuccessListener { snapshot ->
            val list = snapshot.get("allergy") as? List<String> ?: emptyList()
            _selectedAllergies.value = list
        }
    }

    /**
     * Adds/Removes a common allergy from Firestore and local state
     */
    fun toggleAllergy(allergy: String){
        patientDoc?.let { docRef ->
            viewModelScope.launch {
                if(_selectedAllergies.value.contains(allergy))  docRef.update("allergy", FieldValue.arrayRemove(allergy))
                else docRef.update("allergy", FieldValue.arrayUnion(allergy))

                val updated = if(_selectedAllergies.value.contains(allergy)){
                    _selectedAllergies.value - allergy
                } else{
                    _selectedAllergies.value + allergy
                }
                _selectedAllergies.value = updated
            }
        }
    }

    /**
     * Adds a custom allergy string from the allergy field in Firebase.
     */
    fun addCustomAllergy(allergy: String) {
        if (allergy.isBlank()) return
        patientDoc?.let { docRef ->
            viewModelScope.launch {
                docRef.update("allergy", FieldValue.arrayUnion(allergy.trim()))
                _selectedAllergies.value = _selectedAllergies.value + allergy.trim()
            }
        }
    }

    /**
     * Removes an allergy at a given index.
     */
    fun removeAllergyAt(index: Int){
        val oldList = _selectedAllergies.value.toMutableList()
        val old = oldList.removeAt(index)
        patientDoc?.let{ docRef ->
            viewModelScope.launch {
                docRef.update("allergy", FieldValue.arrayRemove(old))
            }
        }
        _selectedAllergies.value = oldList
    }
}