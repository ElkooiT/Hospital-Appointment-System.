package com.hospital.fileio;

import com.hospital.models.Appointment;
import com.hospital.models.Doctor;
import com.hospital.models.Patient;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


public class ReportGenerator {

    private static final String REPORTS_DIR = "data/reports/";
    private static final DateTimeFormatter FILE_FMT =
            DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    
    public static String generateAppointmentReceipt(Appointment appt) throws IOException {
        new File(REPORTS_DIR).mkdirs();
        String filename = REPORTS_DIR + "receipt_" + appt.getAppointmentId() + ".txt";

        
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            bw.write("=".repeat(50));
            bw.newLine();
            bw.write("       HOSPITAL APPOINTMENT RECEIPT");
            bw.newLine();
            bw.write("=".repeat(50));
            bw.newLine();
            bw.write(appt.getSummary());
            bw.newLine();
            bw.write("=".repeat(50));
            bw.newLine();
            bw.write("Generated: " + LocalDateTime.now().format(
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            bw.newLine();
        }

        System.out.println("📄 Receipt saved: " + filename);
        return filename;
    }

    
    public static String generateSystemReport(
            List<Patient> patients,
            List<Doctor> doctors,
            List<Appointment> appointments) throws IOException {

        new File(REPORTS_DIR).mkdirs();
        String filename = REPORTS_DIR + "system_report_" +
                LocalDateTime.now().format(FILE_FMT) + ".txt";

        
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            pw.println("=".repeat(60));
            pw.println("          HOSPITAL APPOINTMENT SYSTEM — FULL REPORT");
            pw.println("=".repeat(60));
            pw.println("Generated: " + LocalDateTime.now());
            pw.println();

            pw.println("── PATIENTS (" + patients.size() + ") " + "─".repeat(40));
            for (Patient p : patients) {
                pw.println("  " + p.displayInfo());
            }
            pw.println();

            pw.println("── DOCTORS (" + doctors.size() + ") " + "─".repeat(41));
            for (Doctor d : doctors) {
                pw.println("  " + d.displayInfo());
            }
            pw.println();

            pw.println("── APPOINTMENTS (" + appointments.size() + ") " + "─".repeat(37));
            for (Appointment a : appointments) {
                pw.println();
                pw.println(a.getSummary());
                pw.println("-".repeat(40));
            }

            pw.println();
            pw.println("END OF REPORT");
        }

        System.out.println("📊 System report saved: " + filename);
        return filename;
    }
}
