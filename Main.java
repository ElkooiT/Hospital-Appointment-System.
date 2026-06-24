package com.hospital;

import com.hospital.database.*;
import com.hospital.exceptions.*;
import com.hospital.fileio.*;
import com.hospital.manager.AppointmentManager;
import com.hospital.models.*;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

/*
 * All 7 chapters together into a single Comman Line Interface (CLI) application.
 
  CHAPTER COVERAGE SUMMARY:
    Ch1 - OOP basics    : Person, Patient, Doctor, Appointment (classes/objects)

    Ch2 - Inheritance   : Person→Patient/Doctor, Appointment→Scheduled/WalkIn

    Ch3 - Polymorphism/Interf.  : Notifiable, Schedulable, polymorphic displayInfo()

    Ch4 - Exception Handling    : AppointmentConflictException, PatientNotFoundException,
                                  InvalidTimeSlotException, try/catch/finally

    Ch5 - Files & Streams   : Serialization (AppointmentFileHandler),
                                  BufferedWriter/PrintWriter (ReportGenerator)

    Ch6 - Collections   : AppointmentManager uses ArrayList + HashMap

    Ch7 - JDBC  : PatientDAO, DoctorDAO, AppointmentDAO with MySQL
 */
public class Main {

    private static final AppointmentManager manager = new AppointmentManager();
    private static final PatientDAO patientDAO = new PatientDAO();
    private static final DoctorDAO doctorDAO = new DoctorDAO();
    private static final AppointmentDAO appointmentDAO = new AppointmentDAO();
    private static final Scanner scanner = new Scanner(System.in);
    private static final DateTimeFormatter DT_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private static boolean dbAvailable = false;

    public static void main(String[] args) {
        System.out.println(" =================================================");
        System.out.println("||     HOSPITAL APPOINTMENT SYSTEM  v1.0        ||");
        System.out.println("||     Java OOP Course Project                  ||");
        System.out.println(" ==================================================");

        // CHAPTER 7: Attempt DB connection — graceful fallback if unavailable
        try {
            DBConnection.getConnection();
            dbAvailable = true;
            patientDAO.printDatabaseInfo();
        } catch (SQLException e) {
            System.out.println("Database unavailable. Running in memory-only mode.");
            System.out.println("   (" + e.getMessage() + ")");
        }

        // CHAPTER 5: Load previously saved data from binary files
        loadFromFiles();

        // Seed demo data if the system is empty
        if (manager.getAllDoctors().isEmpty()) {
            seedDemoData();
        }

        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = readInt("Enter choice: ");
            switch (choice) {
                case 1 -> patientMenu();
                case 2 -> doctorMenu();
                case 3 -> appointmentMenu();
                case 4 -> reportMenu();
                case 5 -> databaseMenu();
                case 0 -> running = false;
                default -> System.out.println("⚠️ Invalid option.");
            }
        }

