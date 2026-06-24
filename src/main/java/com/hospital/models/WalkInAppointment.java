package com.hospital.models;

import java.time.LocalDateTime;

/**
 * CHAPTER 2 (Inheritance)
 *
 * An unscheduled walk-in visit. No advance booking — patient arrives
 * and is assigned the next available doctor.
 * Demonstrates: second concrete subclass, unique fields not in parent.
 */
public class WalkInAppointment extends Appointment {

    private static final long serialVersionUID = 1L;

    private int queueNumber;
    private String urgencyLevel; // LOW, MEDIUM, HIGH

    public WalkInAppointment(Patient patient, Doctor doctor,
                             LocalDateTime arrivalTime, int queueNumber,
                             String urgencyLevel) {
        super(patient, doctor, arrivalTime);
        this.queueNumber = queueNumber;
        this.urgencyLevel = urgencyLevel.toUpperCase();
    }

    @Override
    public String getType() {
        return "Walk-In";
    }

    @Override
    public String getSummary() {
        return super.getSummary() +
               String.format("\nQueue Number   : %d\nUrgency        : %s",
                       queueNumber, urgencyLevel);
    }

    public int getQueueNumber()                 { return queueNumber; }
    public String getUrgencyLevel()             { return urgencyLevel; }
    public void setUrgencyLevel(String level)   { this.urgencyLevel = level.toUpperCase(); }
}
