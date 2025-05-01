package com.example.mycmri.data

/**
 * A representational class for medication used by Patient/User. Contains the id, name, frequency
 * (times per day) and duration (in days) for a given prescription.
 */
data class Medication(
    val id: String = "",
    val name: String,
    val frequency: String,
    val duration: String
)
