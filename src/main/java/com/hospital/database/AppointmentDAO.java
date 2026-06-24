package com.hospital.database;

import com.hospital.models.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * CHAPTER 7 (JDBC — Database Programming)
 *
 * Data Access Object for appointments table.
 * Demonstrates:
 *   - PreparedStatement with multiple types (String, Timestamp, int, boolean)
 *   - Scrollable ResultSet (TYPE_SCROLL_INSENSITIVE — Ch7 Advanced ResultSets)
 *   - JOIN query to retrieve related patient/doctor info
 */
public class AppointmentDAO {

    public void insertAppointment(Appointment appt) throws SQLException {
        String sql = "INSERT INTO appointments " +
                     "(appointment_id, patient_id, doctor_id, date_time, status, notes, type, extra_info) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = DBConnection.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, appt.getAppointmentId());
            pstmt.setString(2, appt.getPatient().getId());
            pstmt.setString(3, appt.getDoctor().getId());
            // CHAPTER 7: LocalDateTime → SQL Timestamp conversion
            pstmt.setTimestamp(4, Timestamp.valueOf(appt.getDateTime()));
            pstmt.setString(5, appt.getStatus().name());
            pstmt.setString(6, appt.getNotes());
            pstmt.setString(7, appt.getType());

            // Store type-specific info as a descriptor string
            String extra = "";
            if (appt instanceof ScheduledAppointment sa) {
                extra = "duration=" + sa.getDurationMinutes() + ";reason=" + sa.getReasonForVisit();
            } else if (appt instanceof WalkInAppointment wa) {
                extra = "queue=" + wa.getQueueNumber() + ";urgency=" + wa.getUrgencyLevel();
            }
            pstmt.setString(8, extra);
            pstmt.executeUpdate();
            System.out.println("✅ Appointment saved to DB: " + appt.getAppointmentId());
        }
    }

    public void updateStatus(String appointmentId, Appointment.Status status) throws SQLException {
        String sql = "UPDATE appointments SET status = ? WHERE appointment_id = ?";
        try (PreparedStatement pstmt = DBConnection.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, status.name());
            pstmt.setString(2, appointmentId);
            pstmt.executeUpdate();
        }
    }

    /**
     * CHAPTER 7: Advanced ResultSet — scrollable, read-only.
     * Demonstrates TYPE_SCROLL_INSENSITIVE to navigate forward and backward.
     */
    public List<String> getAppointmentSummaries() throws SQLException {
        List<String> summaries = new ArrayList<>();
        String sql = "SELECT a.appointment_id, p.name AS patient_name, d.name AS doctor_name, " +
                     "a.date_time, a.status, a.type " +
                     "FROM appointments a " +
                     "JOIN patients p ON a.patient_id = p.patient_id " +
                     "JOIN doctors d ON a.doctor_id = d.doctor_id " +
                     "ORDER BY a.date_time DESC";

        // CHAPTER 7: Scrollable ResultSet
        try (Statement stmt = DBConnection.getConnection().createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
             ResultSet rs = stmt.executeQuery(sql)) {

            // Demonstrate scrolling: jump to last row first to show total count
            if (rs.last()) {
                System.out.println("Total appointments in DB: " + rs.getRow());
                rs.beforeFirst(); // scroll back to start
            }

            while (rs.next()) {
                summaries.add(String.format("%-12s | %-20s | %-20s | %-16s | %-10s | %s",
                        rs.getString("appointment_id"),
                        rs.getString("patient_name"),
                        rs.getString("doctor_name"),
                        rs.getTimestamp("date_time").toLocalDateTime()
                           .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                        rs.getString("status"),
                        rs.getString("type")));
            }
        }
        return summaries;
    }

    public void deleteAppointment(String id) throws SQLException {
        String sql = "DELETE FROM appointments WHERE appointment_id = ?";
        try (PreparedStatement pstmt = DBConnection.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, id);
            pstmt.executeUpdate();
            System.out.println("🗑️ Appointment " + id + " deleted from DB.");
        }
    }
}
