package com.hospital.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * CHAPTER 1 (OOP) + CHAPTER 2 (Inheritance)
 *
 * Abstract base for all appointment types.
 * ScheduledAppointment and WalkInAppointment extend this.
 * Demonstrates: abstract classes, encapsulation, static fields (ID counter).
 */
public abstract class Appointment implements Serializable {

    private static final long serialVersionUID = 1L;

    public enum Status { PENDING, CONFIRMED, CANCELLED, COMPLETED }

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    // CHAPTER 1: Static field shared across all Appointment objects
    private static int idCounter = 1000;

    private String appointmentId;
    private Patient patient;
    private Doctor doctor;
    private LocalDateTime dateTime;
    private Status status;
    private String notes;

    public Appointment(Patient patient, Doctor doctor, LocalDateTime dateTime) {
        // CHAPTER 1: Static method call to generate unique ID
        this.appointmentId = "APT-" + (++idCounter);
        this.patient = patient;
        this.doctor = doctor;
        this.dateTime = dateTime;
        this.status = Status.PENDING;
        this.notes = "";
    }

    // CHAPTER 2: Abstract — each subtype describes itself differently
    public abstract String getType();

    public String getSummary() {
        return String.format(
            "Appointment ID : %s\n" +
            "Type           : %s\n" +
            "Patient        : %s\n" +
            "Doctor         : Dr. %s (%s)\n" +
            "Date & Time    : %s\n" +
            "Status         : %s\n" +
            "Notes          : %s",
            appointmentId,
            getType(),
            patient.getName(),
            doctor.getName(), doctor.getSpecialization(),
            dateTime.format(FORMATTER),
            status,
            notes.isEmpty() ? "None" : notes
        );
    }

    // Getters & Setters
    public String getAppointmentId()           { return appointmentId; }
    public Patient getPatient()                { return patient; }
    public Doctor getDoctor()                  { return doctor; }
    public LocalDateTime getDateTime()         { return dateTime; }
    public Status getStatus()                  { return status; }
    public String getNotes()                   { return notes; }

    public void setDateTime(LocalDateTime dt)  { this.dateTime = dt; }
    public void setStatus(Status status)       { this.status = status; }
    public void setNotes(String notes)         { this.notes = notes; }

    public String getFormattedDateTime() {
        return dateTime.format(FORMATTER);
    }

    @Override
    public String toString() {
        return getSummary();
    }
}
