package com.hospital.exceptions;


public class PatientNotFoundException extends Exception {

    private String patientId;

    public PatientNotFoundException(String patientId) {
        super("No patient found with ID: " + patientId);
        this.patientId = patientId;
    }

    public String getPatientId() { return patientId; }
}
