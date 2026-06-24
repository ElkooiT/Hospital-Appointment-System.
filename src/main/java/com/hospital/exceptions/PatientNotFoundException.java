package com.hospital.exceptions;

/**
 * CHAPTER 4 (Exception Handling)
 *
 * Thrown when a lookup for a patient ID returns no result.
 * Checked exception — forces callers to acknowledge the failure case.
 */
public class PatientNotFoundException extends Exception {

    private String patientId;

    public PatientNotFoundException(String patientId) {
        super("No patient found with ID: " + patientId);
        this.patientId = patientId;
    }

    public String getPatientId() { return patientId; }
}
