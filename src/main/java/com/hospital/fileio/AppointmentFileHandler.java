package com.hospital.fileio;

import com.hospital.models.Appointment;
import com.hospital.models.Doctor;
import com.hospital.models.Patient;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class AppointmentFileHandler {

    private static final String APPOINTMENTS_FILE = "data/appointments.dat";
    private static final String PATIENTS_FILE     = "data/patients.dat";
    private static final String DOCTORS_FILE      = "data/doctors.dat";

    // ─── Save ─────────────────────────────────────────────────────────────

    
    public static void saveAppointments(List<Appointment> appointments) throws IOException {
        ensureDataDirectory();
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(APPOINTMENTS_FILE))) {
            oos.writeObject(appointments);
            System.out.println("💾 Appointments saved to " + APPOINTMENTS_FILE);
        }
    }

    public static void savePatients(List<Patient> patients) throws IOException {
        ensureDataDirectory();
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(PATIENTS_FILE))) {
            oos.writeObject(patients);
            System.out.println("💾 Patients saved to " + PATIENTS_FILE);
        }
    }

    public static void saveDoctors(List<Doctor> doctors) throws IOException {
        ensureDataDirectory();
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(DOCTORS_FILE))) {
            oos.writeObject(doctors);
            System.out.println("💾 Doctors saved to " + DOCTORS_FILE);
        }
    }

    // ─── Load ─────────────────────────────────────────────────────────────

    
    @SuppressWarnings("unchecked")
    public static List<Appointment> loadAppointments() throws IOException, ClassNotFoundException {
        File f = new File(APPOINTMENTS_FILE);
        if (!f.exists()) return new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(APPOINTMENTS_FILE))) {
            return (List<Appointment>) ois.readObject();
        }
    }

    @SuppressWarnings("unchecked")
    public static List<Patient> loadPatients() throws IOException, ClassNotFoundException {
        File f = new File(PATIENTS_FILE);
        if (!f.exists()) return new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(PATIENTS_FILE))) {
            return (List<Patient>) ois.readObject();
        }
    }

    @SuppressWarnings("unchecked")
    public static List<Doctor> loadDoctors() throws IOException, ClassNotFoundException {
        File f = new File(DOCTORS_FILE);
        if (!f.exists()) return new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(DOCTORS_FILE))) {
            return (List<Doctor>) ois.readObject();
        }
    }

    private static void ensureDataDirectory() {
        new File("data").mkdirs();
    }
}
