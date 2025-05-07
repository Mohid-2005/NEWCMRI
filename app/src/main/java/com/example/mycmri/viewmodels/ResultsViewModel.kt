package com.example.mycmri.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mycmri.data.ResultEntry
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ResultsViewModel : ViewModel(){
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _results = MutableStateFlow<List<ResultEntry>>(emptyList())
    val results: StateFlow<List<ResultEntry>> = _results

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun loadResults() {
        val currentUser = auth.currentUser ?: return

        _isLoading.value = true

        db.collection("result")
            .whereEqualTo("patient", db.collection("patient").document(currentUser.uid))
            .get()
            .addOnSuccessListener { documents ->
                val loadedResults = documents.mapNotNull { doc ->
                    val name = doc.getString("name") ?: return@mapNotNull null
                    val outcome = doc.getString("outcome") ?: return@mapNotNull null
                    ResultEntry(name, outcome)
                }
                _results.value = loadedResults
                _isLoading.value = false
            }
            .addOnFailureListener {
                _results.value = emptyList()
                _isLoading.value = false
            }
    }

    init {
        loadResults()
    }
}
