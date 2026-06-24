package com.hospital.exceptions;


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