        // CHAPTER 5: Save to files on exit
        saveToFiles();
        DBConnection.closeConnection();
        System.out.println("\nGoodbye. 👋");
    }

    // menus

    private static void printMainMenu() {
        System.out.println("\n ============ MAIN MENU ============");
        System.out.println("  1. Patient Management");
        System.out.println("  2. Doctor Management");
        System.out.println("  3. Appointment Management");
        System.out.println("  4. Reports (File I/O)");
        System.out.println("  5. Database Operations (JDBC)");
        System.out.println("  0. Exit");
        System.out.println("======================================");
    }

    private static void patientMenu() {
        System.out.println("\n ============ PATIENT MANAGEMENT ============");
        System.out.println("  1. Register new patient");
        System.out.println("  2. View all patients");
        System.out.println("  3. Search patient by ID");
        int choice = readInt("Choice: ");

        switch (choice) {
            case 1 -> registerPatient();
            case 2 -> {
                // CHAPTER 6: Iterating ArrayList
                List<Patient> patients = manager.getAllPatients();
                if (patients.isEmpty()) {
                    System.out.println("No patients registered.");
                    return;
                }
                // CHAPTER 3: Polymorphic displayInfo() called on each Patient
                patients.forEach(p -> System.out.println("  " + p.displayInfo()));
            }
            case 3 -> {
                String id = readString("Enter Patient ID: ");
                try {
                    Patient p = manager.getPatient(id); // CHAPTER 4: throws checked exception
                    System.out.println(p.displayInfo());
                } catch (PatientNotFoundException e) {
                    System.out.println("❌ " + e.getMessage()); // CHAPTER 4: caught here
                }
            }
        }
    }

    private static void doctorMenu() {
        System.out.println("\n ============ DOCTOR MANAGEMENT ============");
        System.out.println("  1. Register new doctor");
        System.out.println("  2. View all doctors");
        System.out.println("  3. Find by specialization");
        int choice = readInt("Choice: ");

        switch (choice) {
            case 1 -> registerDoctor();
            case 2 -> {
                List<Doctor> doctors = manager.getAllDoctors();
                if (doctors.isEmpty()) {
                    System.out.println("No doctors registered.");
                    return;
                }
                doctors.forEach(d -> System.out.println("  " + d.displayInfo()));
            }
            case 3 -> {
                String spec = readString("Specialization: ");
                List<Doctor> filtered = manager.getDoctorsBySpecialization(spec);
                if (filtered.isEmpty()) {
                    System.out.println("No doctors found.");
                    return;
                }
                filtered.forEach(d -> System.out.println("  " + d.displayInfo()));
            }
        }
    }

    private static void appointmentMenu() {
        System.out.println("\n ============ APPOINTMENT MANAGEMENT ============");
        System.out.println("  1. Book scheduled appointment");
        System.out.println("  2. Register walk-in");
        System.out.println("  3. View all appointments");
        System.out.println("  4. Cancel appointment");
        System.out.println("  5. Complete appointment");
        System.out.println("  6. View appointments by patient");
        int choice = readInt("Choice: ");

        switch (choice) {
            case 1 -> bookScheduled();
            case 2 -> registerWalkIn();
            case 3 -> {
                List<Appointment> all = manager.getAllAppointments();
                if (all.isEmpty()) {
                    System.out.println("No appointments.");
                    return;
                }
                // CHAPTER 3: getSummary() called polymorphically on ScheduledAppointment or
                // WalkIn
                all.forEach(a -> {
                    System.out.println("\n" + a.getSummary());
                    System.out.println("─".repeat(40));
                });
            }
            case 4 -> {
                String id = readString("Appointment ID: ");
                manager.cancelAppointment(id);
            }
            case 5 -> {
                String id = readString("Appointment ID: ");
                manager.completeAppointment(id);
            }
            case 6 -> {
                String pid = readString("Patient ID: ");
                manager.getAppointmentsByPatient(pid)
                        .forEach(a -> System.out.println("\n" + a.getSummary()));
            }
        }
    }

    private static void reportMenu() {
        System.out.println("\n ============ REPORTS (CHAPTER 5: FILE I/O) ============");
        System.out.println("  1. Generate system report (PrintWriter)");
        System.out.println("  2. Generate appointment receipt (BufferedWriter)");
        System.out.println("  3. Save all data to binary files (Serialization)");
        System.out.println("  4. Load data from binary files (Deserialization)");
        int choice = readInt("Choice: ");

        switch (choice) {
            case 1 -> {
                // CHAPTER 5: Character stream output (PrintWriter)
                try {
                    ReportGenerator.generateSystemReport(
                            manager.getAllPatients(),
                            manager.getAllDoctors(),
                            manager.getAllAppointments());
                } catch (IOException e) {
                    System.out.println("❌ Report error: " + e.getMessage());
                }
            }
            case 2 -> {
                List<Appointment> all = manager.getAllAppointments();
                if (all.isEmpty()) {
                    System.out.println("No appointments to generate receipt for.");
                    return;
                }
                System.out.println("Available IDs:");
                all.forEach(a -> System.out.println("  " + a.getAppointmentId()));
                String id = readString("Appointment ID: ");
                Appointment target = all.stream()
                        .filter(a -> a.getAppointmentId().equals(id))
                        .findFirst().orElse(null);
                if (target == null) {
                    System.out.println("Not found.");
                    return;
                }
                try {
                    // CHAPTER 5: Character stream output (BufferedWriter)
                    ReportGenerator.generateAppointmentReceipt(target);
                } catch (IOException e) {
                    System.out.println("❌ " + e.getMessage());
                }
            }
            case 3 -> saveToFiles();
            case 4 -> loadFromFiles();
        }
    }

    private static void databaseMenu() {
        if (!dbAvailable) {
            System.out
                    .println("⚠️  Database is not connected. Check MySQL is running and schema.sql has been executed.");
            return;
        }
        System.out.println("\n─── DATABASE (CHAPTER 7: JDBC) ───");
        System.out.println("  1. Sync patients to DB");
        System.out.println("  2. Sync doctors to DB");
        System.out.println("  3. Sync appointments to DB");
        System.out.println("  4. View appointment summaries from DB (Scrollable ResultSet)");
        System.out.println("  5. Print DB metadata");
        int choice = readInt("Choice: ");

        switch (choice) {
            case 1 -> {
                // CHAPTER 7: INSERT via PreparedStatement
                try {
                    for (Patient p : manager.getAllPatients())
                        patientDAO.insertPatient(p);
                } catch (SQLException e) {
                    System.out.println("❌ DB error: " + e.getMessage());
                }
            }
            case 2 -> {
                try {
                    for (Doctor d : manager.getAllDoctors())
                        doctorDAO.insertDoctor(d);
                } catch (SQLException e) {
                    System.out.println("❌ DB error: " + e.getMessage());
                }
            }
            case 3 -> {
                try {
                    for (Appointment a : manager.getAllAppointments())
                        appointmentDAO.insertAppointment(a);
                } catch (SQLException e) {
                    System.out.println("❌ DB error: " + e.getMessage());
                }
            }
            case 4 -> {
                // CHAPTER 7: Scrollable ResultSet demo
                try {
                    List<String> rows = appointmentDAO.getAppointmentSummaries();
                    if (rows.isEmpty()) {
                        System.out.println("No records in DB.");
                        return;
                    }
                    System.out.println("\n" + String.format("%-12s | %-20s | %-20s | %-16s | %-10s | %s",
                            "ID", "Patient", "Doctor", "DateTime", "Status", "Type"));
                    System.out.println("─".repeat(100));
                    rows.forEach(System.out::println);
                } catch (SQLException e) {
                    System.out.println("❌ DB error: " + e.getMessage());
                }
            }
            case 5 -> {
                try {
                    patientDAO.printDatabaseInfo();
                } catch (SQLException e) {
                    System.out.println("❌ " + e.getMessage());
                }
            }
        }
    }

    // ─── Action Helpers ───────────────────────────────────────────────────

    private static void registerPatient() {
        System.out.println("\n── Register Patient ──");
        String id = readString("Patient ID (e.g. P001): ");
        String name = readString("Full name: ");
        String phone = readString("Phone: ");
        String email = readString("Email: ");
        int age = readInt("Age: ");
        String blood = readString("Blood type (A+, B-, etc.): ");

        Patient p = new Patient(id, name, phone, email, age, blood);
        manager.registerPatient(p);
    }

    private static void registerDoctor() {
        System.out.println("\n── Register Doctor ──");
        String id = readString("Doctor ID (e.g. D001): ");
        String name = readString("Full name: ");
        String phone = readString("Phone: ");
        String email = readString("Email: ");
        String spec = readString("Specialization: ");
        String lic = readString("License number: ");

        Doctor d = new Doctor(id, name, phone, email, spec, lic);
        manager.registerDoctor(d);
    }

    private static void bookScheduled() {
        System.out.println("\n── Book Scheduled Appointment ──");
        System.out.println("Available patients:");
        manager.getAllPatients().forEach(p -> System.out.println("  " + p.getId() + " - " + p.getName()));
        String patientId = readString("Patient ID: ");

        System.out.println("Available doctors:");
        manager.getAllDoctors().forEach(d -> System.out
                .println("  " + d.getId() + " - Dr. " + d.getName() + " (" + d.getSpecialization() + ")"));
        String doctorId = readString("Doctor ID: ");

        String dtStr = readString("Date & time (yyyy-MM-dd HH:mm): ");
        int duration = readInt("Duration (minutes): ");
        String reason = readString("Reason for visit: ");

        try {
            LocalDateTime dt = LocalDateTime.parse(dtStr, DT_FMT);
            // CHAPTER 4: Multiple checked exceptions — ALL must be caught
            ScheduledAppointment appt = manager.bookScheduledAppointment(
                    patientId, doctorId, dt, duration, reason);
            System.out.println("\n✅ Appointment booked!\n" + appt.getSummary());
        } catch (PatientNotFoundException e) {
            System.out.println("❌ " + e.getMessage()); // Ch4
        } catch (AppointmentConflictException e) {
            System.out.println("❌ " + e.getMessage()); // Ch4
        } catch (InvalidTimeSlotException e) {
            System.out.println("❌ " + e.getMessage()); // Ch4
        } catch (DateTimeParseException e) {
            System.out.println("❌ Invalid date format. Use yyyy-MM-dd HH:mm");
        }
    }

    private static void registerWalkIn() {
        System.out.println("\n── Register Walk-In ──");
        String patientId = readString("Patient ID: ");
        String doctorId = readString("Doctor ID: ");
        String urgency = readString("Urgency (LOW / MEDIUM / HIGH): ");

        try {
            WalkInAppointment appt = manager.registerWalkIn(patientId, doctorId, urgency);
            System.out.println("\n✅ Walk-in registered!\n" + appt.getSummary());
        } catch (PatientNotFoundException e) {
            System.out.println("❌ " + e.getMessage());
        }
    }

    // File I/O

    private static void saveToFiles() {
        // CHAPTER 5: Serialization — ObjectOutputStream
        try {
            AppointmentFileHandler.savePatients(manager.getAllPatients());
            AppointmentFileHandler.saveDoctors(manager.getAllDoctors());
            AppointmentFileHandler.saveAppointments(manager.getAllAppointments());
        } catch (IOException e) {
            System.out.println("⚠️ Could not save data: " + e.getMessage());
        }
    }

    private static void loadFromFiles() {
        // CHAPTER 5: Deserialization — ObjectInputStream
        try {
            List<Patient> patients = AppointmentFileHandler.loadPatients();
            List<Doctor> doctors = AppointmentFileHandler.loadDoctors();
            List<Appointment> appointments = AppointmentFileHandler.loadAppointments();

            if (!patients.isEmpty())
                manager.loadPatients(patients);
            if (!doctors.isEmpty())
                manager.loadDoctors(doctors);
            if (!appointments.isEmpty())
                manager.loadAppointments(appointments);

            if (!patients.isEmpty() || !doctors.isEmpty()) {
                System.out.println("📂 Data loaded from files.");
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("ℹ️ No saved data found (first run is normal).");
        }
    }

    // Demo Data

    private static void seedDemoData() {
        System.out.println("\n📋 Loading demo data...");
        manager.registerDoctor(new Doctor("D001", "Arsoniya Demo", "0911000001",
                "arsoniya@hospital.com", "Cardiology", "LIC-100"));
        manager.registerDoctor(new Doctor("D002", "Fikadu Demo", "0911000002",
                "fikadu@hospital.com", "General Practice", "LIC-101"));
        manager.registerPatient(new Patient("P001", "Tsinuel Demo", "0922111001",
                "tsinuel@mail.com", 34, "O+"));
        manager.registerPatient(new Patient("P002", "Aisha Demo", "0922111002",
                "aisha@mail.com", 27, "A-"));
        System.out.println("✅Data ready. Try booking an appointment!\n");
    }

    // Input Utilities

    private static int readInt(String prompt) {
        System.out.print(prompt);
        // CHAPTER 4: Handle bad input gracefully
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
}
