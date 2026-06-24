package com.hospital.exceptions;

/**
 * CHAPTER 4 (Exception Handling)
 *
 * Thrown when a doctor is already booked for a requested time slot.
 * Checked exception — callers are forced by the compiler to handle it.
 * Demonstrates: custom exception class, extends Exception (checked).
 */
public class AppointmentConflictException extends Exception {

    private String doctorName;
    private String conflictTime;

    public AppointmentConflictException(String doctorName, String conflictTime) {
        super(String.format(
            "Scheduling conflict: Dr. %s already has an appointment at %s.",
            doctorName, conflictTime
        ));
        this.doctorName = doctorName;
        this.conflictTime = conflictTime;
    }

    public String getDoctorName()   { return doctorName; }
    public String getConflictTime() { return conflictTime; }
}
